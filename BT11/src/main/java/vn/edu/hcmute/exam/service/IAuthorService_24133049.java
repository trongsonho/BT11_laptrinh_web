package vn.edu.hcmute.exam.service;

import vn.edu.hcmute.exam.model.Author_24133049;

import java.util.List;

public interface IAuthorService_24133049 {
    List<Author_24133049> getAllAuthors();
    Author_24133049 getAuthorById(int id);
    Author_24133049 getAuthorByName(String name);
    List<Author_24133049> getAuthorsByBookId(int bookId);
    int addAuthor(Author_24133049 author);
    int getOrCreateAuthor(String authorName);
}
