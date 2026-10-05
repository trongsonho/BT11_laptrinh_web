package vn.edu.hcmute.exam.service;

import vn.edu.hcmute.exam.dao.IUserDao_24133049;
import vn.edu.hcmute.exam.dao.UserDaoImpl_24133049;
import vn.edu.hcmute.exam.model.User_24133049;
import vn.edu.hcmute.exam.util.EmailUtil_24133049;
import vn.edu.hcmute.exam.util.PasswordUtil_24133049;

import java.sql.Timestamp;

public class UserServiceImpl_24133049 implements IUserService_24133049 {

    private static final int MAX_OTP_ATTEMPTS = 5;
    private static final long OTP_VALIDITY_MS = 5 * 60 * 1000; // 5 phút

    private final IUserDao_24133049 userDao;

    public UserServiceImpl_24133049() {
        this.userDao = new UserDaoImpl_24133049();
    }

    public UserServiceImpl_24133049(IUserDao_24133049 userDao) {
        this.userDao = userDao;
    }

    @Override
    public String register(User_24133049 user, String rawPassword) {
        if (user == null || user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return "Email không được để trống.";
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            return "Mật khẩu phải có ít nhất 6 ký tự.";
        }
        if (user.getFullname() == null || user.getFullname().trim().isEmpty()) {
            return "Họ và tên không được để trống.";
        }

        String email = user.getEmail().trim().toLowerCase();
        User_24133049 existing = userDao.findByEmail(email);
        if (existing != null) {
            if (existing.isActive()) {
                return "Email này đã được đăng ký và kích hoạt trong hệ thống.";
            } else {
                // Đã đăng ký nhưng chưa kích hoạt: cập nhật mã OTP mới và gửi lại
                String otp = EmailUtil_24133049.generateOtp(6);
                Timestamp expiry = new Timestamp(System.currentTimeMillis() + OTP_VALIDITY_MS);
                boolean sent = EmailUtil_24133049.sendOtpEmail(email, otp, user.getFullname());
                if (!sent) {
                    return "Không thể gửi email OTP. Vui lòng kiểm tra lại cấu hình SMTP hoặc kết nối mạng.";
                }
                userDao.updateOtp(email, otp, expiry);
                return "SUCCESS";
            }
        }

        String hashedPassword = PasswordUtil_24133049.hashPassword(rawPassword);
        String otp = EmailUtil_24133049.generateOtp(6);
        Timestamp expiry = new Timestamp(System.currentTimeMillis() + OTP_VALIDITY_MS);

        user.setEmail(email);
        user.setPasswd(hashedPassword);
        user.setActive(false);
        user.setAdmin(false);
        user.setOtpCode(otp);
        user.setOtpExpiry(expiry);
        user.setOtpAttempts(0);

        // Gửi email OTP
        boolean sent = EmailUtil_24133049.sendOtpEmail(email, otp, user.getFullname());
        if (!sent) {
            return "Không thể gửi email OTP kích hoạt. Vui lòng kiểm tra lại cấu hình SMTP hoặc kết nối mạng.";
        }

        int userId = userDao.insert(user);
        if (userId <= 0) {
            return "Lỗi cơ sở dữ liệu khi lưu thông tin tài khoản.";
        }

        return "SUCCESS";
    }

    @Override
    public String verifyOtp(String email, String enteredOtp) {
        if (email == null || email.trim().isEmpty() || enteredOtp == null || enteredOtp.trim().isEmpty()) {
            return "Vui lòng nhập đầy đủ email và mã OTP.";
        }

        email = email.trim().toLowerCase();
        User_24133049 user = userDao.findByEmail(email);
        if (user == null) {
            return "Tài khoản với email này không tồn tại.";
        }

        if (user.isActive()) {
            return "Tài khoản đã được kích hoạt trước đó. Bạn có thể đăng nhập ngay.";
        }

        if (user.getOtpAttempts() >= MAX_OTP_ATTEMPTS) {
            return "Bạn đã nhập sai OTP quá " + MAX_OTP_ATTEMPTS + " lần. Vui lòng bấm 'Gửi lại mã OTP' để nhận mã mới.";
        }

        if (user.getOtpExpiry() == null || System.currentTimeMillis() > user.getOtpExpiry().getTime()) {
            return "Mã OTP đã hết hạn (quá 5 phút). Vui lòng bấm 'Gửi lại mã OTP'.";
        }

        if (!enteredOtp.trim().equals(user.getOtpCode())) {
            userDao.incrementOtpAttempts(email);
            int remaining = MAX_OTP_ATTEMPTS - (user.getOtpAttempts() + 1);
            if (remaining > 0) {
                return "Mã OTP không chính xác. Bạn còn " + remaining + " lần thử.";
            } else {
                return "Mã OTP không chính xác. Bạn đã hết số lần thử. Vui lòng yêu cầu gửi lại mã mới.";
            }
        }

        // OTP đúng -> kích hoạt tài khoản
        boolean activated = userDao.activateUser(email);
        if (!activated) {
            return "Kích hoạt tài khoản thất bại do lỗi hệ thống.";
        }

        return "SUCCESS";
    }

    @Override
    public String resendOtp(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email không hợp lệ.";
        }

        email = email.trim().toLowerCase();
        User_24133049 user = userDao.findByEmail(email);
        if (user == null) {
            return "Tài khoản không tồn tại.";
        }

        if (user.isActive()) {
            return "Tài khoản đã kích hoạt, không cần gửi lại OTP.";
        }

        String otp = EmailUtil_24133049.generateOtp(6);
        Timestamp expiry = new Timestamp(System.currentTimeMillis() + OTP_VALIDITY_MS);

        boolean sent = EmailUtil_24133049.sendOtpEmail(email, otp, user.getFullname());
        if (!sent) {
            return "Không thể gửi email OTP. Vui lòng kiểm tra lại cấu hình SMTP.";
        }

        userDao.updateOtp(email, otp, expiry);
        return "SUCCESS";
    }

    @Override
    public User_24133049 login(String email, String rawPassword) {
        if (email == null || email.trim().isEmpty() || rawPassword == null) {
            return null;
        }

        email = email.trim().toLowerCase();
        User_24133049 user = userDao.findByEmail(email);
        if (user == null) {
            return null;
        }

        if (!user.isActive()) {
            return null; // Chưa kích hoạt
        }

        boolean matched = PasswordUtil_24133049.checkPassword(rawPassword, user.getPasswd());
        if (!matched) {
            return null;
        }

        // Đăng nhập thành công -> Cập nhật last_login
        userDao.updateLastLogin(user.getId());
        return user;
    }

    @Override
    public User_24133049 getUserById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User_24133049 getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
