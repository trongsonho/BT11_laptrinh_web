package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.AuthService;

import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet
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
                email = (String) session.getAttribute("resetEmail");
            }
        }

        if (email != null) {
            req.setAttribute("email", email);
        }

        req.getRequestDispatcher(
                "/reset-password.jsp")
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
                email = (String) session.getAttribute("resetEmail");
            }
        }

        String otp =
                req.getParameter("otp");

        String newPassword =
                req.getParameter("password");

        if (email == null || email.isBlank()) {
            req.setAttribute(
                    "error",
                    "Vui lòng nhập email.");
            req.getRequestDispatcher(
                    "/reset-password.jsp")
                    .forward(req, resp);
            return;
        }

        boolean success =
                authService.resetPassword(
                        email,
                        otp,
                        newPassword);

        if (!success) {

            req.setAttribute(
                    "error",
                    "OTP không hợp lệ hoặc đã hết hạn");

            req.setAttribute(
                    "email",
                    email);

            req.getRequestDispatcher(
                    "/reset-password.jsp")
                    .forward(req, resp);

            return;
        }

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute("resetEmail");
        }

        resp.sendRedirect(
                req.getContextPath()
                        + "/login");
    }
}
