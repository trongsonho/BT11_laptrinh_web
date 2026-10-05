-- =======================================================
-- ĐỒ ÁN LẬP TRÌNH WEB - ĐỀ SỐ 02
-- SINH VIÊN: HỒ TRỌNG SƠN - MSSV: 24133049 - MÃ ĐỀ: 02
-- HỆ QUẢN TRỊ CSDL: MICROSOFT SQL SERVER
-- =======================================================

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'BookStore_24133049')
BEGIN
    CREATE DATABASE BookStore_24133049;
END
GO

USE BookStore_24133049;
GO

-- Xóa các bảng cũ nếu đã tồn tại theo thứ tự khóa ngoại
IF OBJECT_ID('dbo.rating', 'U') IS NOT NULL DROP TABLE dbo.rating;
IF OBJECT_ID('dbo.book_author', 'U') IS NOT NULL DROP TABLE dbo.book_author;
IF OBJECT_ID('dbo.books', 'U') IS NOT NULL DROP TABLE dbo.books;
IF OBJECT_ID('dbo.users', 'U') IS NOT NULL DROP TABLE dbo.users;
IF OBJECT_ID('dbo.author', 'U') IS NOT NULL DROP TABLE dbo.author;
GO

-- 1. BẢNG AUTHOR (Tác giả)
CREATE TABLE dbo.author (
    author_id INT IDENTITY(1,1) PRIMARY KEY,
    author_name NVARCHAR(100) NOT NULL,
    date_of_birth DATE NULL
);
GO

-- 2. BẢNG USERS (Người dùng)
-- Ghi chú tinh chỉnh:
-- - 'phone' trong đề thi gốc là int, được chuyển sang varchar(15) để lưu chính xác số 0 đầu và tránh tràn số.
-- - 'passwd' trong đề thi gốc là varchar(32) (chỉ đủ cho MD5), được mở rộng thành varchar(255) để lưu hash an toàn (SHA-256 + Salt).
-- - Bổ sung các trường is_active, otp_code, otp_expiry, otp_attempts để phục vụ yêu cầu kích hoạt OTP và kiểm soát số lần thử sai ở Câu 2.
CREATE TABLE dbo.users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    fullname NVARCHAR(50) NOT NULL,
    phone VARCHAR(15) NULL,
    passwd VARCHAR(255) NOT NULL,
    signup_date DATETIME DEFAULT GETDATE(),
    last_login DATETIME NULL,
    is_admin BIT DEFAULT 0,
    is_active BIT DEFAULT 0,
    otp_code VARCHAR(10) NULL,
    otp_expiry DATETIME NULL,
    otp_attempts INT DEFAULT 0
);
GO

-- 3. BẢNG BOOKS (Sách)
CREATE TABLE dbo.books (
    bookid INT IDENTITY(1,1) PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL,
    title NVARCHAR(200) NOT NULL,
    publisher NVARCHAR(100) NULL,
    price DECIMAL(6, 2) NULL,
    description NVARCHAR(MAX) NULL,
    publish_date DATE NULL,
    cover_image VARCHAR(255) NULL,
    quantity INT DEFAULT 0
);
GO

-- 4. BẢNG BOOK_AUTHOR (Liên kết Sách - Tác giả)
-- Khóa chính phức hợp (bookid, author_id). Các cột không tự tăng, tham chiếu tới books và author.
CREATE TABLE dbo.book_author (
    bookid INT NOT NULL,
    author_id INT NOT NULL,
    CONSTRAINT PK_book_author PRIMARY KEY (bookid, author_id),
    CONSTRAINT FK_book_author_books FOREIGN KEY (bookid) REFERENCES dbo.books(bookid) ON DELETE CASCADE,
    CONSTRAINT FK_book_author_author FOREIGN KEY (author_id) REFERENCES dbo.author(author_id) ON DELETE CASCADE
);
GO

-- 5. BẢNG RATING (Đánh giá sách của người dùng)
-- Khóa chính phức hợp (userid, bookid). Các cột không tự tăng, tham chiếu tới users và books.
CREATE TABLE dbo.rating (
    userid INT NOT NULL,
    bookid INT NOT NULL,
    rating TINYINT CHECK (rating >= 1 AND rating <= 5),
    review_text NVARCHAR(MAX) NULL,
    review_date DATETIME DEFAULT GETDATE(),
    CONSTRAINT PK_rating PRIMARY KEY (userid, bookid),
    CONSTRAINT FK_rating_users FOREIGN KEY (userid) REFERENCES dbo.users(id) ON DELETE CASCADE,
    CONSTRAINT FK_rating_books FOREIGN KEY (bookid) REFERENCES dbo.books(bookid) ON DELETE CASCADE
);
GO

