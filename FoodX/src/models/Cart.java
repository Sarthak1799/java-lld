
import java.util.*;

public class Cart {

    private String id;
    private String customerId;
    private Map<String, CartItem> items;

    public Cart(String customerId) {
        this.id = "CART_" + UUID.randomUUID().toString();
        this.customerId = customerId;
        this.items = new HashMap<>();
    }

    public synchronized void addItem(CartItem item) {
        String itemId = item.getMenuItem().getId();
        if (items.containsKey(itemId)) {
            CartItem existing = items.get(itemId);
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
        } else {
            items.put(itemId, item);
        }
    }

    public synchronized void removeItem(String itemId) {
        items.remove(itemId);
    }

    public synchronized void updateQuantity(String itemId, int quantity) {
        if (items.containsKey(itemId)) {
            CartItem item = items.get(itemId);
            if (quantity <= 0) {
                items.remove(itemId);
            } else {
                item.setQuantity(quantity);
            }
        }
    }

    public synchronized void clear() {
        items.clear();
    }

    public synchronized List<CartItem> getAllItems() {
        return new ArrayList<>(items.values());
    }

    public synchronized double getTotalPrice() {
        return items.values().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public synchronized Map<String, CartItem> getItems() {
        return new HashMap<>(items);
    }

    public synchronized int getItemCount() {
        return items.size();
    }
}
