package vn.edu.hcmute.exam.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import vn.edu.hcmute.exam.config.DBContext_24133049;
import vn.edu.hcmute.exam.model.*;
import vn.edu.hcmute.exam.service.OrderService_24133049;
import vn.edu.hcmute.exam.support.CommerceFixture_24133049;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;
import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named="RUN_SQLSERVER_TESTS", matches="true")
public class OrderHistoryDatabaseTest_24133049 {
    private CommerceFixture_24133049 fixture;
    private final OrderDao_24133049 dao = new OrderDao_24133049();
    private final OrderService_24133049 service = new OrderService_24133049(dao);
    @BeforeEach void create() throws Exception {
        fixture = new CommerceFixture_24133049(); fixture.create();
        fixture.execute("UPDATE dbo.books SET quantity=100 WHERE bookid=?", fixture.bookIds.get(0));
    }
    @AfterEach void cleanup() throws Exception { if (fixture != null) fixture.close(); }
    private long order(int user, String status) throws Exception {
        long id = dao.createCodOrder(user, Map.of(fixture.bookIds.get(0),1),
            new Shipping_24133049("User demo", "0901234567", "Địa chỉ demo", ""), UUID.randomUUID().toString());
        fixture.execute("UPDATE dbo.orders SET order_status=?,created_at='2026-10-06T10:00:00' WHERE order_id=?",status,id);
        return id;
    }
    @Test void ownerScopedCountAndPaginationAreStableAndEveryFilterIncludesItsAliases() throws Exception {
        int user = fixture.userIds.get(0); List<Long> ids = new ArrayList<>();
        for (int i=0;i<21;i++) ids.add(order(user,"NEW"));
        for (var status : OrderStatus_24133049.values()) if (status != OrderStatus_24133049.NEW) ids.add(order(user,status.getCode()));
        ids.add(order(user,"PENDING")); ids.add(order(user,"SHIPPING")); ids.add(order(user,"DELIVERED"));
        long foreign = order(fixture.userIds.get(1),"NEW");
        assertEquals(ids.size(),dao.countOwnedOrders(user,null));
        Collections.reverse(ids);
        var first = service.history(user,null,1); var second = service.history(user,null,2);
        assertEquals(ids.subList(0,10),first.getOrders().stream().map(Order_24133049::getId).toList());
        assertEquals(ids.subList(10,20),second.getOrders().stream().map(Order_24133049::getId).toList());
        assertEquals(22,dao.countOwnedOrders(user,OrderStatus_24133049.NEW));
        assertEquals(2,dao.countOwnedOrders(user,OrderStatus_24133049.SHIPPED));
        assertEquals(2,dao.countOwnedOrders(user,OrderStatus_24133049.COMPLETED));
        for (var status : OrderStatus_24133049.values()) {
            var page = service.history(user,status,1);
            assertTrue(page.getTotalOrders()>0);
            for (var result : page.getOrders()) assertEquals(status,OrderStatus_24133049.fromCode(result.getStatus()));
        }
        assertNull(dao.findOwnedOrder(foreign,user));
        assertEquals(1,dao.countOwnedOrders(fixture.userIds.get(1),null));
        assertEquals(0,dao.countOwnedOrders(fixture.userIds.get(1),OrderStatus_24133049.DELIVERING));
    }
    @Test void sqlUpdatesImmediatelyMoveBetweenFiltersWithoutChangingStockSnapshotsOrPayment() throws Exception {
        int user=fixture.userIds.get(0), book=fixture.bookIds.get(0); long id=order(user,"NEW");
        var before=dao.findOwnedOrder(id,user); long stock=fixture.scalar("SELECT quantity FROM dbo.books WHERE bookid=?",book);
        fixture.execute("UPDATE dbo.books SET price=99.99,title=N'Tên mới' WHERE bookid=?",book);
        OrderStatus_24133049 previous=OrderStatus_24133049.NEW;
        for (var status : OrderStatus_24133049.values()) {
            fixture.execute("UPDATE dbo.orders SET order_status=? WHERE order_id=?",status.getCode(),id);
            assertEquals(1,dao.countOwnedOrders(user,status));
            if (previous!=status) assertEquals(0,dao.countOwnedOrders(user,previous));
            var after=dao.findOwnedOrder(id,user);
            assertEquals(status.getLabel(),after.getStatusLabel()); assertEquals(before.getTotal(),after.getTotal());
            assertEquals(before.getItems().get(0).getUnitPrice(),after.getItems().get(0).getUnitPrice());
            assertEquals("UNPAID",after.getPaymentStatus());
            assertEquals(stock,fixture.scalar("SELECT quantity FROM dbo.books WHERE bookid=?",book)); previous=status;
        }
        fixture.execute("UPDATE dbo.orders SET order_status='NEW' WHERE order_id=?",id);
        assertEquals("Đơn hàng mới",dao.findOwnedOrder(id,user).getStatusLabel());
    }
    @Test void demoFileDoesNotRunAllUpdatesAndInvalidStateStillViolatesCheck() throws Exception {
        int user=fixture.userIds.get(0); long id=order(user,"NEW");
        String script=Files.readString(Path.of("database/demo/BT11_order_status_demo.sql"));
        try(Connection c=DBContext_24133049.getConnection(); Statement ps=c.createStatement()) {
            for(String batch:script.split("(?im)^GO\\s*$")) if(!batch.isBlank()) ps.execute(batch);
        }
        assertEquals("NEW",dao.findOwnedOrder(id,user).getStatus());
        assertThrows(SQLException.class,()->fixture.execute("UPDATE dbo.orders SET order_status='NOT_VALID' WHERE order_id=?",id));
        long stock=fixture.scalar("SELECT quantity FROM dbo.books WHERE bookid=?",fixture.bookIds.get(0));
        var before=dao.findOwnedOrder(id,user);
        Matcher blocks=Pattern.compile("/\\*(.*?)\\*/",Pattern.DOTALL).matcher(script);
        int executed=0;
        while(blocks.find()) {
            String sql=blocks.group(1).replace("DECLARE @OrderId BIGINT = NULL;","DECLARE @OrderId BIGINT = "+id+";");
            try(Connection c=DBContext_24133049.getConnection(); Statement ps=c.createStatement()) {
                ps.execute(sql);
            }
            var after=dao.findOwnedOrder(id,user);
            assertEquals(OrderStatus_24133049.values()[executed++].getCode(),after.getStatus());
            assertEquals(before.getTotal(),after.getTotal()); assertEquals(before.getPaymentStatus(),after.getPaymentStatus());
            assertEquals(stock,fixture.scalar("SELECT quantity FROM dbo.books WHERE bookid=?",fixture.bookIds.get(0)));
        }
        assertEquals(8,executed);
        fixture.execute("UPDATE dbo.orders SET order_status='NEW' WHERE order_id=?",id);
    }
}
