package vn.edu.hcmute.exam.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.exam.service.IUserService_24133049;
import vn.edu.hcmute.exam.service.UserServiceImpl_24133049;

import java.io.IOException;

@WebServlet(name = "VerifyOtpController_24133049", urlPatterns = {"/verify-otp", "/resend-otp"})
public class VerifyOtpController_24133049 extends HttpServlet {

    private final IUserService_24133049 userService = new UserServiceImpl_24133049();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        String email = req.getParameter("email");

        if ("/resend-otp".equals(path)) {
            String result = userService.resendOtp(email);
            if ("SUCCESS".equals(result)) {
                req.getSession(true).setAttribute("infoMessage", "Mã OTP mới đã được gửi lại vào email của bạn.");
            } else {
                req.getSession(true).setAttribute("errorMessage", result);
            }
            resp.sendRedirect(req.getContextPath() + "/verify-otp?email=" + (email != null ? email : ""));
            return;
        }

        req.setAttribute("email", email);
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String otp = req.getParameter("otp");

        String result = userService.verifyOtp(email, otp);
        if ("SUCCESS".equals(result)) {
            req.getSession(true).setAttribute("successMessage", "Tài khoản của bạn đã được kích hoạt thành công! Vui lòng đăng nhập.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.setAttribute("errorMessage", result);
            req.setAttribute("email", email);
            resp.setContentType("text/html;charset=UTF-8");
            req.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").include(req, resp);
        }
    }
}
