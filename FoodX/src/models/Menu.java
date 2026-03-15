
import java.util.*;

public class Menu {

    private String id;
    private String restaurantId;
    private Map<String, MenuItem> items;

    public Menu(String restaurantId) {
        this.id = "MENU_" + UUID.randomUUID().toString();
        this.restaurantId = restaurantId;
        this.items = new HashMap<>();
    }

    public synchronized void addItem(MenuItem item) {
        items.put(item.getId(), item);
    }

    public synchronized void removeItem(String itemId) {
        items.remove(itemId);
    }

    public synchronized MenuItem getItem(String itemId) {
        return items.get(itemId);
    }

    public synchronized List<MenuItem> getAllItems() {
        return new ArrayList<>(items.values());
    }

    public synchronized void updateItem(MenuItem item) {
        if (items.containsKey(item.getId())) {
            items.put(item.getId(), item);
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public synchronized Map<String, MenuItem> getItems() {
        return new HashMap<>(items);
    }
}
