package vn.edu.hcmute.exam.dao;

import vn.edu.hcmute.exam.config.DBContext_24133049;
import vn.edu.hcmute.exam.model.*;
import vn.edu.hcmute.exam.service.CartService_24133049;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class OrderDao_24133049 {
    @FunctionalInterface public interface ConnectionFactory_24133049 { Connection open() throws SQLException; }
    private final ConnectionFactory_24133049 connections;
    private final CommerceDao_24133049 books = new CommerceDao_24133049();
    public OrderDao_24133049() { this(DBContext_24133049::getConnection); }
    public OrderDao_24133049(ConnectionFactory_24133049 connections) { this.connections = connections; }

    /** One connection, deterministic book locks, and a persistent idempotency key. */
    public long createCodOrder(int userId, Map<Integer, Integer> quantities, Shipping_24133049 shipping, String token) throws SQLException {
        shipping.validate();
        UUID.fromString(token);
        try (Connection c = connections.open()) {
            c.setAutoCommit(false);
            try {
                requireActiveUser(c, userId);
                // The unique index also protects replay from another HTTP session/process.
                try (PreparedStatement ps = c.prepareStatement("SELECT order_id FROM dbo.orders WITH (UPDLOCK,HOLDLOCK,INDEX(UQ_orders_checkout_24133049)) WHERE user_id=? AND checkout_token=?")) {
                    ps.setInt(1, userId); ps.setString(2, token);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) { long id = rs.getLong(1); c.commit(); return id; }
                    }
                }
                if (quantities.isEmpty()) throw new IllegalArgumentException("Giỏ hàng trống. Vui lòng thêm sách trước khi đặt hàng.");
                List<OrderItem_24133049> items = new ArrayList<>();
                BigDecimal total = BigDecimal.ZERO;
                for (var entry : new TreeMap<>(quantities).entrySet()) {
                    Book_24133049 b = books.findBook(c, entry.getKey(), true);
                    CartService_24133049.validateStock(b, entry.getValue());
                    BigDecimal lineTotal = b.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                    total = total.add(lineTotal);
                    items.add(new OrderItem_24133049(b.getBookId(), b.getTitle(), b.getPrice(), entry.getValue(), lineTotal));
                }
                long orderId;
                String insert = "INSERT INTO dbo.orders(order_code,user_id,recipient_name,phone,shipping_address,note,total_amount,payment_method,order_status,payment_status,checkout_token) VALUES(?,?,?,?,?,?,?,'COD','NEW','UNPAID',?)";
                try (PreparedStatement ps = c.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, "COD-" + token); ps.setInt(2, userId);
                    ps.setString(3, shipping.getRecipientName()); ps.setString(4, shipping.getPhone());
                    ps.setString(5, shipping.getAddress()); ps.setString(6, shipping.getNote());
                    ps.setBigDecimal(7, total); ps.setString(8, token);
                    if (ps.executeUpdate() != 1) throw new SQLException("Order insert affected unexpected rows");
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) throw new SQLException("Missing order generated key"); orderId = rs.getLong(1);
                    }
                }
                for (OrderItem_24133049 item : items) {
                    try (PreparedStatement ps = c.prepareStatement("INSERT INTO dbo.order_items(order_id,book_id,book_title,unit_price,quantity,line_total) VALUES(?,?,?,?,?,?)")) {
                        ps.setLong(1, orderId); ps.setInt(2, item.getBookId()); ps.setString(3, item.getTitle());
                        ps.setBigDecimal(4, item.getUnitPrice()); ps.setInt(5, item.getQuantity()); ps.setBigDecimal(6, item.getSubtotal());
                        if (ps.executeUpdate() != 1) throw new SQLException("Order detail insert affected unexpected rows");
                    }
                    try (PreparedStatement ps = c.prepareStatement("UPDATE dbo.books SET quantity=quantity-? WHERE bookid=? AND quantity>=?")) {
                        ps.setInt(1, item.getQuantity()); ps.setInt(2, item.getBookId()); ps.setInt(3, item.getQuantity());
                        if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Tồn kho vừa thay đổi, vui lòng kiểm tra lại giỏ.");
                    }
                }
                c.commit(); return orderId;
            } catch (SQLException | RuntimeException e) {
                try { c.rollback(); } catch (SQLException rollback) { e.addSuppressed(rollback); }
                throw e;
            }
        }
    }
    private void requireActiveUser(Connection c, int userId) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT is_admin,is_active FROM dbo.users WITH (HOLDLOCK) WHERE id=?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || rs.getBoolean("is_admin") || !rs.getBoolean("is_active"))
                    throw new IllegalArgumentException("Tài khoản không còn quyền đặt hàng.");
            }
        }
    }
    public Order_24133049 findOwnedOrder(long id, int userId) throws SQLException {
        try (Connection c = connections.open();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM dbo.orders WHERE order_id=? AND user_id=?")) {
            ps.setLong(1, id); ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                List<OrderItem_24133049> items = new ArrayList<>();
                try (PreparedStatement detail = c.prepareStatement("SELECT book_id,book_title,unit_price,quantity,line_total FROM dbo.order_items WHERE order_id=? ORDER BY book_id")) {
                    detail.setLong(1, id);
                    try (ResultSet dr = detail.executeQuery()) {
                        while (dr.next()) items.add(new OrderItem_24133049(dr.getInt(1), dr.getString(2), dr.getBigDecimal(3), dr.getInt(4), dr.getBigDecimal(5)));
                    }
                }
                return mapOrder(rs, items);
            }
        }
    }

    public long countOwnedOrders(int userId, OrderStatus_24133049 status) throws SQLException {
        String sql = "SELECT COUNT_BIG(*) FROM dbo.orders WHERE user_id=?" + statusClause(status);
        try (Connection c = connections.open(); PreparedStatement ps = c.prepareStatement(sql)) {
            bindOwnerAndStatus(ps, userId, status);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getLong(1); }
        }
    }
    public List<Order_24133049> findOwnedOrders(int userId, OrderStatus_24133049 status, long offset, int pageSize) throws SQLException {
        if (offset < 0 || pageSize < 1 || pageSize > 100) throw new IllegalArgumentException("Invalid pagination");
        String sql = "SELECT * FROM dbo.orders WHERE user_id=?" + statusClause(status)
            + " ORDER BY created_at DESC,order_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection c = connections.open(); PreparedStatement ps = c.prepareStatement(sql)) {
            int next = bindOwnerAndStatus(ps, userId, status);
            ps.setLong(next++, offset); ps.setInt(next, pageSize);
            List<Order_24133049> result = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(mapOrder(rs, List.of()));
            }
            return result;
        }
    }
    private String statusClause(OrderStatus_24133049 status) {
        return status == null ? "" : " AND order_status IN (" + String.join(",", Collections.nCopies(status.getDatabaseCodes().size(), "?")) + ")";
    }
    private int bindOwnerAndStatus(PreparedStatement ps, int userId, OrderStatus_24133049 status) throws SQLException {
        ps.setInt(1, userId); int next = 2;
        if (status != null) for (String code : status.getDatabaseCodes()) ps.setString(next++, code);
        return next;
    }
    private Order_24133049 mapOrder(ResultSet rs, List<OrderItem_24133049> items) throws SQLException {
        return new Order_24133049(rs.getLong("order_id"), rs.getString("order_code"),
            new Shipping_24133049(rs.getString("recipient_name"), rs.getString("phone"), rs.getString("shipping_address"), rs.getString("note")),
            rs.getTimestamp("created_at"), rs.getBigDecimal("total_amount"), rs.getString("payment_method"),
            rs.getString("order_status"), rs.getString("payment_status"), items);
    }
}
