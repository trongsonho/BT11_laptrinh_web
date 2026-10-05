package vn.edu.hcmute.exam.dao;

import vn.edu.hcmute.exam.config.DBContext_24133049;
import vn.edu.hcmute.exam.model.User_24133049;

import java.sql.*;

public class UserDaoImpl_24133049 implements IUserDao_24133049 {

    @Override
    public User_24133049 findByEmail(String email) {
        String sql = "SELECT id, email, fullname, phone, passwd, signup_date, last_login, " +
                     "is_admin, is_active, otp_code, otp_expiry, otp_attempts " +
                     "FROM dbo.users WHERE LOWER(email) = LOWER(?)";

        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User_24133049 findById(int id) {
        String sql = "SELECT id, email, fullname, phone, passwd, signup_date, last_login, " +
                     "is_admin, is_active, otp_code, otp_expiry, otp_attempts " +
                     "FROM dbo.users WHERE id = ?";

        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(User_24133049 user) {
        String sql = "INSERT INTO dbo.users (email, fullname, phone, passwd, signup_date, is_admin, is_active, otp_code, otp_expiry, otp_attempts) " +
                     "VALUES (?, ?, ?, ?, GETDATE(), ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullname());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPasswd());
            ps.setBoolean(5, user.isAdmin());
            ps.setBoolean(6, user.isActive());
            ps.setString(7, user.getOtpCode());
            ps.setTimestamp(8, user.getOtpExpiry());
            ps.setInt(9, user.getOtpAttempts());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean updateOtp(String email, String otpCode, Timestamp expiry) {
        String sql = "UPDATE dbo.users SET otp_code = ?, otp_expiry = ?, otp_attempts = 0 WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, otpCode);
            ps.setTimestamp(2, expiry);
            ps.setString(3, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean incrementOtpAttempts(String email) {
        String sql = "UPDATE dbo.users SET otp_attempts = otp_attempts + 1 WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean resetOtpAttempts(String email) {
        String sql = "UPDATE dbo.users SET otp_attempts = 0 WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean activateUser(String email) {
        String sql = "UPDATE dbo.users SET is_active = 1, otp_code = NULL, otp_expiry = NULL, otp_attempts = 0 WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateLastLogin(int id) {
        String sql = "UPDATE dbo.users SET last_login = GETDATE() WHERE id = ?";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private User_24133049 mapUser(ResultSet rs) throws SQLException {
        User_24133049 u = new User_24133049();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setFullname(rs.getString("fullname"));
        u.setPhone(rs.getString("phone"));
        u.setPasswd(rs.getString("passwd"));
        u.setSignupDate(rs.getTimestamp("signup_date"));
        u.setLastLogin(rs.getTimestamp("last_login"));
        u.setAdmin(rs.getBoolean("is_admin"));
        u.setActive(rs.getBoolean("is_active"));
        u.setOtpCode(rs.getString("otp_code"));
        u.setOtpExpiry(rs.getTimestamp("otp_expiry"));
        u.setOtpAttempts(rs.getInt("otp_attempts"));
        return u;
    }
}
