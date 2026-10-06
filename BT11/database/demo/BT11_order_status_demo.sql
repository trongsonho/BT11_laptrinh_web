-- DEMO ONLY: run on an order you deliberately choose for demonstration.
-- No stock, money or payment changes. No cancellation/return/refund business logic.
-- No updated_at column exists in the current schema.
-- Apply BT11_order_status_history.sql first.
-- Each block below is COMMENTED OUT so executing this whole file changes nothing.
-- Copy ONE block without its comment wrapper to a query window, set @OrderId, then run.
-- Record the initial order_status before your first update. To restore it, copy
-- one block and replace its target code with the recorded original code.
-- Refresh /order-history and /order?id=... after EACH selected update.
USE [BookStore_24133049];
GO
/*
-- NEW: Đơn hàng mới
DECLARE @OrderId BIGINT = NULL; -- Replace NULL with your chosen demo order ID.
IF @OrderId IS NULL THROW 51010, 'Set @OrderId explicitly before running.', 1;
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    SELECT order_id,order_code,user_id,order_status,payment_status,total_amount
    FROM dbo.orders WITH (UPDLOCK,HOLDLOCK) WHERE order_id=@OrderId;
    UPDATE dbo.orders SET order_status='NEW' WHERE order_id=@OrderId;
    IF @@ROWCOUNT <> 1 THROW 51011, 'Expected exactly one existing order.', 1;
    SELECT order_id,order_code,order_status,payment_status,total_amount
    FROM dbo.orders WHERE order_id=@OrderId;
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
*/
/*
-- CONFIRMED: Đã xác nhận
DECLARE @OrderId BIGINT = NULL; -- Replace NULL with your chosen demo order ID.
IF @OrderId IS NULL THROW 51010, 'Set @OrderId explicitly before running.', 1;
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    SELECT order_id,order_code,user_id,order_status,payment_status,total_amount
    FROM dbo.orders WITH (UPDLOCK,HOLDLOCK) WHERE order_id=@OrderId;
    UPDATE dbo.orders SET order_status='CONFIRMED' WHERE order_id=@OrderId;
    IF @@ROWCOUNT <> 1 THROW 51011, 'Expected exactly one existing order.', 1;
    SELECT order_id,order_code,order_status,payment_status,total_amount
    FROM dbo.orders WHERE order_id=@OrderId;
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
*/
/*
-- PREPARING: Chuẩn bị hàng
DECLARE @OrderId BIGINT = NULL; -- Replace NULL with your chosen demo order ID.
IF @OrderId IS NULL THROW 51010, 'Set @OrderId explicitly before running.', 1;
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    SELECT order_id,order_code,user_id,order_status,payment_status,total_amount
    FROM dbo.orders WITH (UPDLOCK,HOLDLOCK) WHERE order_id=@OrderId;
    UPDATE dbo.orders SET order_status='PREPARING' WHERE order_id=@OrderId;
    IF @@ROWCOUNT <> 1 THROW 51011, 'Expected exactly one existing order.', 1;
    SELECT order_id,order_code,order_status,payment_status,total_amount
    FROM dbo.orders WHERE order_id=@OrderId;
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
*/
/*
-- SHIPPED: Vận chuyển (SHIPPING cũng được ánh xạ nhãn này)
DECLARE @OrderId BIGINT = NULL; -- Replace NULL with your chosen demo order ID.
IF @OrderId IS NULL THROW 51010, 'Set @OrderId explicitly before running.', 1;
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    SELECT order_id,order_code,user_id,order_status,payment_status,total_amount
    FROM dbo.orders WITH (UPDLOCK,HOLDLOCK) WHERE order_id=@OrderId;
    UPDATE dbo.orders SET order_status='SHIPPED' WHERE order_id=@OrderId;
    IF @@ROWCOUNT <> 1 THROW 51011, 'Expected exactly one existing order.', 1;
    SELECT order_id,order_code,order_status,payment_status,total_amount
    FROM dbo.orders WHERE order_id=@OrderId;
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
*/
/*
-- DELIVERING: Đang giao hàng
DECLARE @OrderId BIGINT = NULL; -- Replace NULL with your chosen demo order ID.
IF @OrderId IS NULL THROW 51010, 'Set @OrderId explicitly before running.', 1;
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    SELECT order_id,order_code,user_id,order_status,payment_status,total_amount
    FROM dbo.orders WITH (UPDLOCK,HOLDLOCK) WHERE order_id=@OrderId;
    UPDATE dbo.orders SET order_status='DELIVERING' WHERE order_id=@OrderId;
    IF @@ROWCOUNT <> 1 THROW 51011, 'Expected exactly one existing order.', 1;
    SELECT order_id,order_code,order_status,payment_status,total_amount
    FROM dbo.orders WHERE order_id=@OrderId;
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
*/
/*
-- COMPLETED: Đã giao (DELIVERED cũng được ánh xạ nhãn này)
DECLARE @OrderId BIGINT = NULL; -- Replace NULL with your chosen demo order ID.
IF @OrderId IS NULL THROW 51010, 'Set @OrderId explicitly before running.', 1;
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    SELECT order_id,order_code,user_id,order_status,payment_status,total_amount
    FROM dbo.orders WITH (UPDLOCK,HOLDLOCK) WHERE order_id=@OrderId;
    UPDATE dbo.orders SET order_status='COMPLETED' WHERE order_id=@OrderId;
    IF @@ROWCOUNT <> 1 THROW 51011, 'Expected exactly one existing order.', 1;
    SELECT order_id,order_code,order_status,payment_status,total_amount
    FROM dbo.orders WHERE order_id=@OrderId;
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
*/
/*
-- CANCELLED: Đơn hàng hủy
DECLARE @OrderId BIGINT = NULL; -- Replace NULL with your chosen demo order ID.
IF @OrderId IS NULL THROW 51010, 'Set @OrderId explicitly before running.', 1;
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    SELECT order_id,order_code,user_id,order_status,payment_status,total_amount
    FROM dbo.orders WITH (UPDLOCK,HOLDLOCK) WHERE order_id=@OrderId;
    UPDATE dbo.orders SET order_status='CANCELLED' WHERE order_id=@OrderId;
    IF @@ROWCOUNT <> 1 THROW 51011, 'Expected exactly one existing order.', 1;
    SELECT order_id,order_code,order_status,payment_status,total_amount
    FROM dbo.orders WHERE order_id=@OrderId;
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
*/
/*
-- RETURNED: Đơn hàng hoàn
DECLARE @OrderId BIGINT = NULL; -- Replace NULL with your chosen demo order ID.
IF @OrderId IS NULL THROW 51010, 'Set @OrderId explicitly before running.', 1;
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    SELECT order_id,order_code,user_id,order_status,payment_status,total_amount
    FROM dbo.orders WITH (UPDLOCK,HOLDLOCK) WHERE order_id=@OrderId;
    UPDATE dbo.orders SET order_status='RETURNED' WHERE order_id=@OrderId;
    IF @@ROWCOUNT <> 1 THROW 51011, 'Expected exactly one existing order.', 1;
    SELECT order_id,order_code,order_status,payment_status,total_amount
    FROM dbo.orders WHERE order_id=@OrderId;
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
*/
