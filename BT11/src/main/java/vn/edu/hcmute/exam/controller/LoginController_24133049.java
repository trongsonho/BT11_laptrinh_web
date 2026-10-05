package vn.edu.hcmute.exam.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.exam.model.User_24133049;
import vn.edu.hcmute.exam.service.IUserService_24133049;
import vn.edu.hcmute.exam.service.UserServiceImpl_24133049;

import java.io.IOException;

@WebServlet(name = "LoginController_24133049", urlPatterns = {"/login"})
public class LoginController_24133049 extends HttpServlet {

    private final IUserService_24133049 userService = new UserServiceImpl_24133049();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            String errorMsg = (String) session.getAttribute("errorMessage");
            if (errorMsg != null) {
                req.setAttribute("errorMessage", errorMsg);
                session.removeAttribute("errorMessage");
            }
            String successMsg = (String) session.getAttribute("successMessage");
            if (successMsg != null) {
                req.setAttribute("successMessage", successMsg);
                session.removeAttribute("successMessage");
            }
        }
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User_24133049 user = userService.login(email, password);

        if (user != null) {
            // Đăng nhập thành công -> lưu vào Session
            HttpSession session = req.getSession(true);
            synchronized (session) {
                User_24133049 previous = (User_24133049) session.getAttribute("currentUser");
                if (previous == null || previous.getId() != user.getId()) {
                    session.removeAttribute("shoppingCart");
                    session.removeAttribute("cartMessage");
                    session.setAttribute("commerceCsrf", vn.edu.hcmute.exam.util.CommerceRequest_24133049.token());
                }
                req.changeSessionId();
                session.setAttribute("currentUser", user);
            }

            if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/books");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } else {
            // Đăng nhập thất bại -> quay lại trang đăng nhập với thông báo lỗi
            User_24133049 existing = userService.getUserByEmail(email);
            if (existing != null && !existing.isActive()) {
                req.setAttribute("errorMessage", "Tài khoản của bạn chưa được kích hoạt. Vui lòng nhập mã OTP để kích hoạt tài khoản.");
                req.setAttribute("unactivatedEmail", email);
            } else {
                req.setAttribute("errorMessage", "Email hoặc mật khẩu không chính xác. Vui lòng thử lại.");
            }
            req.setAttribute("email", email);
            resp.setContentType("text/html;charset=UTF-8");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").include(req, resp);
        }
    }
}
