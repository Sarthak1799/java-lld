
import java.util.UUID;

public class MenuItem {

    private String id;
    private String name;
    private String description;
    private double price;
    private boolean available;

    public MenuItem(String name, String description, double price) {
        this.id = "ITEM_" + UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = true;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setters
    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
