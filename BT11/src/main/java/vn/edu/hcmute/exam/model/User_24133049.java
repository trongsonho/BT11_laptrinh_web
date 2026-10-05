package vn.edu.hcmute.exam.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class User_24133049 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String email;
    private String fullname;
    private String phone;
    private String passwd;
    private Timestamp signupDate;
    private Timestamp lastLogin;
    private boolean admin;
    private boolean active;
    private String otpCode;
    private Timestamp otpExpiry;
    private int otpAttempts;

    public User_24133049() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Timestamp getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(Timestamp signupDate) {
        this.signupDate = signupDate;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public Timestamp getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(Timestamp otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public int getOtpAttempts() {
        return otpAttempts;
    }

    public void setOtpAttempts(int otpAttempts) {
        this.otpAttempts = otpAttempts;
    }
}
