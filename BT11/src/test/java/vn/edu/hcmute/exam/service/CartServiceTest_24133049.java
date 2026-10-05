package vn.edu.hcmute.exam.service;

import org.junit.jupiter.api.Test;
import vn.edu.hcmute.exam.dao.CommerceDao_24133049;
import vn.edu.hcmute.exam.model.*;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest_24133049 {
    private Book_24133049 book(int id, int stock, String price) {
        Book_24133049 book = new Book_24133049(); book.setBookId(id); book.setTitle("Sách " + id);
        book.setQuantity(stock); book.setPrice(new BigDecimal(price)); return book;
    }
    @Test void rejectsMalformedAndOverflowQuantities() {
        for (String input : Arrays.asList(null, "", "0", "-1", "1.5", "abc", "2147483648", "999999999999999999999", " 1", "+1"))
            assertThrows(IllegalArgumentException.class, () -> CartService_24133049.parseQuantity(input), "input=" + input);
        assertEquals(1, CartService_24133049.parseQuantity("1"));
        assertEquals(Integer.MAX_VALUE, CartService_24133049.parseQuantity("2147483647"));
    }
    @Test void accumulatesAndPreservesCartWhenStockDropsOrAddOverflows() throws Exception {
        Book_24133049 book = book(1, 5, "12.35");
        CartService_24133049 service = new CartService_24133049(new CommerceDao_24133049() {
            @Override public Book_24133049 findBook(int id) { return id == 1 ? book : null; }
        });
        Cart_24133049 cart = new Cart_24133049(8);
        service.add(cart, 1, "2"); service.add(cart, 1, "3");
        assertEquals(5, cart.quantity(1)); assertEquals(1, cart.snapshot().size());
        assertThrows(IllegalArgumentException.class, () -> service.add(cart, 1, "1"));
        assertThrows(IllegalArgumentException.class, () -> service.add(cart, 1, "2147483647"));
        book.setQuantity(2);
        assertNotNull(service.lines(cart).get(0).getError()); assertEquals(5, cart.quantity(1));
        assertThrows(IllegalArgumentException.class, () -> service.update(cart, 1, "3"));
        assertEquals(5, cart.quantity(1)); service.update(cart, 1, "2"); assertEquals(2, cart.quantity(1));
        book.setQuantity(0); assertThrows(IllegalArgumentException.class, () -> service.add(cart, 1, "1"));
        assertThrows(IllegalArgumentException.class, () -> service.add(cart, 9, "1"));
    }
    @Test void computesExactMoneyAndSupportsMultipleLinesRemovalAndClear() throws Exception {
        Map<Integer, Book_24133049> books = Map.of(1, book(1, 20, "0.10"), 2, book(2, 10, "19.95"));
        CartService_24133049 service = new CartService_24133049(new CommerceDao_24133049() {
            @Override public Book_24133049 findBook(int id) { return books.get(id); }
        });
        Cart_24133049 cart = new Cart_24133049(1);
        service.add(cart, 1, "3"); service.add(cart, 2, "2");
        assertEquals(new BigDecimal("40.20"), CartService_24133049.total(service.lines(cart)));
        assertEquals(5L, cart.getTotalQuantity());
        cart.remove(1); assertEquals(new BigDecimal("39.90"), CartService_24133049.total(service.lines(cart)));
        cart.clear(); assertTrue(cart.isEmpty());
    }
    @Test void missingAndUnpricedBooksRemainVisibleAsInvalidLines() throws Exception {
        Cart_24133049 cart = new Cart_24133049(1); cart.setQuantity(9, 2);
        CartService_24133049 service = new CartService_24133049(new CommerceDao_24133049() {
            @Override public Book_24133049 findBook(int id) { return null; }
        });
        assertNotNull(service.lines(cart).get(0).getError()); assertEquals(2, cart.quantity(9));
        Book_24133049 book = book(1, 10, "1"); book.setPrice(null);
        assertThrows(IllegalArgumentException.class, () -> CartService_24133049.validateStock(book, 1));
    }
}
