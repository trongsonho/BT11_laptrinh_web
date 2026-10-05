package vn.edu.hcmute.exam.dao;

import vn.edu.hcmute.exam.model.Book_24133049;

import java.util.List;

public interface IBookDao_24133049 {
    List<Book_24133049> findAll(int page, int pageSize);
    int countAll();
    List<Book_24133049> findByAuthorId(int authorId, int page, int pageSize);
    int countByAuthorId(int authorId);
    Book_24133049 findById(int id);
    int insert(Book_24133049 book, int authorId);
    boolean update(Book_24133049 book, int authorId);
    boolean delete(int id);
}
