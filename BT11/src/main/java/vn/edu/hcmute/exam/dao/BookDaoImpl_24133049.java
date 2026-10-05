package vn.edu.hcmute.exam.dao;

import vn.edu.hcmute.exam.config.DBContext_24133049;
import vn.edu.hcmute.exam.model.Author_24133049;
import vn.edu.hcmute.exam.model.Book_24133049;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl_24133049 implements IBookDao_24133049 {

    private final IAuthorDao_24133049 authorDao = new AuthorDaoImpl_24133049();

    @Override
    public List<Book_24133049> findAll(int page, int pageSize) {
        List<Book_24133049> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT b.bookid, b.isbn, b.title, b.publisher, b.price, b.description, " +
                     "b.publish_date, b.cover_image, b.quantity, " +
                     "a.author_id, a.author_name, " +
                     "(SELECT COUNT(*) FROM dbo.rating r WHERE r.bookid = b.bookid) AS review_count " +
                     "FROM dbo.books b " +
                     "LEFT JOIN dbo.book_author ba ON b.bookid = ba.bookid " +
                     "LEFT JOIN dbo.author a ON ba.author_id = a.author_id " +
                     "ORDER BY b.bookid DESC " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Book_24133049 b = mapBook(rs);
                    String aName = rs.getString("author_name");
                    int aId = rs.getInt("author_id");
                    if (aName != null) {
                        b.setAuthorNames(aName);
                        b.setPrimaryAuthorId(aId);
                    }
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM dbo.books";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Book_24133049> findByAuthorId(int authorId, int page, int pageSize) {
        List<Book_24133049> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT b.bookid, b.isbn, b.title, b.publisher, b.price, b.description, " +
                     "b.publish_date, b.cover_image, b.quantity, " +
                     "a.author_id, a.author_name, " +
                     "(SELECT COUNT(*) FROM dbo.rating r WHERE r.bookid = b.bookid) AS review_count " +
                     "FROM dbo.books b " +
                     "JOIN dbo.book_author ba ON b.bookid = ba.bookid " +
                     "JOIN dbo.author a ON ba.author_id = a.author_id " +
                     "WHERE ba.author_id = ? " +
                     "ORDER BY b.bookid ASC " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, authorId);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Book_24133049 b = mapBook(rs);
                    b.setAuthorNames(rs.getString("author_name"));
                    b.setPrimaryAuthorId(rs.getInt("author_id"));
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countByAuthorId(int authorId) {
        String sql = "SELECT COUNT(*) FROM dbo.book_author WHERE author_id = ?";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, authorId);
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

    @Override
    public Book_24133049 findById(int id) {
        String sql = "SELECT b.bookid, b.isbn, b.title, b.publisher, b.price, b.description, " +
                     "b.publish_date, b.cover_image, b.quantity, " +
                     "a.author_id, a.author_name, " +
                     "(SELECT COUNT(*) FROM dbo.rating r WHERE r.bookid = b.bookid) AS review_count, " +
                     "(SELECT AVG(CAST(r.rating AS FLOAT)) FROM dbo.rating r WHERE r.bookid = b.bookid) AS avg_rating " +
                     "FROM dbo.books b " +
                     "LEFT JOIN dbo.book_author ba ON b.bookid = ba.bookid " +
                     "LEFT JOIN dbo.author a ON ba.author_id = a.author_id " +
                     "WHERE b.bookid = ?";

        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Book_24133049 b = mapBook(rs);
                    double avg = rs.getDouble("avg_rating");
                    b.setAverageRating(rs.wasNull() ? 0.0 : avg);
                    b.setAuthorNames(rs.getString("author_name"));
                    b.setPrimaryAuthorId(rs.getInt("author_id"));
                    return b;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(Book_24133049 book, int authorId) {
        String sqlBook = "INSERT INTO dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlBookAuthor = "INSERT INTO dbo.book_author (bookid, author_id) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DBContext_24133049.getConnection();
            conn.setAutoCommit(false);

            int newBookId = -1;
            try (PreparedStatement ps = conn.prepareStatement(sqlBook, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, book.getIsbn());
                ps.setString(2, book.getTitle());
                ps.setString(3, book.getPublisher());
                ps.setBigDecimal(4, book.getPrice());
                ps.setString(5, book.getDescription());
                ps.setDate(6, book.getPublishDate());
                ps.setString(7, book.getCoverImage());
                ps.setInt(8, book.getQuantity());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        newBookId = rs.getInt(1);
                    }
                }
            }

            if (newBookId > 0 && authorId > 0) {
                try (PreparedStatement psBA = conn.prepareStatement(sqlBookAuthor)) {
                    psBA.setInt(1, newBookId);
                    psBA.setInt(2, authorId);
                    psBA.executeUpdate();
                }
            }

            conn.commit();
            return newBookId;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            e.printStackTrace();
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public boolean update(Book_24133049 book, int authorId) {
        String sqlBook = "UPDATE dbo.books SET isbn = ?, title = ?, publisher = ?, price = ?, " +
                         "description = ?, publish_date = ?, cover_image = ?, quantity = ? " +
                         "WHERE bookid = ?";
        String sqlDelBA = "DELETE FROM dbo.book_author WHERE bookid = ?";
        String sqlInsBA = "INSERT INTO dbo.book_author (bookid, author_id) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DBContext_24133049.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlBook)) {
                ps.setString(1, book.getIsbn());
                ps.setString(2, book.getTitle());
                ps.setString(3, book.getPublisher());
                ps.setBigDecimal(4, book.getPrice());
                ps.setString(5, book.getDescription());
                ps.setDate(6, book.getPublishDate());
                ps.setString(7, book.getCoverImage());
                ps.setInt(8, book.getQuantity());
                ps.setInt(9, book.getBookId());
                ps.executeUpdate();
            }

            if (authorId > 0) {
                try (PreparedStatement psDel = conn.prepareStatement(sqlDelBA)) {
                    psDel.setInt(1, book.getBookId());
                    psDel.executeUpdate();
                }
                try (PreparedStatement psIns = conn.prepareStatement(sqlInsBA)) {
                    psIns.setInt(1, book.getBookId());
                    psIns.setInt(2, authorId);
                    psIns.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public boolean delete(int id) {
        String sqlDelRating = "DELETE FROM dbo.rating WHERE bookid = ?";
        String sqlDelBA = "DELETE FROM dbo.book_author WHERE bookid = ?";
        String sqlDelBook = "DELETE FROM dbo.books WHERE bookid = ?";

        Connection conn = null;
        try {
            conn = DBContext_24133049.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlDelRating)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlDelBA)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlDelBook)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            if (e.getErrorCode() == 547 && e.getMessage().contains("FK_order_items_book_24133049")) {
                throw new IllegalStateException("Sách đã có trong đơn hàng, không thể xóa để bảo toàn lịch sử. Bạn có thể cập nhật tồn kho về 0.", e);
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private Book_24133049 mapBook(ResultSet rs) throws SQLException {
        Book_24133049 b = new Book_24133049();
        b.setBookId(rs.getInt("bookid"));
        b.setIsbn(rs.getString("isbn"));
        b.setTitle(rs.getString("title"));
        b.setPublisher(rs.getString("publisher"));
        b.setPrice(rs.getBigDecimal("price"));
        b.setDescription(rs.getString("description"));
        b.setPublishDate(rs.getDate("publish_date"));
        b.setCoverImage(rs.getString("cover_image"));
        b.setQuantity(rs.getInt("quantity"));
        b.setReviewCount(rs.getInt("review_count"));
        return b;
    }
}
