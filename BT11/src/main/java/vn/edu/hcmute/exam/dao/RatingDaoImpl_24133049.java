package vn.edu.hcmute.exam.dao;

import vn.edu.hcmute.exam.config.DBContext_24133049;
import vn.edu.hcmute.exam.model.Rating_24133049;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDaoImpl_24133049 implements IRatingDao_24133049 {

    @Override
    public List<Rating_24133049> findByBookId(int bookId) {
        List<Rating_24133049> list = new ArrayList<>();
        String sql = "SELECT r.userid, r.bookid, r.rating, r.review_text, r.review_date, " +
                     "u.fullname AS user_name, u.email AS user_email, b.title AS book_title " +
                     "FROM dbo.rating r " +
                     "JOIN dbo.users u ON r.userid = u.id " +
                     "JOIN dbo.books b ON r.bookid = b.bookid " +
                     "WHERE r.bookid = ? " +
                     "ORDER BY r.review_date DESC";

        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rating_24133049 r = new Rating_24133049();
                    r.setUserId(rs.getInt("userid"));
                    r.setBookId(rs.getInt("bookid"));
                    r.setRating(rs.getInt("rating"));
                    r.setReviewText(rs.getString("review_text"));
                    r.setReviewDate(rs.getTimestamp("review_date"));
                    r.setUserName(rs.getString("user_name"));
                    r.setUserEmail(rs.getString("user_email"));
                    r.setBookTitle(rs.getString("book_title"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Rating_24133049 findByUserAndBook(int userId, int bookId) {
        String sql = "SELECT r.userid, r.bookid, r.rating, r.review_text, r.review_date, " +
                     "u.fullname AS user_name, u.email AS user_email, b.title AS book_title " +
                     "FROM dbo.rating r " +
                     "JOIN dbo.users u ON r.userid = u.id " +
                     "JOIN dbo.books b ON r.bookid = b.bookid " +
                     "WHERE r.userid = ? AND r.bookid = ?";

        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Rating_24133049 r = new Rating_24133049();
                    r.setUserId(rs.getInt("userid"));
                    r.setBookId(rs.getInt("bookid"));
                    r.setRating(rs.getInt("rating"));
                    r.setReviewText(rs.getString("review_text"));
                    r.setReviewDate(rs.getTimestamp("review_date"));
                    r.setUserName(rs.getString("user_name"));
                    r.setUserEmail(rs.getString("user_email"));
                    r.setBookTitle(rs.getString("book_title"));
                    return r;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertOrUpdate(Rating_24133049 rating) {
        Rating_24133049 existing = findByUserAndBook(rating.getUserId(), rating.getBookId());
        if (existing != null) {
            // Đã tồn tại review -> cập nhật điểm và nhận xét
            String sqlUpdate = "UPDATE dbo.rating SET rating = ?, review_text = ?, review_date = GETDATE() " +
                               "WHERE userid = ? AND bookid = ?";
            try (Connection conn = DBContext_24133049.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setInt(1, rating.getRating());
                ps.setString(2, rating.getReviewText());
                ps.setInt(3, rating.getUserId());
                ps.setInt(4, rating.getBookId());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // Thêm mới
            String sqlInsert = "INSERT INTO dbo.rating (userid, bookid, rating, review_text, review_date) VALUES (?, ?, ?, ?, GETDATE())";
            try (Connection conn = DBContext_24133049.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, rating.getUserId());
                ps.setInt(2, rating.getBookId());
                ps.setInt(3, rating.getRating());
                ps.setString(4, rating.getReviewText());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public int countByBookId(int bookId) {
        String sql = "SELECT COUNT(*) FROM dbo.rating WHERE bookid = ?";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
