package vn.edu.hcmute.exam.dao;

import vn.edu.hcmute.exam.model.User_24133049;

import java.sql.Timestamp;

public interface IUserDao_24133049 {
    User_24133049 findByEmail(String email);
    User_24133049 findById(int id);
    int insert(User_24133049 user);
    boolean updateOtp(String email, String otpCode, Timestamp expiry);
    boolean incrementOtpAttempts(String email);
    boolean resetOtpAttempts(String email);
    boolean activateUser(String email);
    boolean updateLastLogin(int id);
}
