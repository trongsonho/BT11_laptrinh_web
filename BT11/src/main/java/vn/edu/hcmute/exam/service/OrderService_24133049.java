package vn.edu.hcmute.exam.service;

import vn.edu.hcmute.exam.dao.OrderDao_24133049;
import vn.edu.hcmute.exam.model.*;
import java.sql.SQLException;

public class OrderService_24133049 {
    private final OrderDao_24133049 dao;
    public OrderService_24133049() { this(new OrderDao_24133049()); }
    public OrderService_24133049(OrderDao_24133049 dao) { this.dao = dao; }
    public long checkout(Cart_24133049 cart, Shipping_24133049 shipping, String token) throws SQLException {
        Long completed = cart.completedOrder(token);
        if (completed != null) return completed;
        if (!vn.edu.hcmute.exam.util.CommerceRequest_24133049.sameToken(cart.getCheckoutToken(), token))
            throw new IllegalArgumentException("Phiên đặt hàng đã thay đổi hoặc hết hạn. Vui lòng kiểm tra lại giỏ hàng.");
        shipping.validate();
        long id = dao.createCodOrder(cart.getUserId(), cart.snapshot(), shipping, token);
        // DAO returns only after commit. Failed transactions leave both cart and token intact.
        cart.complete(token, id); return id;
    }
    public Order_24133049 findOwnedOrder(long id, int userId) throws SQLException { return dao.findOwnedOrder(id, userId); }
    public static long parsePage(String value) {
        if (value == null || value.isEmpty()) return 1;
        try {
            if (!value.matches("[0-9]{1,19}")) throw new NumberFormatException();
            long page = Long.parseLong(value);
            if (page < 1) throw new NumberFormatException();
            return page;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số trang không hợp lệ. Đã chuyển về trang 1.");
        }
    }
    public OrderHistoryPage_24133049 history(int userId, OrderStatus_24133049 status, long requestedPage) throws SQLException {
        long count = dao.countOwnedOrders(userId, status);
        long totalPages = Math.max(1, count / 10 + (count % 10 == 0 ? 0 : 1));
        long page = Math.max(1, Math.min(requestedPage, totalPages));
        return new OrderHistoryPage_24133049(dao.findOwnedOrders(userId, status, (page - 1) * 10, 10), count, page, totalPages);
    }
}
