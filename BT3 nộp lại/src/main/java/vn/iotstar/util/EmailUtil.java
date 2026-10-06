package vn.iotstar.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

public class EmailUtil {

    private static final String DEFAULT_HOST = "smtp.gmail.com";
    private static final String DEFAULT_PORT = "587";
    private static final String CONFIG_FILE = "mail.properties";

    /**
     * Resolves a configuration key in priority order:
     * 1. System property (-Dkey=value)
     * 2. OS Environment variable
     * 3. Classpath mail.properties file
     * 4. Fallback default
     */
    public static String getProperty(String key, String fallback) {
        // 1. System property (try key as-is, uppercase_underscore, and lowercase.dot)
        String val = System.getProperty(key);
        if (val == null || val.isBlank()) {
            val = System.getProperty(key.toUpperCase().replace('.', '_'));
        }
        if (val == null || val.isBlank()) {
            val = System.getProperty(key.toLowerCase().replace('_', '.'));
        }
        if (val != null && !val.isBlank()) {
            return val.trim();
        }

        // 2. OS Environment variable
        String envKey = key.toUpperCase().replace('.', '_');
        val = System.getenv(envKey);
        if (val != null && !val.isBlank()) {
            return val.trim();
        }

        // 3. Classpath mail.properties
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                val = props.getProperty(key);
                if (val == null || val.isBlank()) {
                    val = props.getProperty(envKey);
                }
                if (val == null || val.isBlank()) {
                    val = props.getProperty(key.toLowerCase().replace('_', '.'));
                }
                if (val != null && !val.isBlank()) {
                    return val.trim();
                }
            }
        } catch (Exception ignored) {
        }

        // Try also via class loader of EmailUtil
        try (InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                val = props.getProperty(key);
                if (val == null || val.isBlank()) {
                    val = props.getProperty(envKey);
                }
                if (val == null || val.isBlank()) {
                    val = props.getProperty(key.toLowerCase().replace('_', '.'));
                }
                if (val != null && !val.isBlank()) {
                    return val.trim();
                }
            }
        } catch (Exception ignored) {
        }

        return fallback;
    }

    public static boolean isMailConfigured() {
        String username = getProperty("MAIL_USERNAME", null);
        String password = getProperty("MAIL_PASSWORD", null);
        return username != null && !username.isBlank() && password != null && !password.isBlank();
    }

    public static void sendOtp(
            String recipient,
            String otp,
            String purpose) {

        String host = getProperty("MAIL_HOST", DEFAULT_HOST);
        String port = getProperty("MAIL_PORT", DEFAULT_PORT);
        String username = getProperty("MAIL_USERNAME", null);
        String password = getProperty("MAIL_PASSWORD", null);
        String from = getProperty("MAIL_FROM", username);

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "MAIL_USERNAME / MAIL_PASSWORD chưa được cấu hình. Vui lòng cấu hình tài khoản gửi thư trong mail.properties hoặc biến môi trường.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.connectiontimeout", "7000");
        props.put("mail.smtp.timeout", "7000");
        props.put("mail.smtp.writetimeout", "7000");

        final String authUser = username;
        final String authPass = password;

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(authUser, authPass);
            }
        });

        try {
            Message message = new MimeMessage(session);

            String sender = (from != null && !from.isBlank()) ? from : username;
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message.setSubject("OTP - " + purpose);
            message.setText(
                    "Mã OTP của bạn là: " + otp + "\n\n"
                    + "Mã có hiệu lực trong 5 phút.\n"
                    + "Vui lòng không chia sẻ mã này cho bất kỳ ai.");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email OTP đến " + recipient + ": " + e.getMessage(), e);
        }
    }
}