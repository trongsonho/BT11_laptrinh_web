package vn.edu.hcmute.exam.service;

import org.junit.jupiter.api.Test;
import vn.edu.hcmute.exam.dao.OrderDao_24133049;
import vn.edu.hcmute.exam.model.*;
import vn.edu.hcmute.exam.util.CommerceRequest_24133049;
import java.sql.SQLException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest_24133049 {
    @Test void validatesShippingIncludingBoundaries() {
        new Shipping_24133049("Tên", "0901234567", "Địa chỉ", "").validate();
        new Shipping_24133049("A".repeat(100), "+84901234567", "B".repeat(500), "C".repeat(1000)).validate();
        for (Shipping_24133049 s : new Shipping_24133049[]{
            new Shipping_24133049("", "0901234567", "Địa chỉ", ""),
            new Shipping_24133049("A".repeat(101), "0901234567", "Địa chỉ", ""),
            new Shipping_24133049("Tên", "123456", "Địa chỉ", ""),
            new Shipping_24133049("Tên", "09abcdefgh", "Địa chỉ", ""),
            new Shipping_24133049("Tên", "0901234567", "", ""),
            new Shipping_24133049("Tên", "0901234567", "A".repeat(501), ""),
            new Shipping_24133049("Tên", "0901234567", "Địa chỉ", "N".repeat(1001))})
            assertThrows(IllegalArgumentException.class, s::validate);
    }
    @Test void failedTransactionKeepsCartAndRetryCreatesOnlyOneOrder() throws Exception {
        int[] calls = {0};
        OrderService_24133049 service = new OrderService_24133049(new OrderDao_24133049() {
            @Override public long createCodOrder(int userId, Map<Integer,Integer> quantities, Shipping_24133049 shipping, String token) throws SQLException {
                if (++calls[0] == 1) throw new SQLException("Simulated transaction failure"); return 42;
            }
        });
        Cart_24133049 cart = new Cart_24133049(7); cart.setQuantity(1, 2);
        String token = CommerceRequest_24133049.token(); cart.setCheckoutToken(token);
        Shipping_24133049 shipping = new Shipping_24133049("Tên", "0901234567", "Địa chỉ", "");
        assertThrows(SQLException.class, () -> service.checkout(cart, shipping, token));
        assertEquals(2, cart.quantity(1)); assertEquals(token, cart.getCheckoutToken());
        assertEquals(42, service.checkout(cart, shipping, token)); assertTrue(cart.isEmpty());
        assertEquals(42, service.checkout(cart, shipping, token)); assertEquals(2, calls[0]);
    }
    @Test void cartChangeInvalidatesCheckoutAndForgedTokenCannotOrder() {
        Cart_24133049 cart = new Cart_24133049(1); cart.setQuantity(1, 1);
        String token = CommerceRequest_24133049.token(); cart.setCheckoutToken(token); cart.setQuantity(1, 2);
        OrderService_24133049 service = new OrderService_24133049();
        Shipping_24133049 shipping = new Shipping_24133049("Tên", "0901234567", "Địa chỉ", "");
        assertThrows(IllegalArgumentException.class, () -> service.checkout(cart, shipping, token));
        assertThrows(IllegalArgumentException.class, () -> service.checkout(cart, shipping, "forged"));
        assertEquals(2, cart.quantity(1));
        assertFalse(CommerceRequest_24133049.sameToken(null, null));
    }
}
