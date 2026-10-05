package vn.edu.hcmute.exam.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.exam.model.*;
import vn.edu.hcmute.exam.service.CartService_24133049;
import vn.edu.hcmute.exam.util.CommerceRequest_24133049;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="CartController_24133049", urlPatterns={"/cart"})
public class CartController_24133049 extends HttpServlet {
    private final CartService_24133049 service = new CartService_24133049();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (req.getSession()) {
            try {
                User_24133049 user = CommerceRequest_24133049.requireUser(req, resp); if (user == null) return;
                Cart_24133049 cart = CommerceRequest_24133049.cart(req.getSession(), user.getId());
                var lines = service.lines(cart);
                req.setAttribute("cartLines", lines); req.setAttribute("cartTotal", CartService_24133049.total(lines));
                req.setAttribute("cartValid", !lines.isEmpty() && lines.stream().allMatch(l -> l.getError() == null));
                req.setAttribute("cartQuantity", cart.getTotalQuantity());
                req.setAttribute("cartMessage", req.getSession().getAttribute("cartMessage")); req.getSession().removeAttribute("cartMessage");
                resp.setContentType("text/html;charset=UTF-8");
                req.getRequestDispatcher("/WEB-INF/views/cart.jsp").include(req, resp);
            } catch (SQLException e) { log("Cannot read cart", e); resp.sendError(503, "Không thể đọc giỏ hàng. Vui lòng thử lại."); }
        }
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (req.getSession()) {
            try {
                User_24133049 user = CommerceRequest_24133049.requireUser(req, resp); if (user == null) return;
                if (!CommerceRequest_24133049.csrf(req, resp)) return;
                Cart_24133049 cart = CommerceRequest_24133049.cart(req.getSession(), user.getId());
                String action = req.getParameter("action");
                if ("clear".equals(action)) {
                    if (!"yes".equals(req.getParameter("confirmed"))) throw new IllegalArgumentException("Vui lòng xác nhận xóa toàn bộ giỏ.");
                    cart.clear();
                } else {
                    int id = CommerceRequest_24133049.bookId(req.getParameter("bookId"));
                    if ("add".equals(action)) service.add(cart, id, req.getParameter("quantity"));
                    else if ("update".equals(action)) service.update(cart, id, req.getParameter("quantity"));
                    else if ("remove".equals(action)) cart.remove(id);
                    else throw new IllegalArgumentException("Thao tác giỏ hàng không hợp lệ.");
                }
                req.getSession().setAttribute("cartMessage", "Đã cập nhật giỏ hàng.");
            } catch (IllegalArgumentException e) { req.getSession().setAttribute("cartMessage", e.getMessage()); }
            catch (SQLException e) { log("Cannot update cart", e); req.getSession().setAttribute("cartMessage", "Lỗi kết nối dữ liệu. Giỏ hàng được giữ nguyên."); }
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }
}
