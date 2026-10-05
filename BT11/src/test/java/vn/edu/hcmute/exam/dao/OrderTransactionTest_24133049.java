package vn.edu.hcmute.exam.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import vn.edu.hcmute.exam.config.DBContext_24133049;
import vn.edu.hcmute.exam.model.*;
import vn.edu.hcmute.exam.service.OrderService_24133049;
import vn.edu.hcmute.exam.support.CommerceFixture_24133049;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named="RUN_SQLSERVER_TESTS", matches="true")
public class OrderTransactionTest_24133049 {
    private CommerceFixture_24133049 fixture;
    private final OrderDao_24133049 dao = new OrderDao_24133049();
    private final Shipping_24133049 shipping = new Shipping_24133049("Người nhận", "0901234567", "1 Võ Văn Ngân", "Giao giờ hành chính");
    @BeforeEach void create() throws Exception { fixture = new CommerceFixture_24133049(); fixture.create(); }
    @AfterEach void cleanup() throws Exception { if (fixture != null) fixture.close(); }
    private String token() { return UUID.randomUUID().toString(); }
    private int user() { return fixture.userIds.get(0); }
    private int book() { return fixture.bookIds.get(0); }
    private long stock(int id) throws Exception { return fixture.scalar("SELECT quantity FROM dbo.books WHERE bookid=?", id); }

