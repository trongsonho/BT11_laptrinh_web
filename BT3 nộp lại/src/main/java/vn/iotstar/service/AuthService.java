package vn.iotstar.service;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.OtpUtil;
import vn.iotstar.util.PasswordUtil;

import java.time.LocalDateTime;

public class AuthService {

    private final UserDao userDao =
            new UserDaoImpl();

    public String register(
            String username,
            String email,
            String password) {

        if (username == null || username.trim().isEmpty()) {
            return "Tên đăng nhập không được để trống";
        }

        if (email == null || email.trim().isEmpty()) {
            return "Email không được để trống";
        }

        if (password == null || password.trim().isEmpty()) {
            return "Mật khẩu không được để trống";
        }

        String trimmedUsername = username.trim();
        String trimmedEmail = email.trim();

        User existingByUsername = userDao.findByUsername(trimmedUsername);
        if (existingByUsername != null && existingByUsername.isActive()) {
            return "Tên đăng nhập đã tồn tại";
        }

        User existingByEmail = userDao.findByEmail(trimmedEmail);
        if (existingByEmail != null && existingByEmail.isActive()) {
            return "Email đã tồn tại";
        }

        User user;
        boolean isNewUser = false;

        if (existingByUsername != null && !existingByUsername.isActive()) {
            user = existingByUsername;
            user.setEmail(trimmedEmail);
        } else if (existingByEmail != null && !existingByEmail.isActive()) {
            user = existingByEmail;
            user.setUsername(trimmedUsername);
        } else {
            user = new User();
            user.setUsername(trimmedUsername);
            user.setEmail(trimmedEmail);
            isNewUser = true;
        }

        user.setPassword(
                PasswordUtil.hashPassword(password));
        user.setActive(false);
        user.setRole("USER");

        String otp =
                OtpUtil.generateOtp();

        user.setOtp(otp);
        user.setOtpExpiry(
                LocalDateTime.now().plusMinutes(5));

        if (isNewUser) {
            userDao.insert(user);
        } else {
            userDao.update(user);
        }

        try {
            EmailUtil.sendOtp(
                    trimmedEmail,
                    otp,
                    "Kích hoạt tài khoản");
        } catch (IllegalStateException e) {
            if (isNewUser && user.getId() != null) {
                userDao.delete(user.getId());
            }
            return "Dịch vụ gửi email OTP chưa được cấu hình (thiếu MAIL_USERNAME/MAIL_PASSWORD). Vui lòng liên hệ quản trị viên hoặc cấu hình file mail.properties.";
        } catch (Exception e) {
            if (isNewUser && user.getId() != null) {
                userDao.delete(user.getId());
            }
            return "Không thể gửi email OTP đến " + trimmedEmail + ": " + e.getMessage();
        }

        return null;
    }

    public boolean verifyOtp(
            String email,
            String otp) {

        User user =
                userDao.findByEmail(email);

        if (user == null) {
            return false;
        }

        if (user.isActive()) {
            return true;
        }

        if (user.getOtp() == null
                || user.getOtpExpiry() == null) {
            return false;
        }

        if (LocalDateTime.now()
                .isAfter(user.getOtpExpiry())) {
            return false;
        }

        if (!user.getOtp().equals(otp)) {
            return false;
        }

        user.setActive(true);
        user.setOtp(null);
        user.setOtpExpiry(null);

        userDao.update(user);

        return true;
    }

    public User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User login(
            String username,
            String password) {

        User user =
                userDao.findByUsername(username);

        if (user == null) {
            return null;
        }

        if (!user.isActive()) {
            return null;
        }

        if (!PasswordUtil.matches(
                password,
                user.getPassword())) {
            return null;
        }

        return user;
    }

    public boolean sendResetOtp(
            String email) {

        User user =
                userDao.findByEmail(email);

        if (user == null) {
            return false;
        }

        String otp =
                OtpUtil.generateOtp();

        user.setResetOtp(otp);

        user.setResetOtpExpiry(
                LocalDateTime.now()
                        .plusMinutes(5));

        try {
            EmailUtil.sendOtp(
                    email,
                    otp,
                    "Đặt lại mật khẩu");
            userDao.update(user);
            return true;
        } catch (Exception e) {
            System.err.println("[AuthService] Failed to send reset OTP: " + e.getMessage());
            return false;
        }
    }

    public boolean resetPassword(
            String email,
            String otp,
            String newPassword) {

        User user =
                userDao.findByEmail(email);

        if (user == null) {
            return false;
        }

        if (user.getResetOtp() == null
                || user.getResetOtpExpiry() == null) {
            return false;
        }

        if (LocalDateTime.now()
                .isAfter(user.getResetOtpExpiry())) {
            return false;
        }

        if (!user.getResetOtp().equals(otp)) {
            return false;
        }

        user.setPassword(
                PasswordUtil.hashPassword(
                        newPassword));

        user.setResetOtp(null);
        user.setResetOtpExpiry(null);

        userDao.update(user);

        return true;
    }
}
