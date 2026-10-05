package vn.edu.hcmute.exam.dao;

import vn.edu.hcmute.exam.model.Author_24133049;

import java.util.List;

public interface IAuthorDao_24133049 {
    List<Author_24133049> findAll();
    Author_24133049 findById(int id);
    Author_24133049 findByName(String name);
    List<Author_24133049> findByBookId(int bookId);
    int insert(Author_24133049 author);
}
