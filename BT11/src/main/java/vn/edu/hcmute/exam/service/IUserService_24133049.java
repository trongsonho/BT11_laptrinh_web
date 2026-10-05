package vn.edu.hcmute.exam.service;

import vn.edu.hcmute.exam.model.User_24133049;

public interface IUserService_24133049 {
    String register(User_24133049 user, String rawPassword);
    String verifyOtp(String email, String enteredOtp);
    String resendOtp(String email);
    User_24133049 login(String email, String rawPassword);
    User_24133049 getUserById(int id);
    User_24133049 getUserByEmail(String email);
}
