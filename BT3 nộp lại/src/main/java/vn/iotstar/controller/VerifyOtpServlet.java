package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.AuthService;

import java.io.IOException;

@WebServlet("/verify")
public class VerifyOtpServlet
        extends HttpServlet {

    private final AuthService authService =
            new AuthService();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        if (email == null || email.isBlank()) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                email = (String) session.getAttribute("verifyEmail");
            }
        }

        if (email != null) {
            req.setAttribute("email", email);
        }

        req.getRequestDispatcher(
                "/verify.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String email =
                req.getParameter("email");

        if (email == null || email.isBlank()) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                email = (String) session.getAttribute("verifyEmail");
            }
        }

        String otp =
                req.getParameter("otp");

        if (email == null || email.isBlank()) {
            req.setAttribute(
                    "error",
                    "Vui lòng nhập email.");
            req.getRequestDispatcher(
                    "/verify.jsp")
                    .forward(req, resp);
            return;
        }

        boolean success =
                authService.verifyOtp(
                        email,
                        otp);

        if (!success) {

            req.setAttribute(
                    "error",
                    "OTP không hợp lệ hoặc đã hết hạn");

            req.setAttribute(
                    "email",
                    email);

            req.getRequestDispatcher(
                    "/verify.jsp")
                    .forward(req, resp);

            return;
        }

        HttpSession session = req.getSession();
        if (session != null) {
            session.removeAttribute("verifyEmail");
            session.setAttribute("message", "Tài khoản đã được kích hoạt. Vui lòng đăng nhập.");
        }

        resp.sendRedirect(
                req.getContextPath()
                        + "/login");
    }
}
