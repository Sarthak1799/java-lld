
import java.util.*;

public class DeliveryAgent extends Person implements OrderObserver {

    private List<Order> assignedOrders;
    private boolean isAvailable;

    public DeliveryAgent(String name, String email, String phone, String address) {
        super("AGENT", name, email, phone, address);
        this.assignedOrders = new ArrayList<>();
        this.isAvailable = true;
    }

    public List<Order> getAssignedOrders() {
        return new ArrayList<>(assignedOrders);
    }

    public void addOrder(Order order) {
        assignedOrders.add(order);
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void completeOrder(String orderId) {
        assignedOrders.removeIf(order -> order.getId().equals(orderId));
    }

    public int getAssignedOrderCount() {
        return assignedOrders.size();
    }

    @Override
    public synchronized void update(Order order) {
        if (getId().equals(order.getDeliveryAgentId())) {
            System.out.println("[Agent " + getName() + "] Order " + order.getId()
                    + " assigned. Status: " + order.getStatus());
        }
    }
}
