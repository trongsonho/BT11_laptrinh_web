package vn.edu.hcmute.exam.model;

import java.math.BigDecimal;

public class OrderItem_24133049 {
    private final int bookId, quantity;
    private final String title;
    private final BigDecimal unitPrice, subtotal;
    public OrderItem_24133049(int bookId, String title, BigDecimal unitPrice, int quantity, BigDecimal subtotal) {
        this.bookId = bookId; this.title = title; this.unitPrice = unitPrice; this.quantity = quantity; this.subtotal = subtotal;
    }
    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public BigDecimal getSubtotal() { return subtotal; }
}
