package vn.edu.hcmute.exam.model;

import java.util.List;

/** Shared mapping for database codes, filter allowlist and all order views. */
public enum OrderStatus_24133049 {
    NEW("Đơn hàng mới", "primary", "NEW", "PENDING"),
    CONFIRMED("Đã xác nhận", "info", "CONFIRMED"),
    PREPARING("Chuẩn bị hàng", "warning", "PREPARING"),
    SHIPPED("Vận chuyển", "info", "SHIPPED", "SHIPPING"),
    DELIVERING("Đang giao hàng", "warning", "DELIVERING"),
    COMPLETED("Đã giao", "success", "COMPLETED", "DELIVERED"),
    CANCELLED("Đơn hàng hủy", "danger", "CANCELLED"),
    RETURNED("Đơn hàng hoàn", "secondary", "RETURNED");

    private final String label, badge;
    private final List<String> databaseCodes;
    OrderStatus_24133049(String label, String badge, String... databaseCodes) {
        this.label = label; this.badge = badge; this.databaseCodes = List.of(databaseCodes);
    }
    public String getCode() { return name(); }
    public String getLabel() { return label; }
    public String getBadge() { return badge; }
    public List<String> getDatabaseCodes() { return databaseCodes; }
    public static OrderStatus_24133049 fromCode(String code) {
        if (code == null) return null;
        for (var status : values()) if (status.databaseCodes.contains(code)) return status;
        return null;
    }
    public static OrderStatus_24133049 filter(String value) {
        if (value == null || value.isEmpty()) return null;
        var status = fromCode(value);
        if (status == null) throw new IllegalArgumentException("Bộ lọc trạng thái không hợp lệ. Đã hiển thị tất cả đơn hàng.");
        return status;
    }
    public static String labelFor(String code) {
        var status = fromCode(code);
        return status == null ? "Trạng thái chưa xác định" : status.label;
    }
    public static String badgeFor(String code) {
        var status = fromCode(code);
        return status == null ? "secondary" : status.badge;
    }
}
