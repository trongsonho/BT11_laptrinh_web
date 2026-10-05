# Kết quả kiểm thử BT11 – Hồ Trọng Sơn, 24133049

Lần build tích hợp trên bản source đã lọc để nộp: **05/10/2026 09:27:30, UTC+07 (Asia/Ho_Chi_Minh)**. Kết quả: **17/17 PASS, không test bị skip**. Java/JSP được đối chiếu hash và khớp với workspace; cấu hình email riêng đã được loại bỏ.

## Môi trường và kết quả thực tế

- Maven 3.9.16; JDK 26.0.2.1 biên dịch `--release 17`; Tomcat 11.0.25 tại cổng 18080.
- SQL Server `SQLEXPRESS`, database hiện có `BookStore_24133049`, Windows Authentication theo DBContext gốc. Sandbox không lấy được Windows credentials; các test tích hợp chạy ngoài sandbox với quyền đã được tự động phê duyệt.
- Migration `database/migrations/BT11_cart_cod.sql` đã áp dụng và chạy lại thành công. Không chạy `database.sql`.
- `mvn -o package` với `RUN_SQLSERVER_TESTS=true`, `RUN_HTTP_TESTS=true` và native DLL của JDBC: **BUILD SUCCESS**, **17 tests, 0 failures, 0 errors, 0 skipped**.
- Báo cáo gốc: `target/surefire-reports/TEST-*.xml` và các file `.txt` tương ứng. WAR: `target/GK_laptrinh_web_24133049.war`.

## Phạm vi đã chạy

| Bộ test | Số test | Kết quả và phạm vi |
|---|---:|---|
| CartServiceTest_24133049 | 4 | PASS: chuỗi số lượng sai/tràn số, cộng dồn và vượt kho, tồn kho giảm không tự sửa giỏ, nhiều dòng/xóa/xóa hết, tiền BigDecimal chính xác, sách mất hoặc giá không hợp lệ |
| OrderServiceTest_24133049 | 3 | PASS: trường giao hàng và giới hạn độ dài/điện thoại, giữ giỏ/token khi DAO lỗi và retry chỉ một đơn, token giả hoặc giỏ thay đổi bị từ chối |
| OrderTransactionTest_24133049 | 6 | PASS trên SQL Server thật: đơn COD NEW/UNPAID và chi tiết/tổng/kho đúng; snapshot giữ giá cũ; chỉ chủ đơn xem; chặn Admin xóa sách bán và rollback liên kết; giỏ trống/sách mất/hết kho/Admin/User inactive; tồn kho giảm; lỗi INSERT chi tiết thứ hai rollback cả đơn/chi tiết/kho và giữ giỏ; hai User tranh bản cuối chỉ một thành công; hai transaction cùng token về một đơn |
| CommerceHttpTest_24133049 | 4 | PASS trên Tomcat: thêm/cộng nhiều sách, sửa số lượng và dữ liệu POST sai, xóa từng dòng/xóa hết có xác nhận, checkout giỏ trống; Guest/Admin/inactive, CSRF giả/mất và GET không đổi giỏ, đổi tài khoản/logout; thông tin giao hàng lỗi được giữ và escape, tồn kho giảm/sách bị xóa; double click/replay/refresh không trùng đơn, COD thành công xóa giỏ, User khác nhận 404; SiteMesh User, đánh giá thực tế và escape, Admin xóa sách bán có thông báo và không mất đánh giá, trang danh sách/phân trang/chi tiết/register/OTP trả 200 |

## Dữ liệu

Các test chỉ tạo dữ liệu riêng qua `CommerceFixture_24133049`; cleanup theo ID được ghi lại trong từng fixture. Không thay đổi số lượng hoặc giá sách mẫu. Kiểm tra sau các lượt chạy: còn **17 sách gốc**, **0 User test** có email `bt11-%@test.invalid`, **0 đơn test**. IDENTITY có thể có khoảng trống sau cleanup/rollback; đây là hành vi SQL Server bình thường.

## Phần chưa kiểm chứng

- Chưa thao tác bằng trình duyệt đồ họa để kiểm tra trực quan responsive, nút ± JavaScript và hộp xác nhận. HTTP test kiểm tra form, nội dung và xử lý server cho các thao tác tương ứng.
- Chưa gửi email OTP thật hoặc hoàn tất chu trình đăng ký qua SMTP; chỉ smoke-test trang register/verify-otp và kiểm tra đăng nhập/đánh giá thực tế bằng tài khoản test.
- Chưa chạy runtime bằng đúng JDK 17; đã biên dịch theo API/bytecode Java 17 và chạy Tomcat với JDK 26 hiện có.
- Chưa deploy vào Tomcat sản xuất; đã deploy và kiểm thử trên Tomcat riêng (ban đầu `target/tomcat-test`, hiện `.runtime/tomcat-test`).

## Sửa lỗi chạy thử trên Windows PowerShell

Sau phản hồi của người dùng ngày 05/10/2026, đã sửa script khởi động thành source ASCII tương thích Windows PowerShell 5.1, thêm script dừng theo đúng command line Tomcat của project và chuyển runtime sang `.runtime/tomcat-test` (ngoài `target`). Đã kiểm tra parser của cả hai script bằng `powershell.exe`, dừng thành công process cũ giữ log và chạy `mvn -o clean package` thành công (7 test logic PASS, 10 test tích hợp SKIPPED trong lượt build này). Đã khởi động lại Tomcat bằng script mới qua Windows PowerShell 5.1.

## Git (bản nộp)

Source đã được lọc vào thư mục BT11 của repository https://github.com/trongsonho/BT11_laptrinh_web trên branch main, với hai commit giỏ hàng và COD theo thời gian thực tế. Không đưa target/runtime, mail.properties, tài liệu giữa kỳ hoặc script cá nhân vào bản nộp. Thư mục BT3 nộp lại giữ nguyên.
