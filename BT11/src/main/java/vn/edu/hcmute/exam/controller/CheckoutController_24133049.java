package vn.edu.hcmute.exam.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.exam.model.*;
import vn.edu.hcmute.exam.service.*;
import vn.edu.hcmute.exam.util.CommerceRequest_24133049;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="CheckoutController_24133049", urlPatterns={"/checkout"})
public class CheckoutController_24133049 extends HttpServlet {
    private final CartService_24133049 carts = new CartService_24133049();
    private final OrderService_24133049 orders = new OrderService_24133049();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (req.getSession()) {
            try {
                User_24133049 user = CommerceRequest_24133049.requireUser(req, resp); if (user == null) return;
                Cart_24133049 cart = CommerceRequest_24133049.cart(req.getSession(), user.getId());
                if (cart.isEmpty()) { req.getSession().setAttribute("cartMessage", "Giỏ hàng trống. Vui lòng thêm sách trước khi đặt hàng."); resp.sendRedirect(req.getContextPath()+"/cart"); return; }
                if (cart.getCheckoutToken() == null) cart.setCheckoutToken(CommerceRequest_24133049.token());
                req.setAttribute("shipping", new Shipping_24133049(user.getFullname(), user.getPhone(), "", ""));
                render(req, resp, cart);
            } catch (SQLException e) { log("Cannot read checkout", e); resp.sendError(503, "Không thể đọc dữ liệu đặt hàng. Vui lòng thử lại."); }
        }
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (req.getSession()) {
            Cart_24133049 cart = null;
            try {
                User_24133049 user = CommerceRequest_24133049.requireUser(req, resp); if (user == null) return;
                if (!CommerceRequest_24133049.csrf(req, resp)) return;
                cart = CommerceRequest_24133049.cart(req.getSession(), user.getId());
                Shipping_24133049 shipping = new Shipping_24133049(req.getParameter("recipientName"), req.getParameter("phone"), req.getParameter("address"), req.getParameter("note"));
                req.setAttribute("shipping", shipping);
                long orderId = orders.checkout(cart, shipping, req.getParameter("checkoutToken"));
                req.getSession().setAttribute("successfulOrderId", orderId);
                resp.sendRedirect(req.getContextPath() + "/order-success?id=" + orderId); return;
            } catch (IllegalArgumentException e) { req.setAttribute("checkoutError", e.getMessage()); }
            catch (SQLException e) { log("COD transaction failed", e); req.setAttribute("checkoutError", "Không thể đặt hàng. Giỏ hàng được giữ nguyên; vui lòng thử lại. Nếu chưa cài đặt, cần chạy migration COD."); }
            if (cart == null) { resp.sendError(503, "Không thể kiểm tra tài khoản. Vui lòng thử lại."); return; }
            try { render(req, resp, cart); }
            catch (SQLException e) {
                log("Cannot reload checkout", e);
                req.setAttribute("cartValid", false); req.setAttribute("checkoutToken", cart.getCheckoutToken());
                resp.setContentType("text/html;charset=UTF-8"); req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").include(req, resp);
            }
        }
    }
    private void render(HttpServletRequest req, HttpServletResponse resp, Cart_24133049 cart) throws SQLException, ServletException, IOException {
        var lines = carts.lines(cart);
        if (!lines.isEmpty() && cart.getCheckoutToken() == null) cart.setCheckoutToken(CommerceRequest_24133049.token());
        req.setAttribute("cartLines", lines); req.setAttribute("cartTotal", CartService_24133049.total(lines));
        req.setAttribute("cartValid", !lines.isEmpty() && lines.stream().allMatch(l -> l.getError() == null));
        req.setAttribute("checkoutToken", cart.getCheckoutToken()); req.setAttribute("cartQuantity", cart.getTotalQuantity());
        resp.setContentType("text/html;charset=UTF-8"); req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").include(req, resp);
    }
}
