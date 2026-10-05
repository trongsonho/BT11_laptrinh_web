package vn.iotstar.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;

import java.io.IOException;

public class AuthUtil {

    /**
     * Checks if current session belongs to an authenticated ADMIN.
     * If unauthenticated, redirects to /login and returns false.
     * If authenticated but not ADMIN, returns HTTP 403 Forbidden and returns false.
     * If authenticated as ADMIN, returns true.
     */
    public static boolean checkAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: You do not have permission to access this resource.");
            return false;
        }

        return true;
    }
}