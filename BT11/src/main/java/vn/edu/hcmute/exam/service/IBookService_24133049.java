package vn.edu.hcmute.exam.service;

import vn.edu.hcmute.exam.model.Book_24133049;

import java.util.List;

public interface IBookService_24133049 {
    List<Book_24133049> getAllBooks(int page, int pageSize);
    int countAllBooks();
    int getTotalPagesAll(int pageSize);

    List<Book_24133049> getBooksByAuthor(int authorId, int page, int pageSize);
    int countBooksByAuthor(int authorId);
    int getTotalPagesByAuthor(int authorId, int pageSize);

    Book_24133049 getBookById(int id);
    boolean addBook(Book_24133049 book, int authorId);
    boolean updateBook(Book_24133049 book, int authorId);
    boolean deleteBook(int id);
}
