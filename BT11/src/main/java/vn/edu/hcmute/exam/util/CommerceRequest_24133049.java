package vn.edu.hcmute.exam.util;

import jakarta.servlet.http.*;
import vn.edu.hcmute.exam.service.CartService_24133049;
import vn.edu.hcmute.exam.model.*;
import java.io.IOException;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

public final class CommerceRequest_24133049 {
    private CommerceRequest_24133049() { }
    public static String token() { return UUID.randomUUID().toString(); }
    public static boolean sameToken(String expected, String actual) {
        return expected != null && actual != null && MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8), actual.getBytes(StandardCharsets.UTF_8));
    }
    public static User_24133049 requireUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        HttpSession session = req.getSession(false);
        User_24133049 user = session == null ? null : (User_24133049) session.getAttribute("currentUser");
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return null; }
        User_24133049 fresh = new CartService_24133049().findUser(user.getId());
        if (fresh == null || !fresh.isActive() || fresh.isAdmin()) {
            resp.sendError(403, "Chỉ tài khoản User đang hoạt động được mua hàng."); return null;
        }
        session.setAttribute("currentUser", fresh); return fresh;
    }
    public static Cart_24133049 cart(HttpSession session, int userId) {
        Cart_24133049 cart = (Cart_24133049) session.getAttribute("shoppingCart");
        if (cart == null || cart.getUserId() != userId) {
            cart = new Cart_24133049(userId); session.setAttribute("shoppingCart", cart);
        }
        return cart;
    }
    public static boolean csrf(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (sameToken((String) req.getSession().getAttribute("commerceCsrf"), req.getParameter("csrfToken"))) return true;
        resp.sendError(403, "CSRF token không hợp lệ. Vui lòng tải lại trang."); return false;
    }
    public static int bookId(String value) {
        try { return vn.edu.hcmute.exam.service.CartService_24133049.parseQuantity(value); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("Mã sách không hợp lệ."); }
    }
}
