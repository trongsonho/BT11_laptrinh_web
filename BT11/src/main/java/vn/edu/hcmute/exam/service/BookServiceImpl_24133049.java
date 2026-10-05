package vn.edu.hcmute.exam.service;

import vn.edu.hcmute.exam.dao.BookDaoImpl_24133049;
import vn.edu.hcmute.exam.dao.IBookDao_24133049;
import vn.edu.hcmute.exam.model.Book_24133049;

import java.util.Collections;
import java.util.List;

public class BookServiceImpl_24133049 implements IBookService_24133049 {

    private final IBookDao_24133049 bookDao;

    public BookServiceImpl_24133049() {
        this.bookDao = new BookDaoImpl_24133049();
    }

    public BookServiceImpl_24133049(IBookDao_24133049 bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public List<Book_24133049> getAllBooks(int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        return bookDao.findAll(page, pageSize);
    }

    @Override
    public int countAllBooks() {
        return bookDao.countAll();
    }

    @Override
    public int getTotalPagesAll(int pageSize) {
        int total = countAllBooks();
        if (total == 0) return 1;
        return (int) Math.ceil((double) total / pageSize);
    }

    @Override
    public List<Book_24133049> getBooksByAuthor(int authorId, int page, int pageSize) {
        if (authorId <= 0) {
            return Collections.emptyList();
        }
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 3;
        return bookDao.findByAuthorId(authorId, page, pageSize);
    }

    @Override
    public int countBooksByAuthor(int authorId) {
        if (authorId <= 0) return 0;
        return bookDao.countByAuthorId(authorId);
    }

    @Override
    public int getTotalPagesByAuthor(int authorId, int pageSize) {
        int total = countBooksByAuthor(authorId);
        if (total == 0) return 1;
        return (int) Math.ceil((double) total / pageSize);
    }

    @Override
    public Book_24133049 getBookById(int id) {
        if (id <= 0) return null;
        return bookDao.findById(id);
    }

    @Override
    public boolean addBook(Book_24133049 book, int authorId) {
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return false;
        }
        return bookDao.insert(book, authorId) > 0;
    }

    @Override
    public boolean updateBook(Book_24133049 book, int authorId) {
        if (book == null || book.getBookId() <= 0 || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return false;
        }
        return bookDao.update(book, authorId);
    }

    @Override
    public boolean deleteBook(int id) {
        if (id <= 0) return false;
        return bookDao.delete(id);
    }
}
