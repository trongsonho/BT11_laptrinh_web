package vn.edu.hcmute.exam.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.exam.model.User_24133049;
import vn.edu.hcmute.exam.service.IUserService_24133049;
import vn.edu.hcmute.exam.service.UserServiceImpl_24133049;

import java.io.IOException;

@WebServlet(name = "RegisterController_24133049", urlPatterns = {"/register"})
public class RegisterController_24133049 extends HttpServlet {

    private final IUserService_24133049 userService = new UserServiceImpl_24133049();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (password == null || !password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp.");
            req.setAttribute("email", email);
            req.setAttribute("fullname", fullname);
            req.setAttribute("phone", phone);
            resp.setContentType("text/html;charset=UTF-8");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").include(req, resp);
            return;
        }

        User_24133049 user = new User_24133049();
        user.setEmail(email);
        user.setFullname(fullname);
        user.setPhone(phone);

        String result = userService.register(user, password);
        if ("SUCCESS".equals(result)) {
            req.getSession(true).setAttribute("infoMessage", "Mã OTP kích hoạt đã được gửi tới email: " + email + ". Vui lòng nhập mã OTP để kích hoạt.");
            resp.sendRedirect(req.getContextPath() + "/verify-otp?email=" + email);
        } else {
            req.setAttribute("errorMessage", result);
            req.setAttribute("email", email);
            req.setAttribute("fullname", fullname);
            req.setAttribute("phone", phone);
            resp.setContentType("text/html;charset=UTF-8");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").include(req, resp);
        }
    }
}
