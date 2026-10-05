package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.entity.User;
import vn.iotstar.service.AuthService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet
        extends HttpServlet {

    private final AuthService authService =
            new AuthService();

    @Override
    public void init() throws ServletException {
        super.init();
        vn.iotstar.config.DatabaseInitializer.initializeDefaultAdmin();
    }

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

        req.getRequestDispatcher(
                "/login.jsp")
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

        String password =
                req.getParameter("password");

        User user =
                authService.findUserByUsername(username);

        if (user == null || !vn.iotstar.util.PasswordUtil.matches(password, user.getPassword())) {

            req.setAttribute(
                    "error",
                    "Sai tên đăng nhập hoặc mật khẩu.");

            req.getRequestDispatcher(
                    "/login.jsp")
                    .forward(req, resp);

            return;
        }

        if (!user.isActive()) {

            req.setAttribute(
                    "error",
                    "Tài khoản chưa được kích hoạt. Vui lòng xác thực mã OTP để kích hoạt tài khoản.");

            req.getRequestDispatcher(
                    "/login.jsp")
                    .forward(req, resp);

            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute(
                "user",
                user);

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(
                    req.getContextPath()
                            + "/admin/products");
        } else {
            resp.sendRedirect(
                    req.getContextPath()
                            + "/");
        }
    }
}
