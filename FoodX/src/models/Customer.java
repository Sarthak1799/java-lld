
import java.util.*;

public class Customer extends Person implements OrderObserver {

    private Cart cart;
    private List<Order> orderHistory;

    public Customer(String name, String email, String phone, String address) {
        super("CUST", name, email, phone, address);
        this.cart = new Cart(getId());
        this.orderHistory = new ArrayList<>();
    }

    public Cart getCart() {
        return cart;
    }

    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }

    public void clearCart() {
        cart.clear();
    }

    public double getCartTotal() {
        return cart.getTotalPrice();
    }

    @Override
    public synchronized void update(Order order) {
        if (order.getCustomerId().equals(getId())) {
            System.out.println("[Customer " + getName() + "] Order " + order.getId()
                    + " status updated: " + order.getStatus());
        }
    }
}
