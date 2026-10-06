package vn.edu.hcmute.exam.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import vn.edu.hcmute.exam.support.CommerceFixture_24133049;
import vn.edu.hcmute.exam.dao.OrderDao_24133049;
import vn.edu.hcmute.exam.model.OrderStatus_24133049;
import vn.edu.hcmute.exam.model.Shipping_24133049;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named="RUN_HTTP_TESTS", matches="true")
public class CommerceHttpTest_24133049 {
    private final String base = System.getProperty("test.baseUrl", "http://localhost:18080/GK_laptrinh_web_24133049");
    private CommerceFixture_24133049 fixture;
    private HttpClient client;
    @BeforeEach void create() throws Exception { fixture=new CommerceFixture_24133049(); fixture.create(); client=newClient(); }
    @AfterEach void cleanup() throws Exception { if(fixture!=null) fixture.close(); }
    private HttpClient newClient() { return HttpClient.newBuilder().cookieHandler(new CookieManager(null,CookiePolicy.ACCEPT_ALL)).connectTimeout(Duration.ofSeconds(10)).build(); }
    private HttpResponse<String> get(HttpClient c,String path) throws Exception {
        return c.send(HttpRequest.newBuilder(URI.create(base+path)).timeout(Duration.ofSeconds(20)).GET().build(),HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
    private HttpResponse<String> post(HttpClient c,String path,Map<String,String> form) throws Exception {
        List<String> pairs=new ArrayList<>(); form.forEach((k,v) -> pairs.add(URLEncoder.encode(k,StandardCharsets.UTF_8)+"="+URLEncoder.encode(v,StandardCharsets.UTF_8)));
        return c.send(HttpRequest.newBuilder(URI.create(base+path)).timeout(Duration.ofSeconds(20)).header("Content-Type","application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(String.join("&",pairs))).build(),HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
    private void login(HttpClient c,int index) throws Exception {
        assertEquals(200,get(c,"/login").statusCode());
        assertEquals(302,post(c,"/login",Map.of("email",fixture.email(index),"password","BT11-test-123!")).statusCode());
    }
    private int book() { return fixture.bookIds.get(0); }
    private String csrf(HttpClient c) throws Exception { return field(get(c,"/book-detail?id="+book()).body(),"csrfToken"); }
    private String field(String html,String name) {
        Matcher m=Pattern.compile("name=\""+name+"\"[^>]*value=\"([^\"]*)\"").matcher(html);
        assertTrue(m.find(),"Missing form field "+name+" in "+html.substring(0,Math.min(500,html.length()))); return m.group(1);
    }
    private HttpResponse<String> cartPost(HttpClient c,String csrf,String action,int id,String quantity) throws Exception {
        return post(c,"/cart",Map.of("csrfToken",csrf,"action",action,"bookId",Integer.toString(id),"quantity",quantity));
    }
    private Map<String,String> delivery(String csrf,String token) {
        return Map.of("csrfToken",csrf,"checkoutToken",token,"recipientName","Người nhận <script>","phone","0901234567","address","123 Đường A & B","note","Gọi trước <b>giờ giao</b>");
    }
    private long historyOrder(int user, String status) throws Exception {
        long id=new OrderDao_24133049().createCodOrder(user,Map.of(book(),1),
            new Shipping_24133049("Demo lịch sử","0901234567","Địa chỉ demo",""),UUID.randomUUID().toString());
        fixture.execute("UPDATE dbo.orders SET order_status=?,created_at='2026-10-06T10:00:00' WHERE order_id=?",status,id);
        return id;
    }
    private List<Long> historyIds(String html) {
        List<Long> ids=new ArrayList<>(); Matcher matcher=Pattern.compile("data-order-id=\"([0-9]+)\"").matcher(html);
        while(matcher.find()) ids.add(Long.parseLong(matcher.group(1))); return ids;
    }
    @Test void historyPaginationOwnershipEmptyStatesAndInvalidParameters() throws Exception {
        login(client,0);
        assertTrue(get(client,"/order-history").body().contains("Bạn chưa có đơn hàng nào"));
        fixture.execute("UPDATE dbo.books SET quantity=100 WHERE bookid=?",book());
        int user=fixture.userIds.get(0); List<Long> own=new ArrayList<>();
        for(int i=0;i<21;i++) own.add(historyOrder(user,"DELIVERING"));
        long shipped=historyOrder(user,"SHIPPED"); long newest=historyOrder(user,"NEW");
        long foreign=historyOrder(fixture.userIds.get(1),"DELIVERING");
        var all=get(client,"/order-history?userId="+fixture.userIds.get(1));
        assertEquals(200,all.statusCode()); assertTrue(all.body().contains("Tổng số đơn: 23"));
        assertEquals(newest,historyIds(all.body()).get(0)); assertEquals(shipped,historyIds(all.body()).get(1));
        assertFalse(historyIds(all.body()).contains(foreign)); assertEquals(10,historyIds(all.body()).size());
        assertTrue(all.body().contains("navbarUserContent")); assertEquals("no-store",all.headers().firstValue("Cache-Control").orElseThrow());
        String filtered=get(client,"/order-history?status=DELIVERING&page=2").body();
        assertTrue(filtered.contains("Tổng số đơn: 21")); assertTrue(filtered.contains("Trang 2 / 3"));
        assertTrue(filtered.contains("status=DELIVERING&amp;page=3")); assertTrue(filtered.contains("name=\"page\" value=\"1\""));
        assertEquals(10,historyIds(filtered).size());
        String last=get(client,"/order-history?status=DELIVERING&page=9223372036854775807").body();
        assertTrue(last.contains("Trang 3 / 3")); assertEquals(1,historyIds(last).size());
        for(String page:List.of("0","-1","abc","1.5","9223372036854775808","999999999999999999999999")) {
            String invalid=get(client,"/order-history?status=DELIVERING&page="+page).body();
            assertTrue(invalid.contains("Số trang không hợp lệ")); assertTrue(invalid.contains("Trang 1 / 3"));
        }
        String invalid=get(client,"/order-history?status=UNPAID").body();
        assertTrue(invalid.contains("Bộ lọc trạng thái không hợp lệ")); assertTrue(invalid.contains("Tổng số đơn: 23"));
        assertTrue(get(client,"/order-history?status=RETURNED").body().contains("Không có đơn hàng khớp"));
        HttpClient other=newClient(); login(other,1);
        assertEquals(1,historyIds(get(other,"/order-history").body()).size());
        assertEquals(404,get(other,"/order?id="+newest).statusCode());
    }
    @Test void oldCodCheckoutAppearsInHistoryAndSqlRefreshMovesAllEightStatesWithoutOtherChanges() throws Exception {
        login(client,0); String csrf=csrf(client); cartPost(client,csrf,"add",book(),"1");
        String token=field(get(client,"/checkout").body(),"checkoutToken");
        var created=post(client,"/checkout",delivery(csrf,token)); assertEquals(302,created.statusCode());
        String location=created.headers().firstValue("location").orElseThrow();
        long id=Long.parseLong(location.substring(location.indexOf("id=")+3));
        assertEquals(List.of(id),historyIds(get(client,"/order-history?status=NEW").body()));
        long stock=fixture.scalar("SELECT quantity FROM dbo.books WHERE bookid=?",book());
        String previous="NEW";
        for(var status:OrderStatus_24133049.values()) {
            fixture.execute("UPDATE dbo.orders SET order_status=? WHERE order_id=?",status.getCode(),id);
            String history=get(client,"/order-history?status="+status.getCode()).body();
            assertEquals(List.of(id),historyIds(history)); assertTrue(history.contains(status.getLabel()));
            if(!previous.equals(status.getCode())) assertTrue(historyIds(get(client,"/order-history?status="+previous).body()).isEmpty());
            String detail=get(client,"/order?id="+id).body(); assertTrue(detail.contains(status.getLabel())); assertTrue(detail.contains("UNPAID"));
            assertEquals(stock,fixture.scalar("SELECT quantity FROM dbo.books WHERE bookid=?",book()));
            assertEquals(1235,fixture.scalar("SELECT total_amount*100 FROM dbo.orders WHERE order_id=?",id));
            previous=status.getCode();
        }
        for(String alias:List.of("PENDING","SHIPPING","DELIVERED")) {
            fixture.execute("UPDATE dbo.orders SET order_status=? WHERE order_id=?",alias,id);
            var status=OrderStatus_24133049.fromCode(alias);
            String result=get(client,"/order-history?status="+status.getCode()).body();
            assertEquals(List.of(id),historyIds(result)); assertTrue(result.contains(status.getLabel()));
        }
        fixture.execute("UPDATE dbo.orders SET order_status='NEW' WHERE order_id=?",id);
    }
    @Test void historyDeniesGuestAdminAndInactiveAccounts() throws Exception {
        assertEquals(302,get(client,"/order-history").statusCode());
        login(client,2); assertEquals(403,get(client,"/order-history").statusCode());
        login(client,0); fixture.execute("UPDATE dbo.users SET is_active=0 WHERE id=?",fixture.userIds.get(0));
        assertEquals(403,get(client,"/order-history").statusCode());
        assertEquals(403,get(client,"/order?id=1").statusCode());
    }
    @Test void cartAddsAccumulatesUpdatesRemovesAndClearsWithServerValidation() throws Exception {
        login(client,0); String csrf=csrf(client);
        assertTrue(get(client,"/home").body().contains("Thêm vào giỏ"), "UTF-8 cart button on book list");
        assertTrue(get(client,"/book-detail?id="+book()).body().contains("Thêm vào giỏ"), "UTF-8 cart button on book detail");
        assertEquals(302,cartPost(client,csrf,"add",book(),"2").statusCode());
        assertEquals(302,cartPost(client,csrf,"add",book(),"3").statusCode());
        String html=get(client,"/cart").body(); assertTrue(html.contains("value=\"5\"")); assertTrue(html.contains("navbarUserContent"));
        for(String quantity:List.of("0","-1","1.5","abc","2147483648","999999999999999999","11","6")) {
            assertEquals(302,cartPost(client,csrf,"add",book(),quantity).statusCode());
            assertTrue(get(client,"/cart").body().contains("value=\"5\""));
        }
        for(String quantity:List.of("0","-1","1.5","abc","2147483648","11")) {
            assertEquals(302,cartPost(client,csrf,"update",book(),quantity).statusCode());
            assertTrue(get(client,"/cart").body().contains("value=\"5\""));
        }
        assertEquals(302,cartPost(client,csrf,"add",fixture.bookIds.get(1),"2").statusCode());
        for(String quantity:List.of("4","3","2")) assertEquals(302,cartPost(client,csrf,"update",book(),quantity).statusCode());
        assertEquals(302,cartPost(client,csrf,"remove",fixture.bookIds.get(1),"1").statusCode());
        assertEquals(302,post(client,"/cart",Map.of("csrfToken",csrf,"action","clear")).statusCode());
        assertTrue(get(client,"/cart").body().contains("value=\"2\""));
        assertEquals(302,post(client,"/cart",Map.of("csrfToken",csrf,"action","clear","confirmed","yes")).statusCode());
        assertTrue(get(client,"/cart").body().contains("Giỏ hàng đang trống"));
        assertEquals(302,get(client,"/checkout").statusCode());
        fixture.execute("UPDATE dbo.books SET quantity=0 WHERE bookid=?",book());
        assertEquals(302,cartPost(client,csrf,"add",book(),"1").statusCode());
        assertTrue(get(client,"/cart").body().contains("hết hàng"));
        assertEquals(302,cartPost(client,csrf,"add",Integer.MAX_VALUE,"1").statusCode());
        assertTrue(get(client,"/cart").body().contains("không tồn tại"));
    }
    @Test void deniesGuestAdminInactiveForgedPostsAndSeparatesAccounts() throws Exception {
        assertEquals(302,get(client,"/cart").statusCode()); assertEquals(302,get(client,"/checkout").statusCode());
        assertEquals(302,post(client,"/cart",Map.of("action","add","bookId",Integer.toString(book()),"quantity","1")).statusCode());
        login(client,0); String csrf=csrf(client);
        assertEquals(403,cartPost(client,"forged","add",book(),"1").statusCode());
        assertEquals(403,post(client,"/checkout",Map.of()).statusCode());
        cartPost(client,csrf,"add",book(),"2");
        get(client,"/cart?action=clear&confirmed=yes"); assertTrue(get(client,"/cart").body().contains("value=\"2\""));
        fixture.execute("UPDATE dbo.users SET is_active=0 WHERE id=?",fixture.userIds.get(0));
        assertEquals(403,get(client,"/cart").statusCode()); assertEquals(403,get(client,"/checkout").statusCode());
        assertEquals(403,get(client,"/order?id=1").statusCode());
        fixture.execute("UPDATE dbo.users SET is_active=1 WHERE id=?",fixture.userIds.get(0));
        login(client,1); assertTrue(get(client,"/cart").body().contains("Giỏ hàng đang trống"));
        assertEquals(403,cartPost(client,csrf,"add",book(),"1").statusCode());
        String secondCsrf=csrf(client); cartPost(client,secondCsrf,"add",book(),"1"); get(client,"/logout");
        login(client,1); assertTrue(get(client,"/cart").body().contains("Giỏ hàng đang trống"));
        login(client,2);
        assertEquals(403,get(client,"/cart").statusCode()); assertEquals(403,get(client,"/checkout").statusCode());
        assertEquals(403,cartPost(client,secondCsrf,"add",book(),"1").statusCode());
        assertEquals(403,post(client,"/checkout",Map.of()).statusCode());
    }
    @Test void checkoutPreservesInvalidShippingAndDetectsStockReductionAndMissingBook() throws Exception {
        login(client,0); String csrf=csrf(client); cartPost(client,csrf,"add",book(),"3");
        String token=field(get(client,"/checkout").body(),"checkoutToken");
        var fields=new HashMap<>(delivery(csrf,token)); fields.put("phone","invalid");
        var invalid=post(client,"/checkout",fields); assertEquals(200,invalid.statusCode());
        assertTrue(invalid.body().contains("invalid")); assertTrue(invalid.body().contains("&lt;script&gt;")); assertFalse(invalid.body().contains("Người nhận <script>"));
        fields.put("phone","0901234567"); fields.put("address",""); assertEquals(200,post(client,"/checkout",fields).statusCode());
        fields.put("address","A".repeat(501)); assertEquals(200,post(client,"/checkout",fields).statusCode());
        fixture.execute("UPDATE dbo.books SET quantity=1 WHERE bookid=?",book());
        String cart=get(client,"/cart").body(); assertTrue(cart.contains("tồn kho đã giảm")); assertTrue(cart.contains("value=\"3\""));
        assertEquals(200,post(client,"/checkout",delivery(csrf,token)).statusCode());
        assertEquals(0,fixture.scalar("SELECT COUNT(*) FROM dbo.orders WHERE user_id=?",fixture.userIds.get(0)));
        assertEquals(1,fixture.scalar("SELECT quantity FROM dbo.books WHERE bookid=?",book()));
        fixture.execute("DELETE FROM dbo.book_author WHERE bookid=?",book()); fixture.execute("DELETE FROM dbo.books WHERE bookid=?",book());
        assertTrue(get(client,"/cart").body().contains("đã bị xóa"));
        assertEquals(200,post(client,"/checkout",delivery(csrf,token)).statusCode());
        cartPost(client,csrf,"remove",book(),"1"); assertTrue(get(client,"/cart").body().contains("Giỏ hàng đang trống"));
    }
    @Test void codDoubleClickReplayRefreshPersistsOneOrderAndOwnerOnlyAndAdminGetsBusinessMessage() throws Exception {
        login(client,0); String csrf=csrf(client);
        cartPost(client,csrf,"add",book(),"2"); cartPost(client,csrf,"add",fixture.bookIds.get(1),"3");
        String checkout=get(client,"/checkout").body(); assertTrue(checkout.contains("User BT11 0")); assertTrue(checkout.contains("0901234567"));
        String token=field(checkout,"checkoutToken"); Map<String,String> fields=delivery(csrf,token);
        ExecutorService pool=Executors.newFixedThreadPool(2);
        HttpResponse<String> first, second;
        try {
            Future<HttpResponse<String>> a=pool.submit(() -> post(client,"/checkout",fields));
            Future<HttpResponse<String>> b=pool.submit(() -> post(client,"/checkout",fields));
            first=a.get(25,TimeUnit.SECONDS); second=b.get(25,TimeUnit.SECONDS);
        } finally { pool.shutdownNow(); }
        assertEquals(302,first.statusCode()); assertEquals(302,second.statusCode());
        String location=first.headers().firstValue("location").orElseThrow(); assertEquals(location,second.headers().firstValue("location").orElseThrow());
        long id=Long.parseLong(location.substring(location.indexOf("id=")+3));
        assertEquals(1,fixture.scalar("SELECT COUNT(*) FROM dbo.orders WHERE user_id=?",fixture.userIds.get(0)));
        assertEquals(2,fixture.scalar("SELECT COUNT(*) FROM dbo.order_items WHERE order_id=?",id));
        assertEquals(8,fixture.scalar("SELECT quantity FROM dbo.books WHERE bookid=?",book()));
        assertTrue(get(client,"/cart").body().contains("Giỏ hàng đang trống"));
        String success=get(client,"/order-success?id="+id).body(); assertTrue(success.contains("Đặt hàng thành công")); assertTrue(success.contains("UNPAID"));
        assertTrue(success.contains("Người nhận &lt;script&gt;")); assertTrue(success.contains("navbarUserContent"));
        assertEquals(302,post(client,"/checkout",fields).statusCode()); assertEquals(200,get(client,"/order-success?id="+id).statusCode());
        HttpClient another=newClient(); login(another,1);
        assertEquals(404,get(another,"/order?id="+id).statusCode()); assertEquals(404,get(another,"/order-success?id="+id).statusCode());
        assertEquals(200,get(client,"/order?id="+id).statusCode());
        assertEquals(302,post(client,"/review/add",Map.of("bookId",Integer.toString(book()),"rating","5","reviewText","BT11 <script>review</script>")).statusCode());
        String detail=get(client,"/book-detail?id="+book()).body();
        assertTrue(detail.contains("BT11 &lt;script&gt;review&lt;/script&gt;"));
        login(another,2); assertEquals(200,get(another,"/admin/books").statusCode());
        assertEquals(302,post(another,"/admin/books/delete",Map.of("id",Integer.toString(book()))).statusCode());
        assertTrue(get(another,"/admin/books").body().contains("Sách đã có trong đơn hàng"));
        assertEquals(1,fixture.scalar("SELECT COUNT(*) FROM dbo.rating WHERE bookid=? AND userid=?",book(),fixture.userIds.get(0)));
        assertEquals(200,get(client,"/home?page=2").statusCode()); assertEquals(200,get(client,"/books").statusCode());
        assertEquals(200,get(client,"/book-detail?id="+book()).statusCode()); assertEquals(200,get(client,"/register").statusCode());
        assertEquals(200,get(client,"/verify-otp").statusCode());
    }
}