-- =======================================================
-- SEED DATA (DỮ LIỆU MẪU KIỂM THỬ)
-- =======================================================

-- Thêm Tác giả (Ít nhất 4 tác giả)
SET IDENTITY_INSERT dbo.author ON;
INSERT INTO dbo.author (author_id, author_name, date_of_birth) VALUES
(1, N'Nguyễn Nhật Ánh', '1955-05-07'),
(2, N'Haruki Murakami', '1949-01-12'),
(3, N'J.K. Rowling', '1965-07-31'),
(4, N'Dale Carnegie', '1888-11-24');
SET IDENTITY_INSERT dbo.author OFF;
GO

-- Thêm Người dùng (Admin và User đã kích hoạt + hash mật khẩu)
-- Mật khẩu mặc định là '123456' được hash SHA-256: e10adc3949ba59abbe56e057f20f883e (hoặc SHA-256 dạng standard)
-- SHA-256('123456') = '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'
SET IDENTITY_INSERT dbo.users ON;
INSERT INTO dbo.users (id, email, fullname, phone, passwd, signup_date, last_login, is_admin, is_active, otp_code, otp_expiry, otp_attempts) VALUES
(1, 'admin@bookstore.vn', N'Quản Trị Viên (Hồ Trọng Sơn)', '0901234567', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', GETDATE(), NULL, 1, 1, NULL, NULL, 0),
(2, 'user1@gmail.com', N'Nguyễn Văn A', '0912345678', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', GETDATE(), NULL, 0, 1, NULL, NULL, 0),
(3, 'user2@gmail.com', N'Trần Thị B', '0987654321', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', GETDATE(), NULL, 0, 1, NULL, NULL, 0),
(4, 'user3@gmail.com', N'Lê Hoàng C', '0933445566', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', GETDATE(), NULL, 0, 1, NULL, NULL, 0);
SET IDENTITY_INSERT dbo.users OFF;
GO

-- Thêm Sách: Tác giả Nguyễn Nhật Ánh (author_id = 1) có 10 cuốn sách để phân trang 3 cuốn/trang qua 4 trang!
SET IDENTITY_INSERT dbo.books ON;
INSERT INTO dbo.books (bookid, isbn, title, publisher, price, description, publish_date, cover_image, quantity) VALUES
(1, '9786041001', N'Mắt Biếc', N'NXB Trẻ', 110.00, N'Tác phẩm kinh điển về tình yêu thuở học trò và làng Đo Đo.', '2019-03-15', 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400', 50),
(2, '9786041002', N'Cho Tôi Xin Một Vé Đi Tuổi Thơ', N'NXB Trẻ', 95.00, N'Cuốn sách đưa người đọc trở về những ký ức ngọt ngào trong veo.', '2018-06-20', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400', 40),
(3, '9786041003', N'Cô Gái Đến Từ Hôm Qua', N'NXB Trẻ', 85.00, N'Câu chuyện tình yêu học trò đầy mơ mộng và hoài niệm tuổi thanh xuân.', '2017-09-10', 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=400', 35),
(4, '9786041004', N'Tôi Thấy Hoa Vàng Trên Cỏ Xanh', N'NXB Trẻ', 125.00, N'Bức tranh làng quê bình dị với tình anh em ruột thịt cảm động.', '2020-01-05', 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=400', 60),
(5, '9786041005', N'Ngồi Khóc Trên Cây', N'NXB Trẻ', 105.00, N'Câu chuyện cảm động giữa chàng sinh viên Đông và cô bé Rùa hồn nhiên.', '2016-08-12', 'https://images.unsplash.com/photo-1495640388908-05fa85288e61?w=400', 25),
(6, '9786041006', N'Kính Vạn Hoa', N'NXB Kim Đồng', 150.00, N'Những chuyến phiêu lưu kỳ thú của bộ ba Quý ròm, Tiểu Long, nhỏ Hạnh.', '2015-11-20', 'https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=400', 80),
(7, '9786041007', N'Lá Nằm Trong Tay Phúc', N'NXB Trẻ', 90.00, N'Hành trình vượt qua thử thách và tìm lại niềm tin trong cuộc sống.', '2018-04-18', 'https://images.unsplash.com/photo-1476275466078-4007374efbbe?w=400', 20),
(8, '9786041008', N'Bàn Có Năm Chỗ Ngồi', N'NXB Trẻ', 70.00, N'Tình bạn bè thân thiết của 5 bạn học sinh cùng chung một bàn học.', '2014-02-14', 'https://images.unsplash.com/photo-1524578271613-d550eacf6090?w=400', 30),
(9, '9786041009', N'Cây Chuối Non Đi Giày Xanh', N'NXB Trẻ', 115.00, N'Kỷ niệm tuổi thơ gắn bó tại miền quê thân thương xứ Quảng.', '2017-12-25', 'https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=400', 45),
(10, '9786041010', N'Ngày Xưa Có Một Chuyện Tình', N'NXB Trẻ', 130.00, N'Bản tình ca đầy trăn trở và tình bạn chân thành sâu sắc.', '2016-09-18', 'https://images.unsplash.com/photo-1532012164546-f432f2e3777a?w=400', 55),
-- Sách của Tác giả Haruki Murakami (author_id = 2)
(11, '9786041011', N'Rừng Na Uy (Norwegian Wood)', N'NXB Hội Nhà Văn', 140.00, N'Kiệt tác văn học Nhật Bản hiện đại về nỗi cô đơn và tình yêu tuổi trẻ.', '2018-05-10', 'https://images.unsplash.com/photo-1491841550275-ad7854e35ca6?w=400', 30),
(12, '9786041012', N'Biên Niên Ký Chim Vặn Dây Cót', N'NXB Hội Nhà Văn', 180.00, N'Hành trình tìm kiếm kỳ ảo trong thế giới siêu thực của Murakami.', '2019-10-15', 'https://images.unsplash.com/photo-1519682337058-a94d519337bc?w=400', 20),
(13, '9786041013', N'Kafka Bên Bờ Biển', N'NXB Hội Nhà Văn', 165.00, N'Huyền ảo hòa quyện hiện thực trong chuyến phiêu lưu của cậu bé 15 tuổi.', '2020-07-22', 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=400', 28),
(14, '9786041014', N'1Q84 - Tập 1', N'NXB Hội Nhà Văn', 190.00, N'Một thế giới song song nơi có hai mặt trăng chiếu sáng bầu trời đêm.', '2021-03-08', 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400', 22),
-- Sách của J.K. Rowling (author_id = 3)
(15, '9786041015', N'Harry Potter và Hòn Đá Phù Thủy', N'NXB Trẻ', 175.00, N'Khởi đầu chuyến phiêu lưu huyền diệu tại ngôi trường phép thuật Hogwarts.', '2017-06-01', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400', 70),
(16, '9786041016', N'Harry Potter và Phòng Chứa Bí Mật', N'NXB Trẻ', 185.00, N'Bí mật đen tối ngủ yên ngàn năm dưới tầng hầm Hogwarts thức giấc.', '2018-06-01', 'https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=400', 65),
-- Sách của Dale Carnegie (author_id = 4)
(17, '9786041017', N'Đắc Nhân Tâm', N'NXB Tổng Hợp TP.HCM', 98.00, N'Nghệ thuật thu phục lòng người và giao tiếp ứng xử đỉnh cao.', '2021-01-10', 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=400', 100);
SET IDENTITY_INSERT dbo.books OFF;
GO

-- Liên kết Sách - Tác giả
INSERT INTO dbo.book_author (bookid, author_id) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1),
(11, 2),
(12, 2),
(13, 2),
(14, 2),
(15, 3),
(16, 3),
(17, 4);
GO

-- Thêm Đánh giá mẫu (Rating & Review)
INSERT INTO dbo.rating (userid, bookid, rating, review_text, review_date) VALUES
(2, 1, 5, N'Sách rất hay, đọc lại nhiều lần vẫn thấy xúc động nghẹn ngào!', '2026-01-10'),
(3, 1, 5, N'Tình yêu đẹp nhưng buồn nao lòng. Đoạn kết làm tôi rơi nước mắt.', '2026-02-14'),
(4, 1, 4, N'Ngòi bút Nguyễn Nhật Ánh miêu tả làng quê xứ Quảng quá tuyệt vời.', '2026-03-01'),
(2, 2, 5, N'Cuốn sách giúp tôi tìm lại tuổi thơ nghịch ngợm ngây ngô.', '2026-02-20'),
(3, 2, 4, N'Giọng văn hóm hỉnh, sâu sắc.', '2026-03-05'),
(2, 3, 5, N'Nhớ lại mối tình thời cấp ba đầy ngây thơ trong sáng.', '2026-01-25'),
(4, 4, 5, N'Đoạn phim và sách đều cực kỳ xuất sắc.', '2026-02-18'),
(2, 11, 5, N'Một tác phẩm ám ảnh về nỗi cô đơn của tuổi trẻ.', '2026-03-12'),
(3, 11, 4, N'Văn phong Haruki Murakami luôn mang chất riêng cuốn hút.', '2026-03-15'),
(2, 15, 5, N'Tuổi thơ của tôi! Bản dịch của cô Lý Lan tuyệt vời.', '2026-01-05'),
(3, 17, 5, N'Sách gối đầu giường về nghệ thuật sống và giao tiếp.', '2026-02-01');
GO

PRINT N'Khởi tạo Cơ sở dữ liệu BookStore_24133049 thành công!';
