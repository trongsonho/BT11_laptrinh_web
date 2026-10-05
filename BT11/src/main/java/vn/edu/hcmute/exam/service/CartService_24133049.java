package vn.edu.hcmute.exam.service;

import vn.edu.hcmute.exam.dao.CommerceDao_24133049;
import vn.edu.hcmute.exam.model.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class CartService_24133049 {
    private final CommerceDao_24133049 dao;
    public CartService_24133049() { this(new CommerceDao_24133049()); }
    public CartService_24133049(CommerceDao_24133049 dao) { this.dao = dao; }
    public User_24133049 findUser(int id) throws SQLException { return dao.findUser(id); }
    public static int parseQuantity(String value) {
        if (value == null || !value.matches("[0-9]{1,10}"))
            throw new IllegalArgumentException("Số lượng phải là số nguyên dương, không vượt giới hạn.");
        try {
            int quantity = Integer.parseInt(value);
            if (quantity < 1) throw new NumberFormatException();
            return quantity;
        } catch (NumberFormatException e) { throw new IllegalArgumentException("Số lượng phải từ 1 đến tồn kho hiện tại."); }
    }
    public static void validateStock(Book_24133049 book, long quantity) {
        String error = stockError(book, quantity);
        if (error != null) throw new IllegalArgumentException(error);
    }
    public static String stockError(Book_24133049 book, long quantity) {
        if (book == null) return "Sách không tồn tại hoặc đã bị xóa. Vui lòng xóa sách khỏi giỏ.";
        if (book.getQuantity() < 1) return "Sách đã hết hàng: " + book.getTitle();
        if (quantity < 1 || quantity > Integer.MAX_VALUE || quantity > book.getQuantity())
            return "Số lượng không hợp lệ hoặc tồn kho đã giảm: " + book.getTitle() + " (còn " + book.getQuantity() + ").";
        if (book.getPrice() == null || book.getPrice().signum() < 0) return "Sách chưa có giá hợp lệ: " + book.getTitle();
        return null;
    }
    public void add(Cart_24133049 cart, int id, String value) throws SQLException {
        int quantity = parseQuantity(value);
        long combined = (long) cart.quantity(id) + quantity;
        validateStock(dao.findBook(id), combined);
        cart.setQuantity(id, (int) combined);
    }
    public void update(Cart_24133049 cart, int id, String value) throws SQLException {
        if (cart.quantity(id) == 0) throw new IllegalArgumentException("Sách không có trong giỏ.");
        int quantity = parseQuantity(value);
        validateStock(dao.findBook(id), quantity); cart.setQuantity(id, quantity);
    }
    public List<CartLine_24133049> lines(Cart_24133049 cart) throws SQLException {
        List<CartLine_24133049> lines = new ArrayList<>();
        for (var entry : cart.snapshot().entrySet()) {
            Book_24133049 book = dao.findBook(entry.getKey());
            lines.add(new CartLine_24133049(entry.getKey(), book, entry.getValue(), stockError(book, entry.getValue())));
        }
        return lines;
    }
    public static BigDecimal total(List<CartLine_24133049> lines) {
        return lines.stream().map(CartLine_24133049::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
