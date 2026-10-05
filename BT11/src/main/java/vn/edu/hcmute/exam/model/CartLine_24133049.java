package vn.edu.hcmute.exam.model;

import java.math.BigDecimal;

public class CartLine_24133049 {
    private final int bookId;
    private final Book_24133049 book;
    private final int quantity;
    private final String error;
    public CartLine_24133049(int bookId, Book_24133049 book, int quantity, String error) {
        this.bookId = bookId; this.book = book; this.quantity = quantity; this.error = error;
    }
    public int getBookId() { return bookId; }
    public Book_24133049 getBook() { return book; }
    public int getQuantity() { return quantity; }
    public String getError() { return error; }
    public BigDecimal getSubtotal() {
        return book == null || book.getPrice() == null ? BigDecimal.ZERO : book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
