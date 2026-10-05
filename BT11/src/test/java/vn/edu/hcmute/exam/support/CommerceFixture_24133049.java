package vn.edu.hcmute.exam.support;

import vn.edu.hcmute.exam.config.DBContext_24133049;
import vn.edu.hcmute.exam.util.PasswordUtil_24133049;
import java.sql.*;
import java.util.*;

/** Creates isolated records and removes only their recorded IDs; never touches sample data. */
public class CommerceFixture_24133049 implements AutoCloseable {
    public final List<Integer> userIds = new ArrayList<>(), bookIds = new ArrayList<>();
    public final String prefix = "bt11-" + UUID.randomUUID().toString().substring(0, 12);
    public String email(int index) { return prefix + "-" + index + "@test.invalid"; }
    public void create() throws SQLException {
        for (int i = 0; i < 3; i++) {
            userIds.add(insert("INSERT INTO dbo.users(email,fullname,phone,passwd,is_active,is_admin) VALUES(?,?,?,?,1,?)",
                email(i), "User BT11 " + i, "0901234567", PasswordUtil_24133049.hashPassword("BT11-test-123!"), i == 2));
        }
        for (int i = 0; i < 2; i++) {
            int id = insert("INSERT INTO dbo.books(isbn,title,price,quantity,cover_image) VALUES(?,?,?,10,?)",
                prefix + i, "Sách BT11 " + i, new java.math.BigDecimal(i == 0 ? "12.35" : "0.10"), "https://example.invalid/cover.png");
            bookIds.add(id);
            execute("INSERT INTO dbo.book_author(bookid,author_id) SELECT ?,MIN(author_id) FROM dbo.author HAVING COUNT(*)>0", id);
        }
    }
    private int insert(String sql, Object... parameters) throws SQLException {
        try (Connection c = DBContext_24133049.getConnection(); PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(p, parameters); p.executeUpdate();
            try (ResultSet rs = p.getGeneratedKeys()) { if (!rs.next()) throw new SQLException("Missing fixture key"); return rs.getInt(1); }
        }
    }
    private static void bind(PreparedStatement p, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) p.setObject(i + 1, parameters[i]);
    }
    public void execute(String sql, Object... parameters) throws SQLException {
        try (Connection c = DBContext_24133049.getConnection(); PreparedStatement p = c.prepareStatement(sql)) { bind(p, parameters); p.executeUpdate(); }
    }
    public long scalar(String sql, Object... parameters) throws SQLException {
        try (Connection c = DBContext_24133049.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, parameters); try (ResultSet rs = p.executeQuery()) { if (!rs.next()) throw new SQLException("No scalar row"); return rs.getLong(1); }
        }
    }
    @Override public void close() throws SQLException {
        try (Connection c = DBContext_24133049.getConnection()) {
            c.setAutoCommit(false);
            try {
                for (int userId : userIds) {
                    delete(c, "DELETE FROM dbo.order_items WHERE order_id IN (SELECT order_id FROM dbo.orders WHERE user_id=?)", userId);
                    delete(c, "DELETE FROM dbo.orders WHERE user_id=?", userId);
                    delete(c, "DELETE FROM dbo.rating WHERE userid=?", userId);
                }
                for (int bookId : bookIds) {
                    delete(c, "DELETE FROM dbo.rating WHERE bookid=?", bookId);
                    delete(c, "DELETE FROM dbo.book_author WHERE bookid=?", bookId);
                    delete(c, "DELETE FROM dbo.books WHERE bookid=?", bookId);
                }
                for (int userId : userIds) delete(c, "DELETE FROM dbo.users WHERE id=?", userId);
                c.commit();
            } catch (SQLException e) { c.rollback(); throw e; }
        }
    }
    private void delete(Connection c, String sql, int id) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(sql)) { p.setInt(1,id); p.executeUpdate(); }
    }
}
