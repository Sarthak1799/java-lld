
import java.util.UUID;

public class OrderItem {

    private String id;
    private String menuItemId;
    private String menuItemName;
    private int quantity;
    private double price;

    public OrderItem(String menuItemId, String menuItemName, int quantity, double price) {
        this.id = "OITEM_" + UUID.randomUUID().toString();
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}
