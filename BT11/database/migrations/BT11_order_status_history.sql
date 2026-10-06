-- Additive migration: retain all existing codes; do not rewrite orders.
USE [BookStore_24133049];
GO
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    IF OBJECT_ID(N'dbo.orders', N'U') IS NULL
        THROW 51000, 'Apply BT11_cart_cod.sql to the existing database first.', 1;

    DECLARE @ConstraintId INT = OBJECT_ID(N'dbo.CK_orders_status_24133049', N'C');
    IF @ConstraintId IS NULL
        THROW 51001, 'Expected order-status CHECK missing. Inspect schema before proceeding.', 1;
    -- Display the actual definition before replacing only this status CHECK.
    SELECT name, definition FROM sys.check_constraints WHERE object_id = @ConstraintId;
    IF EXISTS (
        SELECT 1 FROM sys.sql_expression_dependencies
        WHERE referencing_id = @ConstraintId AND referenced_id = OBJECT_ID(N'dbo.orders')
          AND referenced_minor_id > 0
          AND COL_NAME(referenced_id, referenced_minor_id) <> N'order_status'
    )
        THROW 51002, 'Status CHECK also validates other columns; manual review required.', 1;

    ALTER TABLE dbo.orders DROP CONSTRAINT CK_orders_status_24133049;
    ALTER TABLE dbo.orders WITH CHECK ADD CONSTRAINT CK_orders_status_24133049
        CHECK (order_status IN ('NEW','PENDING','CONFIRMED','PREPARING',
              'SHIPPED','SHIPPING','DELIVERING','COMPLETED','DELIVERED','CANCELLED','RETURNED'));
    ALTER TABLE dbo.orders CHECK CONSTRAINT CK_orders_status_24133049;
    -- All other CHECKs, defaults, foreign keys and existing indexes remain intact.
    IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id=OBJECT_ID(N'dbo.orders') AND name=N'IX_orders_user_status_history_24133049')
        CREATE INDEX IX_orders_user_status_history_24133049
            ON dbo.orders(user_id,order_status,created_at DESC,order_id DESC)
            INCLUDE (order_code,total_amount,payment_method,payment_status);
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
GO
