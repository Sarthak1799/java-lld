
import java.util.UUID;

public class Restaurant implements OrderObserver {

    private String id;
    private String name;
    private String address;
    private String phone;
    private Menu menu;
    private boolean isOpen;

    public Restaurant(String name, String address, String phone) {
        this.id = "REST_" + UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.menu = new Menu(id);
        this.isOpen = true;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public Menu getMenu() {
        return menu;
    }

    public boolean isOpen() {
        return isOpen;
    }

    // Setters
    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public synchronized void update(Order order) {
        if (order.getRestaurantId().equals(id)) {
            System.out.println("[Restaurant " + name + "] Order " + order.getId()
                    + " status: " + order.getStatus());
        }
    }
}
