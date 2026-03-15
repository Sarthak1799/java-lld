
import java.util.*;

public class OrderService {

    private static OrderService instance;
    private Map<String, Order> orders;
    private List<OrderObserver> observers;
    private int orderCounter;
    private PaymentProcessor paymentProcessor;

    private OrderService() {
        this.orders = new HashMap<>();
        this.observers = new ArrayList<>();
        this.orderCounter = 0;
    }

    public static synchronized OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public synchronized void subscribe(OrderObserver observer) {
        observers.add(observer);
    }

    public synchronized void unsubscribe(OrderObserver observer) {
        observers.remove(observer);
    }

    public synchronized void notifyObservers(Order order) {
        for (OrderObserver observer : observers) {
            observer.update(order);
        }
    }

    public synchronized Order createOrder(String customerId, String restaurantId, List<OrderItem> items,
            double totalPrice, PaymentMethod paymentMethod, String deliveryAddress) {
        Order order = new Order(customerId, restaurantId, items, totalPrice, paymentMethod, deliveryAddress);

        // Process payment
        if (paymentProcessor.processPayment(totalPrice, paymentMethod)) {
            orders.put(order.getId(), order);
            order.setStatus(OrderStatus.CONFIRMED);
            notifyObservers(order);
            return order;
        }
        return null;
    }

    public synchronized Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public synchronized List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public synchronized List<Order> getOrdersByCustomer(String customerId) {
        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getCustomerId().equals(customerId)) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    public synchronized List<Order> getOrdersByRestaurant(String restaurantId) {
        List<Order> restaurantOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getRestaurantId().equals(restaurantId)) {
                restaurantOrders.add(order);
            }
        }
        return restaurantOrders;
    }

    public synchronized List<Order> getOrdersByDeliveryAgent(String agentId) {
        List<Order> agentOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (agentId.equals(order.getDeliveryAgentId())) {
                agentOrders.add(order);
            }
        }
        return agentOrders;
    }

    public synchronized void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(status);
            notifyObservers(order);
        }
    }

    public synchronized void assignDeliveryAgent(String orderId, String agentId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setDeliveryAgentId(agentId);
            notifyObservers(order);
        }
    }

    public synchronized void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null && order.getStatus() != OrderStatus.DELIVERED) {
            order.setStatus(OrderStatus.CANCELLED);
            paymentProcessor.refund(order.getTotalPrice(), order.getPaymentMethod());
            notifyObservers(order);
        }
    }

    public synchronized int getTotalOrders() {
        return orders.size();
    }

    public synchronized List<Order> getPendingOrders() {
        List<Order> pending = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getStatus() == OrderStatus.PENDING) {
                pending.add(order);
            }
        }
        return pending;
    }

    public synchronized double getTotalRevenue() {
        return orders.values().stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }
}
