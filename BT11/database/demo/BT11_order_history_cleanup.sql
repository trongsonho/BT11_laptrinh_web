-- Remove only display fixtures created by BT11_order_history_seed.sql.
-- Real orders and inventory are never changed. Review the SELECT first.
USE [BookStore_24133049];
GO
SET XACT_ABORT ON;
DECLARE @UserEmail VARCHAR(50)='user1@gmail.com';
DECLARE @UserId INT=(SELECT id FROM dbo.users WHERE email=@UserEmail);
DECLARE @Marker NVARCHAR(1000)=N'[BT11_STATUS_DEMO_24133049] Đơn mẫu kiểm tra lịch sử; không phát sinh mua bán hoặc thay đổi tồn kho.';
DECLARE @Ids TABLE (id BIGINT PRIMARY KEY);
BEGIN TRY
    BEGIN TRANSACTION;
    INSERT INTO @Ids SELECT order_id FROM dbo.orders WITH (UPDLOCK,HOLDLOCK)
    WHERE user_id=@UserId AND note=@Marker
      AND order_code IN (
          SELECT CONCAT('DEMO-HISTORY-',@UserId,'-',RIGHT(CONCAT('0',n),2))
          FROM (VALUES(1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15),(16)) AS samples(n)
      );
    SELECT o.order_id,o.order_code,o.order_status FROM dbo.orders o JOIN @Ids d ON d.id=o.order_id;
    DELETE i FROM dbo.order_items i JOIN @Ids d ON d.id=i.order_id;
    DELETE o FROM dbo.orders o JOIN @Ids d ON d.id=o.order_id;
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT>0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
GO
