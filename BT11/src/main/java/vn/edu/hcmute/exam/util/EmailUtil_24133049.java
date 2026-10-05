package vn.edu.hcmute.exam.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class EmailUtil_24133049 {

    private static final String CONFIG_FILE = "mail.properties";

    public static String generateOtp(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static boolean isDevModeEnabled() {
        String devMode = System.getenv("OTP_DEV_MODE");
        if (devMode == null) {
            devMode = System.getProperty("otp.dev.mode", "false");
        }
        return "true".equalsIgnoreCase(devMode);
    }

    public static boolean sendOtpEmail(String recipientEmail, String otpCode, String recipientName) {
        Properties mailProps = new Properties();

        // Đọc từ file cấu hình nếu có
        try (InputStream in = EmailUtil_24133049.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in != null) {
                mailProps.load(in);
            }
        } catch (Exception ignored) {
        }

        // Ưu tiên biến môi trường hoặc system properties
        String host = getProp(mailProps, "mail.smtp.host", "SMTP_HOST", "smtp.gmail.com");
        String port = getProp(mailProps, "mail.smtp.port", "SMTP_PORT", "587");
        String auth = getProp(mailProps, "mail.smtp.auth", "SMTP_AUTH", "true");
        String starttls = getProp(mailProps, "mail.smtp.starttls.enable", "SMTP_STARTTLS", "true");
        String username = getProp(mailProps, "mail.username", "SMTP_USERNAME", null);
        String password = getProp(mailProps, "mail.password", "SMTP_PASSWORD", null);
        String from = getProp(mailProps, "mail.from", "SMTP_FROM", username);

        // Kiểm tra xem SMTP đã được cấu hình chưa
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.err.println("[EmailUtil_24133049] LỖI: Chưa cấu hình tài khoản SMTP (SMTP_USERNAME / SMTP_PASSWORD).");
            if (isDevModeEnabled()) {
                System.out.println("[EmailUtil_24133049 - DEV MODE] Mã OTP dành cho " + recipientEmail + " là: " + otpCode);
                return true;
            }
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from, "BookStore 24133049 - Hồ Trọng Sơn"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Mã OTP kích hoạt tài khoản - BookStore 24133049");

            String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                    + "<h2 style='color: #2c3e50; text-align: center;'>KÍCH HOẠT TÀI KHOẢN BOOKSTORE</h2>"
                    + "<p>Xin chào <b>" + (recipientName != null ? recipientName : "Quý khách") + "</b>,</p>"
                    + "<p>Cảm ơn bạn đã đăng ký tài khoản tại BookStore. Mã xác thực OTP của bạn là:</p>"
                    + "<div style='background-color: #f1f8ff; padding: 15px; text-align: center; font-size: 28px; font-weight: bold; letter-spacing: 5px; color: #0366d6; border-radius: 4px; margin: 20px 0;'>"
                    + otpCode
                    + "</div>"
                    + "<p>Mã OTP này có hiệu lực trong vòng <b>5 phút</b>. Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>"
                    + "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'/>"
                    + "<p style='font-size: 12px; color: #888; text-align: center;'>Bài thi Lập Trình Web - Đề 02 - MSSV: 24133049 - Hồ Trọng Sơn</p>"
                    + "</div>";

            message.setContent(content, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("[EmailUtil_24133049] Gửi mã OTP thành công tới: " + recipientEmail);
            return true;
        } catch (Exception e) {
            System.err.println("[EmailUtil_24133049] Lỗi khi gửi email OTP: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String getProp(Properties props, String propKey, String envKey, String defaultVal) {
        String val = System.getenv(envKey);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }
        val = System.getProperty(propKey);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }
        val = props.getProperty(propKey);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }
        return defaultVal;
    }
}
