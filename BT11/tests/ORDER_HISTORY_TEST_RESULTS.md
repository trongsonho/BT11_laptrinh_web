# Kiểm thử lịch sử đặt hàng và lọc trạng thái

Sinh viên: Hồ Trọng Sơn – 24133049. Lượt build cuối: **06/10/2026 21:46:58, UTC+07**.

## Kết quả thực tế

- Build bản chuẩn bị commit tại `.runtime/github-structure/BT11`: **BUILD SUCCESS**, **26 tests, 0 failures, 0 errors, 0 skipped**.
- Maven 3.9.16, JDK 26.0.2.1 biên dịch `release=17`, Tomcat 11.0.25 trên cổng 18080, SQL Server SQLEXPRESS với Windows Authentication hiện có.
- Migration trạng thái đã áp dụng và chạy lại thành công. CHECK tiền/COD/thanh toán còn được bật và trusted; không chạy database.sql, không đổi trạng thái đơn thật.
- Đọc schema trước khi sửa: dbo.orders/order_items; order_status và payment_status riêng; không có updated_at. Giữ NEW, SHIPPED, COMPLETED; PENDING/SHIPPING/DELIVERED là alias. Mapping tập trung OrderStatus_24133049.

## Phạm vi

| Bộ test | Số test | Kết quả |
|---|---:|---|
| CartServiceTest_24133049 | 4 | PASS hồi quy số lượng, tồn kho, giỏ và BigDecimal |
| OrderServiceTest_24133049 | 3 | PASS hồi quy checkout/token/giao hàng |
| OrderTransactionTest_24133049 | 6 | PASS hồi quy transaction, snapshot, quyền sở hữu, mua cạnh tranh, rollback, chống trùng |
| OrderHistoryServiceTest_24133049 | 3 | PASS 8 nhãn, alias, trạng thái không rõ, allowlist, phân biệt vận chuyển/giao hàng, trang sai/tràn số và clamp |
| OrderHistoryDatabaseTest_24133049 | 3 | PASS COUNT/list theo chủ đơn, sort khi trùng thời gian, phân trang, từng bộ lọc và alias; SQL thay trạng thái đọc mới; tiền/kho/thanh toán/snapshot giữ nguyên; chạy toàn file demo không UPDATE; chạy lần lượt 8 khối demo trên fixture thành công; mã sai bị CHECK từ chối |
| CommerceHttpTest_24133049 | 7 | PASS các luồng COD cũ và lịch sử GET: Guest/Admin/inactive, userId giả bị bỏ qua, User khác không xem được đơn; giỏ trống lịch sử và lọc không kết quả; 10 đơn/trang, trang 2 và cuối, giữ filter và reset trang 1 trong form; tham số sai; SQL refresh danh sách/chi tiết và rời bộ lọc cũ; COD mới xuất hiện; SiteMesh và UTF-8 |

## Dữ liệu kiểm thử

Fixture tạo tài khoản/sách/đơn riêng, dọn theo ID cụ thể trong AfterEach. Những UPDATE trong test và các khối demo chỉ chạy trên ID đơn fixture. Khôi phục NEW khi thử đủ các trạng thái rồi cleanup. Không thay đổi trạng thái, tiền hoặc tồn kho của đơn/sách thật. Báo cáo từng bộ test nằm trong target/surefire-reports của bản build; target không được commit.

## Giới hạn

Chưa kiểm tra trực quan bằng trình duyệt đồ họa hoặc runtime đúng JDK 17. HTTP kiểm tra form/bộ lọc/phân trang thực tế trên Tomcat; code được build theo API/bytecode Java 17. Đổi trạng thái SQL chỉ minh họa hiển thị/lọc, không có nghiệp vụ hủy/hoàn hàng, hoàn kho, hoàn tiền hoặc xác nhận thu tiền.

## Git

Đã kiểm thử và commit trên branch main của repository trong `.runtime/github-structure`. Thông điệp: `feat(user): add order history with status filters`. Việc push được thực hiện khi có yêu cầu của chủ project. Source gốc ngoài repository cũng đã được cập nhật để ứng dụng đang chạy dùng cùng implementation; chỉ file thuộc lịch sử được đồng bộ và stage.
