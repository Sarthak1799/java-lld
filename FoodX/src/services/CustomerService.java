
import java.util.*;

public class CustomerService {

    private static CustomerService instance;
    private Map<String, Customer> customers;

    private CustomerService() {
        this.customers = new HashMap<>();
    }

    public static synchronized CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }

    public synchronized void registerCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    public synchronized Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public synchronized List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public synchronized void addItemToCart(String customerId, MenuItem item, int quantity) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            CartItem cartItem = new CartItem(item, quantity);
            customer.getCart().addItem(cartItem);
        }
    }

    public synchronized void removeItemFromCart(String customerId, String itemId) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            customer.getCart().removeItem(itemId);
        }
    }

    public synchronized void updateCartItemQuantity(String customerId, String itemId, int quantity) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            customer.getCart().updateQuantity(itemId, quantity);
        }
    }

    public synchronized double getCartTotal(String customerId) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            return customer.getCartTotal();
        }
        return 0.0;
    }

    public synchronized void clearCart(String customerId) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            customer.clearCart();
        }
    }

    public synchronized List<Order> getOrderHistory(String customerId) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            return customer.getOrderHistory();
        }
        return new ArrayList<>();
    }

    public synchronized void addOrderToHistory(String customerId, Order order) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            customer.addOrder(order);
        }
    }

    public synchronized Cart getCart(String customerId) {
        Customer customer = customers.get(customerId);
        if (customer != null) {
            return customer.getCart();
        }
        return null;
    }
}
