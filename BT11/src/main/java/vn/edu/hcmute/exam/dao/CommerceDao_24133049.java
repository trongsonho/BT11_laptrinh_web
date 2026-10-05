package vn.edu.hcmute.exam.dao;

import vn.edu.hcmute.exam.config.DBContext_24133049;
import vn.edu.hcmute.exam.model.Book_24133049;
import vn.edu.hcmute.exam.model.User_24133049;
import java.sql.*;

/** Unlike legacy read DAOs, propagates database failures instead of reporting missing data. */
public class CommerceDao_24133049 {
    public User_24133049 findUser(int id) throws SQLException {
        try (Connection c = DBContext_24133049.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id,fullname,phone,is_admin,is_active FROM dbo.users WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                User_24133049 u = new User_24133049(); u.setId(id);
                u.setFullname(rs.getString("fullname")); u.setPhone(rs.getString("phone"));
                u.setAdmin(rs.getBoolean("is_admin")); u.setActive(rs.getBoolean("is_active")); return u;
            }
        }
    }
    public Book_24133049 findBook(int id) throws SQLException {
        try (Connection c = DBContext_24133049.getConnection()) { return findBook(c, id, false); }
    }
    public Book_24133049 findBook(Connection c, int id, boolean lock) throws SQLException {
        String sql = "SELECT bookid,title,price,cover_image,quantity FROM dbo.books "
                + (lock ? "WITH (UPDLOCK,HOLDLOCK) " : "") + "WHERE bookid=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Book_24133049 b = new Book_24133049(); b.setBookId(id);
                b.setTitle(rs.getString("title")); b.setPrice(rs.getBigDecimal("price"));
                b.setCoverImage(rs.getString("cover_image")); b.setQuantity(rs.getInt("quantity")); return b;
            }
        }
    }
}
