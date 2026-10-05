package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.AuthService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet
        extends HttpServlet {

    private final AuthService authService =
            new AuthService();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // Tự động tạo tên đăng nhập mặc định duy nhất theo timestamp
        long timestampSuffix = System.currentTimeMillis() % 1000000;
        String defaultUsername = "user" + timestampSuffix;

        // Tự động tạo mật khẩu tạm thời mặc định an toàn
        int randomDigits = 1000 + new java.util.Random().nextInt(9000);
        String defaultPassword = "Pass@" + randomDigits;

        req.setAttribute("defaultUsername", defaultUsername);
        req.setAttribute("defaultPassword", defaultPassword);

        req.getRequestDispatcher(
                "/register.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username =
                req.getParameter("username");

        String email =
                req.getParameter("email");

        String password =
                req.getParameter("password");

        String error;
        try {
            error = authService.register(
                    username,
                    email,
                    password);
        } catch (Exception e) {
            error = "Lỗi xử lý đăng ký: " + e.getMessage();
        }

        if (error != null) {

            req.setAttribute(
                    "error",
                    error);
            req.setAttribute(
                    "username",
                    username);
            req.setAttribute(
                    "password",
                    password);
            req.setAttribute(
                    "email",
                    email);

            req.getRequestDispatcher(
                    "/register.jsp")
                    .forward(req, resp);

            return;
        }

        HttpSession session =
                req.getSession();

        session.setAttribute(
                "verifyEmail",
                email);

        resp.sendRedirect(
                req.getContextPath()
                        + "/verify");
    }
}