    @Test void commitsCodSnapshotsExactMoneyAndOwnerOnlyAndBlocksSoldBookDeletion() throws Exception {
        long id = dao.createCodOrder(user(), Map.of(book(), 2, fixture.bookIds.get(1), 3), shipping, token());
        Order_24133049 order = dao.findOwnedOrder(id, user());
        assertEquals(new BigDecimal("25.00"), order.getTotal()); assertEquals(2, order.getItems().size());
        assertEquals("NEW", order.getStatus()); assertEquals("UNPAID", order.getPaymentStatus()); assertEquals("COD", order.getPaymentMethod());
        assertEquals(8, stock(book())); assertEquals(7, stock(fixture.bookIds.get(1)));
        assertNull(dao.findOwnedOrder(id, fixture.userIds.get(1)));
        fixture.execute("UPDATE dbo.books SET price=99.99,title=N'Giá đã thay đổi' WHERE bookid=?", book());
        assertEquals(new BigDecimal("12.35"), dao.findOwnedOrder(id,user()).getItems().get(0).getUnitPrice());
        assertEquals(new BigDecimal("25.00"), dao.findOwnedOrder(id,user()).getTotal());
        assertThrows(IllegalStateException.class, () -> new BookDaoImpl_24133049().delete(book()));
        assertEquals(1, fixture.scalar("SELECT COUNT(*) FROM dbo.book_author WHERE bookid=?", book()));
    }
    @Test void rejectsEmptyMissingOutOfStockAndIneligibleUsersWithoutWrites() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> dao.createCodOrder(user(), Map.of(), shipping, token()));
        assertThrows(IllegalArgumentException.class, () -> dao.createCodOrder(user(), Map.of(Integer.MAX_VALUE,1), shipping, token()));
        fixture.execute("UPDATE dbo.books SET quantity=0 WHERE bookid=?",book());
        assertThrows(IllegalArgumentException.class, () -> dao.createCodOrder(user(), Map.of(book(),1),shipping,token()));
        assertThrows(IllegalArgumentException.class, () -> dao.createCodOrder(fixture.userIds.get(2), Map.of(book(),1),shipping,token()));
        fixture.execute("UPDATE dbo.users SET is_active=0 WHERE id=?",user());
        assertThrows(IllegalArgumentException.class, () -> dao.createCodOrder(user(), Map.of(book(),1),shipping,token()));
        assertEquals(0, fixture.scalar("SELECT COUNT(*) FROM dbo.orders WHERE user_id=?",user()));
    }
    @Test void lateStockReductionRollsBackEntireCartWithoutPartialDeductions() throws Exception {
        fixture.execute("UPDATE dbo.books SET quantity=1 WHERE bookid=?",fixture.bookIds.get(1));
        assertThrows(IllegalArgumentException.class, () -> dao.createCodOrder(user(),Map.of(book(),2,fixture.bookIds.get(1),2),shipping,token()));
        assertEquals(10,stock(book())); assertEquals(1,stock(fixture.bookIds.get(1)));
        assertEquals(0,fixture.scalar("SELECT COUNT(*) FROM dbo.orders WHERE user_id=?",user()));
    }
    @Test void injectedFailureAfterFirstStockDeductionRollsBackAndRetainsSessionCart() throws Exception {
        OrderDao_24133049 failing = new OrderDao_24133049(() -> wrapConnection(DBContext_24133049.getConnection()));
        Cart_24133049 cart = new Cart_24133049(user());
        cart.setQuantity(book(),2); cart.setQuantity(fixture.bookIds.get(1),3); String token=token(); cart.setCheckoutToken(token);
        assertThrows(SQLException.class, () -> new OrderService_24133049(failing).checkout(cart,shipping,token));
        assertEquals(2,cart.snapshot().size()); assertEquals(token,cart.getCheckoutToken());
        assertEquals(10,stock(book())); assertEquals(10,stock(fixture.bookIds.get(1)));
        assertEquals(0,fixture.scalar("SELECT COUNT(*) FROM dbo.orders WHERE user_id=?",user()));
        assertEquals(0,fixture.scalar("SELECT COUNT(*) FROM dbo.order_items WHERE book_id=?",book()));
    }
    private Connection wrapConnection(Connection actual) {
        int[] detailInserts={0};
        return (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Connection.class}, (proxy,method,args) -> {
            Object result = invoke(actual,method,args);
            if (method.getName().equals("prepareStatement") && ((String)args[0]).startsWith("INSERT INTO dbo.order_items")) {
                PreparedStatement ps=(PreparedStatement)result;
                return Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{PreparedStatement.class},(p,m,a) -> {
                    if (m.getName().equals("executeUpdate") && ++detailInserts[0]==2) throw new SQLException("Injected failure after first detail + stock deduction");
                    return invoke(ps,m,a);
                });
            }
            return result;
        });
    }
    private static Object invoke(Object target, Method method, Object[] args) throws Throwable {
        try { return method.invoke(target,args); } catch (InvocationTargetException e) { throw e.getCause(); }
    }
    @Test void competingBuyersCannotPurchaseLastCopyTwice() throws Exception {
        fixture.execute("UPDATE dbo.books SET quantity=1 WHERE bookid=?",book());
        ExecutorService pool=Executors.newFixedThreadPool(2); CountDownLatch start=new CountDownLatch(1);
        try {
            List<Future<Boolean>> results=new ArrayList<>();
            for (int buyer : fixture.userIds.subList(0,2)) results.add(pool.submit(() -> {
                start.await(); try { dao.createCodOrder(buyer,Map.of(book(),1),shipping,token()); return true; }
                catch (IllegalArgumentException e) { return false; }
            }));
            start.countDown(); int success=0; for (var result:results) if(result.get(20,TimeUnit.SECONDS)) success++;
            assertEquals(1,success); assertEquals(0,stock(book()));
            assertEquals(1,fixture.scalar("SELECT COUNT(*) FROM dbo.order_items WHERE book_id=?",book()));
        } finally { pool.shutdownNow(); }
    }
    @Test void simultaneousReplayWithSameTokenReturnsSameCommittedOrder() throws Exception {
        String token=token(); ExecutorService pool=Executors.newFixedThreadPool(2); CountDownLatch start=new CountDownLatch(1);
        try {
            Callable<Long> action=() -> { start.await(); return dao.createCodOrder(user(),Map.of(book(),2),shipping,token); };
            Future<Long> a=pool.submit(action), b=pool.submit(action); start.countDown();
            assertEquals(a.get(20,TimeUnit.SECONDS),b.get(20,TimeUnit.SECONDS));
            assertEquals(8,stock(book())); assertEquals(1,fixture.scalar("SELECT COUNT(*) FROM dbo.orders WHERE user_id=?",user()));
        } finally { pool.shutdownNow(); }
    }
}
