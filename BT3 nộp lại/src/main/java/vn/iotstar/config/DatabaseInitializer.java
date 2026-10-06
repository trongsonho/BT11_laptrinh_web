package vn.iotstar.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.util.PasswordUtil;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_RAW_PASSWORD = "123456";
    private static final String DEFAULT_EMAIL = "admin@example.com";
    private static final String DEFAULT_ROLE = "ADMIN";

    private static volatile boolean initialized = false;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initializeDefaultAdmin();
    }

    public static synchronized void initializeDefaultAdmin() {
        if (initialized) {
            return;
        }

        try {
            UserDao userDao = new UserDaoImpl();
            String hashedPassword = PasswordUtil.hashPassword(DEFAULT_RAW_PASSWORD);

            User existing = userDao.findByUsername(DEFAULT_USERNAME);

            if (existing == null) {
                // A. If admin does NOT exist: create it
                User admin = new User();
                admin.setUsername(DEFAULT_USERNAME);
                admin.setEmail(DEFAULT_EMAIL);
                admin.setPassword(hashedPassword);
                admin.setActive(true);
                admin.setRole(DEFAULT_ROLE);
                userDao.insert(admin);
                System.out.println("[DatabaseInitializer] Default admin created: " + DEFAULT_USERNAME + " / " + DEFAULT_RAW_PASSWORD);
            } else {
                // C. Check if the existing admin is already correct
                boolean isPasswordCorrect = hashedPassword.equals(existing.getPassword());
                boolean isEmailValid = existing.getEmail() != null && !existing.getEmail().trim().isEmpty();
                boolean isActive = existing.isActive();
                boolean isRoleCorrect = DEFAULT_ROLE.equalsIgnoreCase(existing.getRole());

                if (!isPasswordCorrect || !isEmailValid || !isActive || !isRoleCorrect) {
                    existing.setEmail(DEFAULT_EMAIL);
                    existing.setPassword(hashedPassword);
                    existing.setActive(true);
                    existing.setRole(DEFAULT_ROLE);
                    existing.setOtp(null);
                    existing.setOtpExpiry(null);
                    existing.setResetOtp(null);
                    existing.setResetOtpExpiry(null);
                    userDao.update(existing);
                    System.out.println("[DatabaseInitializer] Updated admin account to valid ADMIN test account: "
                            + DEFAULT_USERNAME + " / " + DEFAULT_RAW_PASSWORD);
                } else {
                    System.out.println("[DatabaseInitializer] Admin account is already valid ADMIN. Unchanged.");
                }
            }

            // Ensure a default normal user exists for testing normal user flow
            User normalUser = userDao.findByUsername("user");
            if (normalUser == null) {
                User u = new User();
                u.setUsername("user");
                u.setEmail("user@example.com");
                u.setPassword(hashedPassword);
                u.setActive(true);
                u.setRole("USER");
                userDao.insert(u);
                System.out.println("[DatabaseInitializer] Default normal user created: user / 123456");
            }

            initialized = true;
        } catch (Throwable t) {
            System.err.println("[DatabaseInitializer] Warning during database initialization: " + t.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}