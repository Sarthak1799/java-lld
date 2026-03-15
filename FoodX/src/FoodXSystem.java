
public class FoodXSystem {

    private static FoodXSystem instance;
    private CustomerService customerService;
    private RestaurantService restaurantService;
    private OrderService orderService;
    private DeliveryService deliveryService;

    private FoodXSystem() {
        this.customerService = CustomerService.getInstance();
        this.restaurantService = RestaurantService.getInstance();
        this.deliveryService = DeliveryService.getInstance();
        this.orderService = OrderService.getInstance();

        // Initialize payment processor
        PaymentProcessor paymentProcessor = new CardPaymentProcessor();
        this.orderService.setPaymentProcessor(paymentProcessor);

        // Subscribe observers
        this.orderService.subscribe(restaurantService);
        this.orderService.subscribe(deliveryService);
    }

    public static synchronized FoodXSystem getInstance() {
        if (instance == null) {
            instance = new FoodXSystem();
        }
        return instance;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public RestaurantService getRestaurantService() {
        return restaurantService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }

    // Convenience methods for system operations
    public Order placeOrder(String customerId, String restaurantId, PaymentMethod paymentMethod, String deliveryAddress) {
        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            return null;
        }

        Cart cart = customer.getCart();
        if (cart.getItemCount() == 0) {
            return null;
        }

        // Convert cart items to order items
        java.util.List<OrderItem> orderItems = new java.util.ArrayList<>();
        for (CartItem cartItem : cart.getAllItems()) {
            MenuItem item = cartItem.getMenuItem();
            OrderItem orderItem = new OrderItem(
                    item.getId(),
                    item.getName(),
                    cartItem.getQuantity(),
                    item.getPrice()
            );
            orderItems.add(orderItem);
        }

        double totalPrice = cart.getTotalPrice();
        Order order = orderService.createOrder(customerId, restaurantId, orderItems, totalPrice, paymentMethod, deliveryAddress);

        if (order != null) {
            customerService.addOrderToHistory(customerId, order);
            customerService.clearCart(customerId);

            // Subscribe customer as observer for their order
            orderService.subscribe(customer);

            // Attempt to assign delivery agent
            DeliveryAgent agent = deliveryService.findNearestAvailableAgent();
            if (agent != null) {
                deliveryService.assignOrderToAgent(agent.getId(), order);
                orderService.updateOrderStatus(order.getId(), OrderStatus.PREPARING);
            }
        }

        return order;
    }

    public void cancelOrder(String orderId) {
        orderService.cancelOrder(orderId);
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        orderService.updateOrderStatus(orderId, status);
    }
}
