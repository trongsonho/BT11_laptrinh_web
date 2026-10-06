package vn.edu.hcmute.exam.service;

import org.junit.jupiter.api.Test;
import vn.edu.hcmute.exam.dao.OrderDao_24133049;
import vn.edu.hcmute.exam.model.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderHistoryServiceTest_24133049 {
    @Test void distinguishesEightStatusesAndPreservesLegacyAliasesWithSafeUnknownLabel() {
        assertEquals(8, OrderStatus_24133049.values().length);
        assertEquals("Đơn hàng mới", OrderStatus_24133049.labelFor("NEW"));
        assertEquals(OrderStatus_24133049.NEW, OrderStatus_24133049.filter("PENDING"));
        assertEquals(OrderStatus_24133049.SHIPPED, OrderStatus_24133049.filter("SHIPPING"));
        assertEquals(OrderStatus_24133049.COMPLETED, OrderStatus_24133049.filter("DELIVERED"));
        assertNotEquals(OrderStatus_24133049.labelFor("SHIPPING"), OrderStatus_24133049.labelFor("DELIVERING"));
        assertEquals("Trạng thái chưa xác định", OrderStatus_24133049.labelFor("<script>"));
        assertEquals("secondary", OrderStatus_24133049.badgeFor(null));
        for (String input : List.of("UNPAID", "PAID", "' OR 1=1--", "new", "<script>"))
            assertThrows(IllegalArgumentException.class, () -> OrderStatus_24133049.filter(input));
    }
    @Test void rejectsMalformedPagesAndAcceptsLongWithoutOverflow() {
        assertEquals(1, OrderService_24133049.parsePage(null));
        assertEquals(Long.MAX_VALUE, OrderService_24133049.parsePage("9223372036854775807"));
        for (String input : List.of("0", "-1", "1.5", "abc", "9223372036854775808", "99999999999999999999999", " 1"))
            assertThrows(IllegalArgumentException.class, () -> OrderService_24133049.parsePage(input));
    }
    @Test void clampsHugePageToLastPageAndKeepsOwnerAndFilterOnBothQueries() throws Exception {
        long[] offset = {-1};
        OrderService_24133049 service = new OrderService_24133049(new OrderDao_24133049() {
            @Override public long countOwnedOrders(int userId, OrderStatus_24133049 status) {
                assertEquals(42, userId); assertEquals(OrderStatus_24133049.DELIVERING, status); return 21;
            }
            @Override public List<Order_24133049> findOwnedOrders(int userId, OrderStatus_24133049 status, long start, int size) {
                assertEquals(42, userId); assertEquals(OrderStatus_24133049.DELIVERING, status); assertEquals(10, size);
                offset[0] = start; return List.of();
            }
        });
        var page = service.history(42, OrderStatus_24133049.DELIVERING, Long.MAX_VALUE);
        assertEquals(3, page.getPage()); assertEquals(21, page.getTotalOrders()); assertEquals(20, offset[0]);
        assertEquals(List.of(1L,2L,3L), page.getPageLinks());
    }
}
