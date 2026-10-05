package vn.edu.hcmute.exam.dao;

import vn.edu.hcmute.exam.config.DBContext_24133049;
import vn.edu.hcmute.exam.model.Author_24133049;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDaoImpl_24133049 implements IAuthorDao_24133049 {

    @Override
    public List<Author_24133049> findAll() {
        List<Author_24133049> list = new ArrayList<>();
        String sql = "SELECT author_id, author_name, date_of_birth FROM dbo.author ORDER BY author_id ASC";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Author_24133049 author = new Author_24133049();
                author.setAuthorId(rs.getInt("author_id"));
                author.setAuthorName(rs.getString("author_name"));
                author.setDateOfBirth(rs.getDate("date_of_birth"));
                list.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Author_24133049 findById(int id) {
        String sql = "SELECT author_id, author_name, date_of_birth FROM dbo.author WHERE author_id = ?";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Author_24133049 author = new Author_24133049();
                    author.setAuthorId(rs.getInt("author_id"));
                    author.setAuthorName(rs.getString("author_name"));
                    author.setDateOfBirth(rs.getDate("date_of_birth"));
                    return author;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Author_24133049 findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT author_id, author_name, date_of_birth FROM dbo.author WHERE LOWER(LTRIM(RTRIM(author_name))) = LOWER(LTRIM(RTRIM(?)))";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Author_24133049 author = new Author_24133049();
                    author.setAuthorId(rs.getInt("author_id"));
                    author.setAuthorName(rs.getString("author_name"));
                    author.setDateOfBirth(rs.getDate("date_of_birth"));
                    return author;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Author_24133049> findByBookId(int bookId) {
        List<Author_24133049> list = new ArrayList<>();
        String sql = "SELECT a.author_id, a.author_name, a.date_of_birth " +
                     "FROM dbo.author a " +
                     "JOIN dbo.book_author ba ON a.author_id = ba.author_id " +
                     "WHERE ba.bookid = ? ORDER BY a.author_name ASC";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Author_24133049 author = new Author_24133049();
                    author.setAuthorId(rs.getInt("author_id"));
                    author.setAuthorName(rs.getString("author_name"));
                    author.setDateOfBirth(rs.getDate("date_of_birth"));
                    list.add(author);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int insert(Author_24133049 author) {
        String sql = "INSERT INTO dbo.author (author_name, date_of_birth) VALUES (?, ?)";
        try (Connection conn = DBContext_24133049.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, author.getAuthorName());
            ps.setDate(2, author.getDateOfBirth());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
