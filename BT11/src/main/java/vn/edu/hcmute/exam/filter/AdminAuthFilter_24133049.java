package vn.edu.hcmute.exam.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.exam.model.User_24133049;

import java.io.IOException;

public class AdminAuthFilter_24133049 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        User_24133049 currentUser = (session != null) ? (User_24133049) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            req.getSession(true).setAttribute("errorMessage", "Vui lòng đăng nhập với tài khoản Admin để truy cập trang quản trị.");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!currentUser.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Từ chối truy cập: Bạn không có quyền quản trị viên.");
            return;
        }

        chain.doFilter(request, response);
    }
}
