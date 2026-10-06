package vn.edu.hcmute.exam.model;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryPage_24133049 {
    private final List<Order_24133049> orders;
    private final long totalOrders, page, totalPages;
    public OrderHistoryPage_24133049(List<Order_24133049> orders, long totalOrders, long page, long totalPages) {
        this.orders = List.copyOf(orders); this.totalOrders = totalOrders;
        this.page = page; this.totalPages = totalPages;
    }
    public List<Order_24133049> getOrders() { return orders; }
    public long getTotalOrders() { return totalOrders; }
    public long getPage() { return page; }
    public long getTotalPages() { return totalPages; }
    public List<Long> getPageLinks() {
        List<Long> links = new ArrayList<>();
        for (long i = Math.max(1, page - 2); i <= Math.min(totalPages, page + 2); i++) links.add(i);
        return links;
    }
}
