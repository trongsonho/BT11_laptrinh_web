package vn.edu.hcmute.exam.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/** Access only while holding the HttpSession monitor. Prices never live in the cart. */
public class Cart_24133049 implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int userId;
    private final Map<Integer, Integer> quantities = new TreeMap<>();
    private String checkoutToken;
    private final Map<String, Long> completedTokens = new LinkedHashMap<>();
    public Cart_24133049(int userId) { this.userId = userId; }
    public int getUserId() { return userId; }
    public Map<Integer, Integer> snapshot() { return new TreeMap<>(quantities); }
    public int quantity(int bookId) { return quantities.getOrDefault(bookId, 0); }
    public long getTotalQuantity() { return quantities.values().stream().mapToLong(Integer::longValue).sum(); }
    public boolean isEmpty() { return quantities.isEmpty(); }
    public void setQuantity(int bookId, int quantity) { quantities.put(bookId, quantity); checkoutToken = null; }
    public void remove(int bookId) { quantities.remove(bookId); checkoutToken = null; }
    public void clear() { quantities.clear(); checkoutToken = null; }
    public String getCheckoutToken() { return checkoutToken; }
    public void setCheckoutToken(String token) { checkoutToken = token; }
    public Long completedOrder(String token) { return completedTokens.get(token); }
    public void complete(String token, long orderId) {
        clear();
        completedTokens.put(token, orderId);
        if (completedTokens.size() > 10) completedTokens.remove(completedTokens.keySet().iterator().next());
    }
}
