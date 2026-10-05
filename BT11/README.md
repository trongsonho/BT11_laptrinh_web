# BookStore – Bài kiểm tra quá trình Lập trình Web

Ứng dụng quản lý và mua sách bằng Java Web, bổ sung **giỏ hàng** và **đặt hàng COD** cho User. Project giữ kiến trúc Servlet → Service → DAO → SQL Server và đóng gói Maven WAR.

| Thông tin | Nội dung |
|---|---|
| Sinh viên | Hồ Trọng Sơn |
| MSSV | 24133049 |
| Trường | Đại học Sư phạm Kỹ thuật TP.HCM – HCMUTE |
| Môn học | Lập trình Web, HK1 năm học 2026–2027 |
| Giảng viên | ThS. Nguyễn Hữu Trung |
| Đề thi gốc | Đề số 02 |
| Package | `vn.edu.hcmute.exam` |
| Database | `BookStore_24133049` |
| Tên WAR / context | `GK_laptrinh_web_24133049` / `/GK_laptrinh_web_24133049` |

## 1. Chạy nhanh trên máy hiện tại

### Điều kiện trước khi chạy

- SQL Server instance `.\SQLEXPRESS` đang hoạt động và đã có database `BookStore_24133049` cùng các bảng gốc.
- Maven, JDK và Tomcat đã được cài đặt; các đường dẫn trong lệnh dưới đây là đường dẫn trên máy hiện tại.
- JDBC driver và DLL Windows Authentication đã có trong Tomcat theo [phần cấu hình](#2-công-nghệ-và-cấu-hình).

Sau khi clone repository https://github.com/trongsonho/BT11_laptrinh_web, mở PowerShell trong thư mục `BT11` chứa `pom.xml`. Lệnh dưới đây giả sử repository được clone tại `D:\BT11_laptrinh_web`; thay đường dẫn nếu cần:

```powershell
cd D:\BT11_laptrinh_web\BT11

# Dừng Tomcat kiểm thử cũ nếu đang chạy.
./tests/stop-test-tomcat.ps1

# Chỉ bổ sung schema đơn hàng vào database hiện có.
sqlcmd -S .\SQLEXPRESS -E -C -d BookStore_24133049 -b -i database/migrations/BT11_cart_cod.sql

# Build và chạy các test logic mặc định.
Remove-Item Env:RUN_SQLSERVER_TESTS -ErrorAction SilentlyContinue
Remove-Item Env:RUN_HTTP_TESTS -ErrorAction SilentlyContinue
mvn clean package

# Chỉ thực hiện sau khi Maven báo BUILD SUCCESS.
./tests/start-test-tomcat.ps1 -TomcatHome 'D:\apache-tomcat-11.0.25-windows-x64\apache-tomcat-11.0.25' -JavaHome 'D:\jdk-26_windows-x64_bin\jdk-26.0.2.1'
```

Chờ Tomcat khởi động, sau đó mở:

**[http://localhost:18080/GK_laptrinh_web_24133049/home](http://localhost:18080/GK_laptrinh_web_24133049/home)**

| Vai trò | Email mẫu | Mật khẩu mẫu |
|---|---|---|
| User | `user1@gmail.com` | `123456` |
| Admin | `admin@bookstore.vn` | `123456` |

Các tài khoản trên thuộc dữ liệu mẫu gốc; sử dụng nếu chúng vẫn còn và chưa được đổi mật khẩu. Migration COD không tạo lại tài khoản hoặc seed dữ liệu.

**Dừng ứng dụng kiểm thử:**

```powershell
./tests/stop-test-tomcat.ps1
```

Script chỉ dừng Tomcat kiểm thử của project này, kể cả bản cũ từng chạy trong `target/tomcat-test`. Runtime hiện lưu tại `.runtime/tomcat-test`, tách khỏi `target` để Maven clean không xóa các file log đang mở.

> **Không chạy lại `database.sql` trên database đang dùng.** Script gốc có lệnh DROP bảng. Chỉ chạy `database/migrations/BT11_cart_cod.sql` để bổ sung chức năng COD; migration có thể chạy lại mà không xóa dữ liệu.

## 2. Công nghệ và cấu hình

| Thành phần | Cấu hình trong project |
|---|---|
| Java | Biên dịch với `release=17` |
| Maven | Đóng gói WAR; môi trường đã kiểm thử dùng Maven 3.9.16 |
| Web container | Tomcat 11; môi trường đã kiểm thử dùng 11.0.25 |
| Servlet / JSP | Jakarta Servlet 6.1.0 / Jakarta JSP 3.1.1, scope `provided` |
| JSTL | API 3.0.0, implementation 3.0.1 |
| Giao diện | Bootstrap 5, SiteMesh 3.3.0-RC1, tiếng Việt UTF-8 |
| Database | SQL Server qua JDBC `mssql-jdbc:12.6.4.jre11` |
| Email OTP | Jakarta Mail 2.1.3 và Angus Mail 2.0.3 |
| Kiểm thử | JUnit Jupiter 5.10.3 và Maven Surefire 3.2.5 |

Các lớp Java tiếp tục sử dụng hậu tố `_24133049`. Cấu hình decorator: `/admin/*` dùng `admin.jsp`, các trang User dùng `user.jsp`.

### Kết nối SQL Server

`DBContext_24133049` mặc định kết nối `127.0.0.1`, instance `SQLEXPRESS`, database `BookStore_24133049`, bằng Windows Authentication. Tài khoản Windows chạy Tomcat và test JDBC cần có quyền truy cập database.

JDBC driver dùng scope **`provided`**, nên không nằm trong WAR. Cần đặt:

- `mssql-jdbc-12.6.4.jre11.jar` trong `CATALINA_HOME/lib/`.
- DLL `mssql-jdbc_auth-12.6.4.x64.dll` trong `CATALINA_HOME/bin/` khi dùng Windows Authentication với JVM x64.
- JVM option `-Djava.library.path` trỏ đến thư mục chứa DLL. Script chạy thử đã thiết lập đường dẫn này.

Khi dùng cấu hình kết nối khác, thiết lập biến môi trường cho tiến trình chạy ứng dụng:

| Biến | Ý nghĩa |
|---|---|
| `DB_URL` | JDBC URL thay thế cấu hình mặc định |
| `DB_USER` | Tên đăng nhập SQL Authentication, nếu sử dụng |
| `DB_PASSWORD` | Mật khẩu SQL Authentication, nếu sử dụng |

Không đưa mật khẩu kết nối vào source hoặc commit. Khi không có `DB_URL`, ứng dụng dùng cấu hình Windows Authentication mặc định.

### Email kích hoạt OTP

Đăng ký và kích hoạt tài khoản cần SMTP. Có thể cấu hình qua các biến môi trường `SMTP_HOST`, `SMTP_PORT`, `SMTP_AUTH`, `SMTP_STARTTLS`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `SMTP_FROM`; xem mẫu tại `src/main/resources/mail.properties.example`. Đặt các biến trước khi khởi động Tomcat. Giỏ hàng và COD không yêu cầu SMTP khi sử dụng User đã kích hoạt.

### Deploy vào Tomcat thông thường

Sau khi build thành công, sao chép `target/GK_laptrinh_web_24133049.war` vào `CATALINA_HOME/webapps/` và khởi động Tomcat bằng `bin/startup.bat`. Cấu hình JDK cùng native library trong `bin/setenv.bat` hoặc môi trường của Tomcat.

Nếu Tomcat dùng cổng mặc định 8080, mở [http://localhost:8080/GK_laptrinh_web_24133049/home](http://localhost:8080/GK_laptrinh_web_24133049/home). Cổng **18080** chỉ dành cho script kiểm thử của project.

Máy hiện tại chạy JDK 26. Script kiểm thử có `--enable-native-access=ALL-UNNAMED`; nếu chuyển sang JDK 17, cần bỏ tùy chọn này trong script và lệnh test. Chưa kiểm chứng runtime bằng đúng JDK 17.

## 3. Các chức năng

### Chức năng gốc

- Đăng ký, kích hoạt OTP, đăng nhập và đăng xuất; session lưu tài khoản ở `currentUser`.
- Danh sách sách lọc theo tác giả, phân trang 3 sách/trang và xem chi tiết.
- User đăng nhập gửi hoặc cập nhật đánh giá sách.
- Admin quản lý sách: danh sách, thêm, sửa, xóa; phân trang 5 sách/trang.

### Giỏ hàng

- Thêm sách từ danh sách hoặc chi tiết, chọn số lượng; thêm lại cùng sách cộng vào một dòng.
- Hiển thị ảnh, tên sách, đơn giá, số lượng, thành tiền và tổng tiền; thanh điều hướng có badge tổng số lượng.
- Sửa số lượng bằng ô nhập hoặc nút ± rồi **Lưu**; xóa từng sách hoặc xóa toàn bộ có xác nhận.
- Giỏ lưu ID sách và số lượng trong HttpSession, gắn với ID tài khoản. Đổi tài khoản xóa giỏ cũ; đăng xuất hủy session.
- Chỉ User đang hoạt động và không phải Admin được dùng. Quyền được kiểm tra lại trên server từ database.
- Số lượng phải là số nguyên từ 1 đến tồn kho, kể cả tổng sau khi cộng thêm. Số âm, 0, thập phân, ký tự, tràn số và vượt kho bị chặn.
- Thêm giỏ chưa trừ kho. Nếu sách bị xóa, hết hàng hoặc tồn kho giảm, giỏ báo lỗi và giữ nguyên số lượng để User tự điều chỉnh.

### Đặt hàng COD

- Nhập tên người nhận, điện thoại, địa chỉ và ghi chú tùy chọn; điền sẵn tên/điện thoại tài khoản.
- Kiểm tra thông tin giao hàng trên server và giữ dữ liệu đã nhập khi có lỗi.
- Đơn mới có phương thức **COD**, trạng thái đơn **`NEW`**, trạng thái thanh toán **`UNPAID`**.
- Lưu đơn và chi tiết thật trong SQL Server. Chi tiết lưu tên sách, đơn giá, số lượng và thành tiền tại thời điểm mua; giá mới không làm thay đổi đơn cũ.
- Chỉ chủ đơn được xem trang thành công và chi tiết đơn; đổi ID sang đơn của User khác nhận 404.
- Không tích hợp thanh toán online, phí vận chuyển hoặc khuyến mãi.

### Tính nguyên tử và bảo vệ request

Tạo đơn, tạo chi tiết và trừ tồn kho thực hiện trong **một transaction trên cùng Connection**. DAO đọc lại giá và tồn kho, khóa `UPDLOCK,HOLDLOCK` theo bookid tăng dần và kiểm tra số dòng của UPDATE có điều kiện. Khi lỗi, toàn bộ transaction rollback và giỏ được giữ nguyên; chỉ xóa giỏ sau commit thành công.

Các thao tác thay đổi giỏ và xác nhận đơn dùng POST kèm CSRF token, chuyển hướng sau thành công theo Post/Redirect/Get. Token checkout một lần được kiểm soát nguyên tử trong session; UNIQUE `(user_id, checkout_token)` bảo vệ chống đơn trùng ở database. Tiền tính bằng `BigDecimal`, giữ nguyên đơn vị giá trong database.

Khóa ngoại không cascade xóa lịch sử. Admin xóa sách đã bán nhận thông báo nghiệp vụ; có thể sửa tồn kho về 0 để ngừng bán.

## 4. Đường dẫn chính

Các URL dưới đây được nối với context `/GK_laptrinh_web_24133049`.

| Chức năng | Đường dẫn |
|---|---|
| Danh sách sách | `/`, `/home`, `/books` |
| Chi tiết sách | `/book-detail?id={bookId}` |
| Gửi đánh giá | POST `/review/add` |
| Đăng ký / OTP | `/register`, `/verify-otp`, `/resend-otp` |
| Đăng nhập / đăng xuất | `/login`, `/logout` |
| Giỏ hàng | GET `/cart`; POST `/cart` để thêm, sửa, xóa |
| Checkout COD | GET `/checkout`; POST `/checkout` để xác nhận |
| Đặt hàng thành công | `/order-success?id={orderId}` |
| Chi tiết đơn | `/order?id={orderId}` |
| Quản lý sách Admin | `/admin/books` |
| Thêm / sửa / xóa sách Admin | `/admin/books/add`, `/admin/books/edit?id={bookId}`, `/admin/books/delete` |

## 5. Kịch bản demo và kiểm tra thủ công

**Luồng demo:** Đăng nhập User → chọn sách và số lượng → **Thêm vào giỏ** → sửa số lượng rồi **Lưu** → xóa một sách → **Đặt hàng COD** → nhập giao hàng → **Xác nhận đặt hàng COD** → xem trang thành công → **Xem chi tiết đơn**.

| Thử nghiệm | Kết quả mong đợi |
|---|---|
| Thêm một sách, thêm lại, thêm sách khác | Sách trùng cộng dồn một dòng; badge và tổng tiền đúng |
| Sửa ô số lượng hoặc dùng ± rồi Lưu | Thành tiền và tổng tiền cập nhật theo số lượng mới |
| Nhập 0, âm, thập phân hoặc vượt kho | Bị chặn, giỏ không bị thay đổi bởi dữ liệu sai |
| Xóa một sách; xóa hết và chọn Hủy / xác nhận | Xóa đúng phạm vi; Hủy giữ giỏ; xác nhận làm giỏ trống |
| Admin giảm kho thấp hơn số lượng đã thêm | Giỏ báo lỗi, giữ số lượng và chặn checkout |
| Checkout giỏ trống | Quay về giỏ và yêu cầu thêm sách |
| Thiếu địa chỉ hoặc điện thoại sai | Báo lỗi và giữ thông tin đã nhập |
| COD hợp lệ | Có mã đơn, NEW/UNPAID; giỏ trống, kho giảm đúng |
| Tải lại trang thành công hoặc gửi lại POST | Không tạo đơn hay trừ kho thêm |
| User khác mở URL của đơn vừa tạo | 404 |
| Admin xóa sách đã có đơn | Thông báo không thể xóa; lịch sử và đánh giá được giữ |

Để thử giảm kho, dùng Admin ở trình duyệt khác hoặc cửa sổ riêng tư để giữ nguyên session User. Đơn tạo bằng thao tác thủ công được lưu thật và trừ kho; chỉ dùng dữ liệu dành cho demo.

Trong SSMS, chọn database `BookStore_24133049` để đối chiếu:

```sql
SELECT TOP (5) order_id, order_code, user_id, total_amount,
       payment_method, order_status, payment_status, created_at
FROM dbo.orders
ORDER BY order_id DESC;

-- Thay 123 bằng order_id vừa tạo.
SELECT book_id, book_title, unit_price, quantity, line_total
FROM dbo.order_items
WHERE order_id = 123;
```

## 6. Kiểm thử tự động

### Test logic, không cần SQL Server hoặc Tomcat

```powershell
Remove-Item Env:RUN_SQLSERVER_TESTS -ErrorAction SilentlyContinue
Remove-Item Env:RUN_HTTP_TESTS -ErrorAction SilentlyContinue
mvn test
```

Kết quả mong đợi: **7 test logic PASS; 10 test tích hợp SKIPPED**. SKIPPED không có nghĩa là phần tích hợp đã được kiểm chứng trong lượt chạy này.

### Test đầy đủ với SQL Server và Tomcat

Chạy migration, build và khởi động Tomcat theo phần 1 trước. Sau đó:

```powershell
$env:RUN_SQLSERVER_TESTS = 'true'
$env:RUN_HTTP_TESTS = 'true'
mvn test '-DargLine=-Djava.library.path=D:\apache-tomcat-11.0.25-windows-x64\apache-tomcat-11.0.25\bin --enable-native-access=ALL-UNNAMED'
```

Kết quả mong đợi: **17 test, 0 failures, 0 errors, 0 skipped**.

| Bộ test | Số test | Phạm vi |
|---|---:|---|
| `CartServiceTest_24133049` | 4 | Số lượng, cộng dồn, tồn kho, tính tiền và xóa giỏ |
| `OrderServiceTest_24133049` | 3 | Giao hàng, token checkout, giữ giỏ khi lỗi và retry |
| `OrderTransactionTest_24133049` | 6 | Lưu COD, snapshot, quyền sở hữu, cạnh tranh tồn kho, chống trùng và rollback |
| `CommerceHttpTest_24133049` | 4 | Luồng HTTP, quyền, CSRF, form, UTF-8, COD và hồi quy chức năng cũ |

Để chạy riêng, thêm `-Dtest=OrderTransactionTest_24133049` hoặc `-Dtest=CommerceHttpTest_24133049` vào lệnh. Khi dùng Tomcat khác, HTTP test nhận URL qua `-Dtest.baseUrl=http://localhost:8080/GK_laptrinh_web_24133049`.

Test tích hợp tạo tài khoản/sách riêng với tiền tố `bt11-` và dọn theo ID trong từng fixture. Test rollback cố tình gây lỗi ở INSERT chi tiết thứ hai, sau khi đã trừ kho sách thứ nhất. Báo cáo mỗi lượt chạy nằm ở `target/surefire-reports/` và có thể bị ghi đè ở lượt sau hoặc xóa bởi `mvn clean`.

**Kết quả đã ghi nhận:** lượt build tích hợp trên bản source đã lọc để nộp ngày 05/10/2026 lúc 09:27:30 có 17/17 test PASS, không test bị skip. Sau sửa UTF-8, lượt HTTP ngày 05/10/2026 lúc 09:14:30 có 4/4 test PASS, gồm kiểm tra nút tiếng Việt trên danh sách và chi tiết. Xem [biên bản kiểm thử](tests/BT11_TEST_RESULTS.md) để biết phạm vi và giới hạn.

Chưa kiểm chứng gửi OTP thật qua SMTP, giao diện responsive/nút JavaScript bằng kiểm thử trình duyệt đồ họa, hoặc runtime đúng JDK 17. Môi trường thực tế đã chạy là Tomcat 11 với JDK 26, biên dịch `release=17`.

## 7. Xử lý lỗi thường gặp

| Hiện tượng | Cách xử lý |
|---|---|
| Maven không xóa được `target/tomcat-test/logs/stdout.log` | Chạy `./tests/stop-test-tomcat.ps1`, rồi build lại. Script mới lưu runtime ngoài `target`. |
| Script báo WAR missing | Chạy `mvn clean package`, chỉ khởi động khi có BUILD SUCCESS. |
| Script báo Tomcat đang chạy hoặc cổng bị dùng | Dừng bằng script stop trước khi khởi động lại; không dừng toàn bộ tiến trình Java. |
| Windows PowerShell báo ParserError với chữ tiếng Việt | Dùng bản script hiện tại trong `tests/`; script đã chuyển sang ASCII và kiểm tra trên PowerShell 5.1. |
| Nút Thêm vào giỏ bị lỗi chữ | Build/deploy bản mới rồi Ctrl + F5. Fragment `cart-add.jspf` đã khai báo UTF-8. |
| JDBC báo thiếu driver hoặc native DLL | Kiểm tra JAR trong Tomcat/lib, DLL đúng phiên bản/kiến trúc và `java.library.path`. |
| Windows Authentication thất bại | Chạy bằng tài khoản Windows có quyền SQL Server; kiểm tra instance SQLEXPRESS đang hoạt động. |
| Checkout báo chưa cài migration | Chạy migration bổ sung orders/order_items trong phần 1, không chạy database.sql. |
| Test tích hợp bị SKIPPED | Kiểm tra biến RUN_SQLSERVER_TESTS/RUN_HTTP_TESTS trong chính terminal chạy Maven. |

Log Tomcat kiểm thử: `.runtime/tomcat-test/logs/stdout.log` và `stderr.log`.

## 8. Cấu trúc project

```text
pom.xml
src/main/java/vn/edu/hcmute/exam/
    config/       Kết nối database, SiteMesh
    controller/   Servlet User và Admin
    service/      Nghiệp vụ tài khoản, sách, giỏ hàng, đơn hàng
    dao/          Truy vấn và transaction JDBC
    model/        User, Book, Cart, Order, Shipping
    filter/       Encoding, quyền Admin, session giỏ hàng
    util/         Mật khẩu, email, CSRF và helper request
src/main/webapp/WEB-INF/
    views/        JSP chức năng
    decorators/   Layout User và Admin
src/main/resources/
    mail.properties.example
src/test/java/    Test logic, SQL Server, HTTP và fixture dữ liệu
database/migrations/BT11_cart_cod.sql
tests/           Script start/stop Tomcat, biên bản kiểm thử
target/          WAR và báo cáo build/test được sinh tự động
.runtime/        Tomcat kiểm thử được sinh tự động
```

## 9. Git và hạn nộp

Hạn commit giỏ hàng và COD: **trước 10:45 ngày 05/10/2026, Asia/Ho_Chi_Minh**.

Source đã được lọc để nộp tại [BT11 trên GitHub](https://github.com/trongsonho/BT11_laptrinh_web/tree/main/BT11), trên branch `main`. Hai commit chia theo giỏ hàng và COD, dùng thời gian commit thực tế:

1. `feat(user): implement shopping cart with stock validation`
2. `feat(user): implement COD checkout with atomic order persistence`

Chỉ stage source, migration, test và tài liệu liên quan. `target/` và `.runtime/` đã được ignore. Không commit mật khẩu cấu hình hoặc DOCX/PDF/ZIP giữa kỳ không thuộc thay đổi này; không sửa thời gian commit. File `mail.properties` chứa cấu hình riêng được loại khỏi bản nộp; dùng biến môi trường SMTP hoặc tạo file cục bộ từ bản example khi cần.
