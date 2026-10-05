# BT11 – Giỏ hàng BookStore

Sinh viên: **Hồ Trọng Sơn – 24133049**. Project Maven WAR, Java 17, Jakarta Servlet/JSP/JSTL, SQL Server và SiteMesh, chạy Tomcat 11.

## Chức năng giỏ hàng

- User đang hoạt động có thể thêm sách từ danh sách/chi tiết, chọn số lượng và thêm lại để cộng dồn.
- Sửa số lượng bằng ô nhập/nút ± rồi Lưu; xóa từng sách hoặc xóa toàn bộ có xác nhận.
- Giỏ gắn với tài khoản trong HttpSession; đổi tài khoản hoặc đăng xuất không giữ giỏ cũ.
- Server kiểm tra quyền, CSRF, số lượng nguyên dương và tổng sau khi cộng không vượt kho; giá và tồn kho đọc từ database.
- Tiền dùng BigDecimal; thêm giỏ chưa trừ kho. Tồn kho giảm được báo rõ, không tự thay số lượng.

## Chạy thử

Mở PowerShell trong thư mục BT11, chạy `mvn clean package`. Cấu hình JDBC driver provided trong Tomcat/lib và DLL Windows Authentication trong Tomcat/bin, kết nối database BookStore_24133049 hiện có qua DBContext_24133049.

Dùng tests/start-test-tomcat.ps1 với -TomcatHome và -JavaHome; truy cập http://localhost:18080/GK_laptrinh_web_24133049/home. Dừng bằng tests/stop-test-tomcat.ps1.

Đăng nhập tài khoản mẫu nếu còn: user1@gmail.com / 123456. Chạy `mvn test`: 4 test giỏ hàng, không cần SQL Server hoặc Tomcat.

Database.sql là script gốc để tham khảo/khởi tạo trên môi trường trống; có DROP bảng nên không chạy trên database đang dùng. Cấu hình SMTP sử dụng biến môi trường hoặc mail.properties riêng từ bản example, không đưa bí mật vào Git.