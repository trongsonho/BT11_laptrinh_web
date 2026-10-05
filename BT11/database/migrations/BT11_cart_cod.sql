-- Hồ Trọng Sơn - 24133049. Apply only to the existing BookStore database.
USE [BookStore_24133049];
GO
SET XACT_ABORT ON;
BEGIN TRANSACTION;
IF OBJECT_ID(N'dbo.orders', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.orders (
        order_id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT PK_orders_24133049 PRIMARY KEY,
        order_code VARCHAR(40) NOT NULL,
        user_id INT NOT NULL,
        recipient_name NVARCHAR(100) NOT NULL,
        phone VARCHAR(15) NOT NULL,
        shipping_address NVARCHAR(500) NOT NULL,
        note NVARCHAR(1000) NULL,
        created_at DATETIME2(3) NOT NULL CONSTRAINT DF_orders_created_24133049 DEFAULT SYSDATETIME(),
        total_amount DECIMAL(28,2) NOT NULL,
        payment_method VARCHAR(10) NOT NULL CONSTRAINT DF_orders_method_24133049 DEFAULT 'COD',
        order_status VARCHAR(10) NOT NULL CONSTRAINT DF_orders_status_24133049 DEFAULT 'NEW',
        payment_status VARCHAR(10) NOT NULL CONSTRAINT DF_orders_payment_24133049 DEFAULT 'UNPAID',
        checkout_token VARCHAR(36) NOT NULL,
        CONSTRAINT UQ_orders_code_24133049 UNIQUE (order_code),
        CONSTRAINT UQ_orders_checkout_24133049 UNIQUE (user_id, checkout_token),
        CONSTRAINT FK_orders_user_24133049 FOREIGN KEY (user_id) REFERENCES dbo.users(id),
        CONSTRAINT CK_orders_total_24133049 CHECK (total_amount >= 0),
        CONSTRAINT CK_orders_method_24133049 CHECK (payment_method = 'COD'),
        CONSTRAINT CK_orders_status_24133049 CHECK (order_status IN ('NEW','PENDING','CONFIRMED','SHIPPED','COMPLETED','CANCELLED')),
        CONSTRAINT CK_orders_payment_24133049 CHECK (payment_status IN ('UNPAID','PAID'))
    );
END;
IF OBJECT_ID(N'dbo.order_items', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.order_items (
        order_id BIGINT NOT NULL,
        book_id INT NOT NULL,
        book_title NVARCHAR(200) NOT NULL,
        unit_price DECIMAL(28,2) NOT NULL,
        quantity INT NOT NULL,
        line_total DECIMAL(28,2) NOT NULL,
        CONSTRAINT PK_order_items_24133049 PRIMARY KEY (order_id, book_id),
        CONSTRAINT FK_order_items_order_24133049 FOREIGN KEY (order_id) REFERENCES dbo.orders(order_id),
        CONSTRAINT FK_order_items_book_24133049 FOREIGN KEY (book_id) REFERENCES dbo.books(bookid),
        CONSTRAINT CK_order_items_quantity_24133049 CHECK (quantity > 0),
        CONSTRAINT CK_order_items_price_24133049 CHECK (unit_price >= 0),
        CONSTRAINT CK_order_items_total_24133049 CHECK (line_total = unit_price * quantity)
    );
END;
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.orders') AND name = 'IX_orders_user_created_24133049')
    CREATE INDEX IX_orders_user_created_24133049 ON dbo.orders(user_id, created_at DESC);
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.order_items') AND name = 'IX_order_items_book_24133049')
    CREATE INDEX IX_order_items_book_24133049 ON dbo.order_items(book_id);
COMMIT TRANSACTION;
GO
