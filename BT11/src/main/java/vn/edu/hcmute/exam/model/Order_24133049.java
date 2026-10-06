package vn.edu.hcmute.exam.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order_24133049 {
    private final long id;
    private final String code, paymentMethod, status, paymentStatus;
    private final Shipping_24133049 shipping;
    private final Timestamp createdAt;
    private final BigDecimal total;
    private final List<OrderItem_24133049> items;
    public Order_24133049(long id, String code, Shipping_24133049 shipping, Timestamp createdAt,
            BigDecimal total, String paymentMethod, String status, String paymentStatus, List<OrderItem_24133049> items) {
        this.id = id; this.code = code; this.shipping = shipping; this.createdAt = createdAt;
        this.total = total; this.paymentMethod = paymentMethod; this.status = status; this.paymentStatus = paymentStatus;
        this.items = List.copyOf(items);
    }
    public long getId() { return id; }
    public String getCode() { return code; }
    public Shipping_24133049 getShipping() { return shipping; }
    public Timestamp getCreatedAt() { return createdAt; }
    public BigDecimal getTotal() { return total; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public String getStatusLabel() { return OrderStatus_24133049.labelFor(status); }
    public String getStatusBadge() { return OrderStatus_24133049.badgeFor(status); }
    public String getPaymentStatus() { return paymentStatus; }
    public List<OrderItem_24133049> getItems() { return items; }
}
