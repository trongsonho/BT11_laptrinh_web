package vn.edu.hcmute.exam.service;

import vn.edu.hcmute.exam.dao.AuthorDaoImpl_24133049;
import vn.edu.hcmute.exam.dao.IAuthorDao_24133049;
import vn.edu.hcmute.exam.model.Author_24133049;

import java.util.List;

public class AuthorServiceImpl_24133049 implements IAuthorService_24133049 {

    private final IAuthorDao_24133049 authorDao;

    public AuthorServiceImpl_24133049() {
        this.authorDao = new AuthorDaoImpl_24133049();
    }

    public AuthorServiceImpl_24133049(IAuthorDao_24133049 authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public List<Author_24133049> getAllAuthors() {
        return authorDao.findAll();
    }

    @Override
    public Author_24133049 getAuthorById(int id) {
        return authorDao.findById(id);
    }

    @Override
    public List<Author_24133049> getAuthorsByBookId(int bookId) {
        return authorDao.findByBookId(bookId);
    }

    @Override
    public Author_24133049 getAuthorByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return authorDao.findByName(name.trim());
    }

    @Override
    public int addAuthor(Author_24133049 author) {
        return authorDao.insert(author);
    }

    @Override
    public int getOrCreateAuthor(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            return -1;
        }
        String trimmed = authorName.trim();
        Author_24133049 existing = authorDao.findByName(trimmed);
        if (existing != null) {
            return existing.getAuthorId();
        }
        Author_24133049 newAuthor = new Author_24133049();
        newAuthor.setAuthorName(trimmed);
        return authorDao.insert(newAuthor);
    }
}
