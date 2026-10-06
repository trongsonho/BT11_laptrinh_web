-- Create synthetic orders for browsing all eight statuses and pagination.
-- Existing orders, users, books and stock are not updated. Safe to rerun.
-- These orders are display fixtures, not purchases or inventory movements.
USE [BookStore_24133049];
GO
SET XACT_ABORT ON;
DECLARE @UserEmail VARCHAR(50) = 'user1@gmail.com';
DECLARE @Marker NVARCHAR(1000) = N'[BT11_STATUS_DEMO_24133049] Đơn mẫu kiểm tra lịch sử; không phát sinh mua bán hoặc thay đổi tồn kho.';
DECLARE @UserId INT, @Name NVARCHAR(100), @Phone VARCHAR(15);
SELECT @UserId=id,@Name=fullname,@Phone=phone FROM dbo.users
WHERE email=@UserEmail AND is_active=1 AND is_admin=0;
IF @UserId IS NULL THROW 51020, 'Active demo User not found.', 1;

DECLARE @BookId INT, @Title NVARCHAR(200), @Price DECIMAL(28,2);
SELECT TOP (1) @BookId=bookid,@Title=title,@Price=price FROM dbo.books
WHERE price IS NOT NULL AND price>=0 ORDER BY bookid;
IF @BookId IS NULL THROW 51021, 'No book with a valid price for the snapshot.', 1;

DECLARE @Samples TABLE (number INT PRIMARY KEY,status VARCHAR(10));
INSERT INTO @Samples VALUES
 (1,'NEW'),(2,'CONFIRMED'),(3,'PREPARING'),(4,'SHIPPED'),
 (5,'DELIVERING'),(6,'COMPLETED'),(7,'CANCELLED'),(8,'RETURNED'),
 (9,'NEW'),(10,'CONFIRMED'),(11,'PREPARING'),(12,'SHIPPED'),
 (13,'DELIVERING'),(14,'COMPLETED'),(15,'CANCELLED'),(16,'RETURNED');

BEGIN TRY
    BEGIN TRANSACTION;
    DECLARE @Number INT=1,@Code VARCHAR(40),@Status VARCHAR(10),@OrderId BIGINT;
    WHILE @Number<=16
    BEGIN
        SET @Code=CONCAT('DEMO-HISTORY-',@UserId,'-',RIGHT(CONCAT('0',@Number),2));
        SELECT @Status=status FROM @Samples WHERE number=@Number;
        IF NOT EXISTS (SELECT 1 FROM dbo.orders WITH (UPDLOCK,HOLDLOCK) WHERE order_code=@Code)
        BEGIN
            INSERT INTO dbo.orders(order_code,user_id,recipient_name,phone,shipping_address,note,
                created_at,total_amount,payment_method,order_status,payment_status,checkout_token)
            VALUES(@Code,@UserId,@Name,COALESCE(@Phone,'0901234567'),N'Địa chỉ mẫu – chỉ phục vụ kiểm thử giao diện',@Marker,
                DATEADD(MINUTE,-@Number,SYSDATETIME()),@Price,'COD',@Status,'UNPAID',CONVERT(VARCHAR(36),NEWID()));
            SET @OrderId=SCOPE_IDENTITY();
            INSERT INTO dbo.order_items(order_id,book_id,book_title,unit_price,quantity,line_total)
            VALUES(@OrderId,@BookId,@Title,@Price,1,@Price);
        END;
        SET @Number+=1;
    END;
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT>0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;

SELECT order_id,order_code,order_status,payment_status,total_amount FROM dbo.orders
WHERE user_id=@UserId AND note=@Marker AND order_code LIKE CONCAT('DEMO-HISTORY-',@UserId,'-%')
ORDER BY created_at DESC,order_id DESC;
GO
