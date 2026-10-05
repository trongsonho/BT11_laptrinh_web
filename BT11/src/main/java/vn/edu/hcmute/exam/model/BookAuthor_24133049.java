package vn.edu.hcmute.exam.model;

import java.io.Serializable;

public class BookAuthor_24133049 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookId;
    private int authorId;

    public BookAuthor_24133049() {
    }

    public BookAuthor_24133049(int bookId, int authorId) {
        this.bookId = bookId;
        this.authorId = authorId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
}
