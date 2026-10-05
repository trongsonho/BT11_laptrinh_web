package vn.edu.hcmute.exam.config;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext_24133049 {

    private static SQLServerDataSource dataSource;

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            initDataSource();
        } catch (Exception e) {
            System.err.println("[DBContext_24133049] Lỗi khởi tạo DataSource: " + e.getMessage());
        }
    }

    private static synchronized void initDataSource() {
        if (dataSource == null) {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName("127.0.0.1");
            ds.setInstanceName("SQLEXPRESS");
            ds.setDatabaseName("BookStore_24133049");
            ds.setIntegratedSecurity(true);
            ds.setTrustServerCertificate(true);
            ds.setEncrypt("true");
            ds.setLoginTimeout(5);
            ds.setSendStringParametersAsUnicode(true);
            dataSource = ds;
        }
    }

    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("DB_URL");
        if (dbUrl != null && !dbUrl.trim().isEmpty()) {
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");
            if (dbUser != null && !dbUser.trim().isEmpty() && dbPassword != null) {
                return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            } else {
                return DriverManager.getConnection(dbUrl);
            }
        }

        if (dataSource != null) {
            return dataSource.getConnection();
        }

        return DriverManager.getConnection("jdbc:sqlserver://127.0.0.1;instanceName=SQLEXPRESS;databaseName=BookStore_24133049;encrypt=true;trustServerCertificate=true;integratedSecurity=true;loginTimeout=5;sendStringParametersAsUnicode=true;");
    }

    public static void close(AutoCloseable... resources) {
        for (AutoCloseable res : resources) {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
