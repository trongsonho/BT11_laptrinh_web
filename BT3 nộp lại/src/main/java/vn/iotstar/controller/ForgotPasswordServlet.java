package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.AuthService;

import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet
        extends HttpServlet {

    private final AuthService authService =
            new AuthService();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(
                "/forgot-password.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        String email =
                req.getParameter("email");

        boolean success = false;
        String errorMsg = null;
        try {
            success = authService.sendResetOtp(email);
            if (!success) {
                errorMsg = "Email không tồn tại trên hệ thống.";
            }
        } catch (IllegalStateException e) {
            errorMsg = "Dịch vụ gửi email OTP chưa được cấu hình. Vui lòng cấu hình tài khoản gửi thư trong mail.properties hoặc biến môi trường.";
        } catch (Exception e) {
            errorMsg = "Lỗi khi gửi email đặt lại mật khẩu: " + e.getMessage();
        }

        if (!success) {

            req.setAttribute(
                    "error",
                    errorMsg != null ? errorMsg : "Email không tồn tại");
            req.setAttribute(
                    "email",
                    email);

            req.getRequestDispatcher(
                    "/forgot-password.jsp")
                    .forward(req, resp);

            return;
        }

        HttpSession session =
                req.getSession();

        session.setAttribute(
                "resetEmail",
                email);

        req.setAttribute(
                "email",
                email);

        req.getRequestDispatcher(
                "/reset-password.jsp")
                .forward(req, resp);
    }
}
