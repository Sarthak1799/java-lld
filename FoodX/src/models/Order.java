
import java.time.LocalDateTime;
import java.util.*;

public class Order {

    private String id;
    private String customerId;
    private String restaurantId;
    private String deliveryAgentId;
    private List<OrderItem> items;
    private OrderStatus status;
    private double totalPrice;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String deliveryAddress;

    public Order(String customerId, String restaurantId, List<OrderItem> items,
            double totalPrice, PaymentMethod paymentMethod, String deliveryAddress) {
        this.id = "ORD_" + UUID.randomUUID().toString();
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.items = new ArrayList<>(items);
        this.status = OrderStatus.PENDING;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getDeliveryAgentId() {
        return deliveryAgentId;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    // Setters
    public void setStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDeliveryAgentId(String deliveryAgentId) {
        this.deliveryAgentId = deliveryAgentId;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
