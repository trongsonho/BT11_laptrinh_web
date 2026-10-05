# Bài tập 03 Lập trình Web

Ứng dụng Jakarta Servlet/JSP, JPA Hibernate và MySQL, kế thừa CRUD danh mục của bài 02. Thư mục này chứa source được chọn lọc để nộp riêng bài 03.

## Chức năng

- Đăng ký, gửi OTP qua email và kích hoạt tài khoản; OTP có thời hạn 5 phút.
- Đăng nhập, đăng xuất; tài khoản chưa kích hoạt không đăng nhập được.
- Quên mật khẩu, gửi OTP qua email và đặt lại mật khẩu.
- Products liên kết nhiều-một với Category bằng JPA và khóa ngoại `cate_id`.
- Admin thêm, sửa, xóa và xem sản phẩm; quản lý danh mục.
- Trang chủ hiển thị tối đa 10 sản phẩm mới nhất.
- `/product` phân trang 6 sản phẩm/trang.
- Xem chi tiết khi chọn sản phẩm trên trang chủ hoặc danh sách.

## Cấu trúc

`src/main/java`: config, entity, DAO, service, controller, filter và util.

`src/main/webapp`: các JSP được sử dụng và cấu hình web.xml.

`src/main/resources`: persistence.xml và mẫu cấu hình email.

`setup_bt3.sql`: tạo database riêng `bt03_jpa`, các bảng và dữ liệu mẫu từ dump gốc bài 03. Chỉ import một lần khi database này chưa tồn tại. Database có 10 sản phẩm.

## Chuẩn bị database

Cần JDK 17 trở lên, Maven, MySQL 8 trở lên và Tomcat 10.1 hoặc 11. Clone repository rồi mở PowerShell trong thư mục này.

```powershell
& "D:\MySQL\bin\mysql.exe" -u root -p --default-character-set=utf8mb4
```

Nhập mật khẩu MySQL. Khi mở client từ thư mục bài 03, tại dấu nhắc `mysql>` chạy:

```sql
source setup_bt3.sql;
exit;
```

Cũng có thể mở `setup_bt3.sql` bằng MySQL Workbench và thực thi khi chưa có database `bt03_jpa`. Nếu máy đã được thiết lập database bài 03, bỏ qua bước import. Không import vào database `categorycrud` của các bài khác vì có thể khác cấu trúc cột.

## Build và chạy bằng PowerShell

Dừng Tomcat cũ bằng Ctrl+C trước khi chạy. Sửa đường dẫn JDK/Tomcat nếu máy của bạn cài nơi khác.

```powershell
mvn clean package
if ($LASTEXITCODE -ne 0) { throw "Build thất bại" }

$env:JAVA_HOME = "C:\Program Files\Java\jdk-24"
$env:JRE_HOME = $env:JAVA_HOME
$env:CATALINA_HOME = "D:\apache-tomcat-11.0.25-windows-x64\apache-tomcat-11.0.25"
$env:CATALINA_BASE = $env:CATALINA_HOME
$env:DB_URL = "jdbc:mysql://localhost:3306/bt03_jpa?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
$env:DB_USERNAME = "root"
$dbCredential = Get-Credential -UserName root -Message "Nhập mật khẩu MySQL"
$env:DB_PASSWORD = $dbCredential.GetNetworkCredential().Password

Copy-Item .\target\bt02_laptrinh_web.war "$env:CATALINA_BASE\webapps\" -Force
try {
    & "$env:CATALINA_HOME\bin\catalina.bat" run
} finally {
    Remove-Item Env:DB_PASSWORD -ErrorAction SilentlyContinue
}
```

Mật khẩu database không được lưu trong source. JpaConfig nhận DB_URL, DB_USERNAME và DB_PASSWORD từ Java system properties hoặc biến môi trường (system properties được ưu tiên). Nếu không cấu hình, giá trị mặc định lấy từ persistence.xml, với mật khẩu rỗng. Trong Eclipse, có thể đặt các biến này tại Run Configurations → Environment của Tomcat.

Mở `http://localhost:8080/bt02_laptrinh_web/`. Nhấn Ctrl+C để dừng server.

| Chức năng | URL sau context path `/bt02_laptrinh_web` |
| --- | --- |
| Đăng ký / kích hoạt | `/register`, `/verify` |
| Đăng nhập / đăng xuất | `/login`, `/logout` |
| Quên / đặt lại mật khẩu | `/forgot-password`, `/reset-password` |
| Danh sách / phân trang | `/product`, `/product?page=2` |
| Chi tiết | `/product/detail?id=1` |
| Quản trị sản phẩm | `/admin/products` |
| Quản trị danh mục | `/admin/category/list` |

Tài khoản thử nghiệm: `admin / 123456` (ADMIN), `user / 123456` (USER). Tài khoản mới phải xác minh OTP.

## Cấu hình gửi OTP

Nếu chưa có file cấu hình riêng, chạy:

```powershell
Copy-Item .\src\main\resources\mail.properties.example .\src\main\resources\mail.properties
notepad .\src\main\resources\mail.properties
```

Điền SMTP host, port, username, password và from theo tài khoản gửi thư. Với Gmail dùng mật khẩu ứng dụng. File mail.properties đã được bỏ qua bởi Git. Sau khi cấu hình, build và deploy lại. Có thể thay bằng biến môi trường MAIL_HOST, MAIL_PORT, MAIL_USERNAME, MAIL_PASSWORD và MAIL_FROM.

## Ảnh sản phẩm

Ảnh upload được lưu tại `D:/uploads` theo Constant.DIR. Các ảnh trong dump gốc không nằm trong repository; chép ảnh cũ vào vị trí này hoặc upload lại qua trang quản trị. Có thể sửa Constant.DIR rồi build lại cho máy khác.

## Nguồn và kiểm tra

Source dựa trên [commit f815873 của repository bài 02](https://github.com/trongsonho/BT02_laptrinh_web/commit/f815873a900743393c61cb37242d937dfbc0517c), ngày 07/09/2026 lúc 00:23:58 (+07:00), trước các phần bài 04. `LICH_SU_COMMIT_GOC.txt` ghi lại lịch sử gốc để đối chiếu; bản tách được commit ở thời điểm hiện tại.

Bản tách sửa truy vấn ProductDaoImpl để tải category trước khi đóng EntityManager và dùng database riêng. Các demo Cookie/Session cũ, JSP trùng không được servlet sử dụng, model cũ và DBConnection không sử dụng đã được loại bỏ. Không đưa target, WAR, ZIP hay mật khẩu riêng lên GitHub.

Đã kiểm tra bản bài 03 với MySQL/Tomcat: trang chủ, danh sách, trang 2, chi tiết và đăng nhập admin trả HTTP 200; trang 1 có 6 sản phẩm, trang 2 có 4. Đã kiểm tra trang chi tiết và quản trị với sản phẩm có category. Chưa kiểm thử toàn bộ CRUD và gửi OTP qua SMTP thực tế.

Nếu gặp `Unknown column p1_0.id`, kiểm tra đã kết nối `bt03_jpa` và deploy đúng WAR mới. Database cũ có `productId` thay vì `id` sẽ không khớp entity này.
