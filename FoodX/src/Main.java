
import java.util.concurrent.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========================================");
        System.out.println("     NORMAL TEST (SEQUENTIAL)");
        System.out.println("========================================\n");
        runNormalTest();

        System.out.println("\n\n========================================");
        System.out.println("     CONCURRENT TEST (MULTI-THREADED)");
        System.out.println("========================================\n");
        runConcurrentTest();
    }

    private static void runNormalTest() {
        // Initialize system
        FoodXSystem system = FoodXSystem.getInstance();

        // Create restaurants and add menu items
        Restaurant restaurant1 = new Restaurant("Pizza Palace", "123 Main St", "555-1234");
        MenuItem pizza = new MenuItem("Margherita Pizza", "Classic pizza with cheese", 12.99);
        MenuItem burger = new MenuItem("Burger", "Delicious beef burger", 8.99);
        MenuItem pasta = new MenuItem("Pasta", "Italian pasta", 10.99);
        restaurant1.getMenu().addItem(pizza);
        restaurant1.getMenu().addItem(burger);
        restaurant1.getMenu().addItem(pasta);
        system.getRestaurantService().addRestaurant(restaurant1);

        Restaurant restaurant2 = new Restaurant("Sushi House", "456 Oak Ave", "555-5678");
        MenuItem sushi = new MenuItem("Salmon Sushi", "Fresh salmon sushi rolls", 15.99);
        MenuItem ramen = new MenuItem("Ramen", "Japanese noodle soup", 13.99);
        restaurant2.getMenu().addItem(sushi);
        restaurant2.getMenu().addItem(ramen);
        system.getRestaurantService().addRestaurant(restaurant2);

        // Create customers
        Customer customer1 = new Customer("John Doe", "john@example.com", "555-1111", "789 Elm St");
        Customer customer2 = new Customer("Jane Smith", "jane@example.com", "555-2222", "321 Pine St");
        system.getCustomerService().registerCustomer(customer1);
        system.getCustomerService().registerCustomer(customer2);

        // Create delivery agents
        DeliveryAgent agent1 = new DeliveryAgent("Bob Driver", "bob@example.com", "555-3333", "100 Delivery Ln");
        DeliveryAgent agent2 = new DeliveryAgent("Alice Runner", "alice@example.com", "555-4444", "200 Delivery Ln");
        system.getDeliveryService().registerDeliveryAgent(agent1);
        system.getDeliveryService().registerDeliveryAgent(agent2);

        // Simulate customer placing orders
        System.out.println("=== Customer 1 Orders ===");
        system.getCustomerService().addItemToCart(customer1.getId(), pizza, 2);
        system.getCustomerService().addItemToCart(customer1.getId(), burger, 1);
        System.out.println("Cart total: $" + system.getCustomerService().getCartTotal(customer1.getId()));

        Order order1 = system.placeOrder(customer1.getId(), restaurant1.getId(), PaymentMethod.CREDIT_CARD, "789 Elm St");
        if (order1 != null) {
            System.out.println("Order placed: " + order1.getId());
        }

        System.out.println("\n=== Customer 2 Orders ===");
        system.getCustomerService().addItemToCart(customer2.getId(), sushi, 3);
        Order order2 = system.placeOrder(customer2.getId(), restaurant2.getId(), PaymentMethod.DIGITAL_WALLET, "321 Pine St");
        if (order2 != null) {
            System.out.println("Order placed: " + order2.getId());
        }

        // Update order statuses
        System.out.println("\n=== Updating Order Status ===");
        system.updateOrderStatus(order1.getId(), OrderStatus.READY_FOR_PICKUP);
        system.updateOrderStatus(order1.getId(), OrderStatus.OUT_FOR_DELIVERY);
        system.updateOrderStatus(order1.getId(), OrderStatus.DELIVERED);

        // Display order history
        System.out.println("\n=== Order History ===");
        for (Order order : system.getCustomerService().getOrderHistory(customer1.getId())) {
            System.out.println("Order " + order.getId() + ": " + order.getStatus() + " - Total: $" + order.getTotalPrice());
        }

        // Display system statistics
        System.out.println("\n=== System Statistics ===");
        System.out.println("Total Restaurants: " + system.getRestaurantService().getTotalRestaurants());
        System.out.println("Total Customers: " + system.getCustomerService().getAllCustomers().size());
        System.out.println("Total Delivery Agents: " + system.getDeliveryService().getTotalDeliveryAgents());
        System.out.println("Total Orders: " + system.getOrderService().getTotalOrders());
        System.out.println("Total Revenue: $" + system.getOrderService().getTotalRevenue());
    }

    private static void runConcurrentTest() throws InterruptedException {
        FoodXSystem system = FoodXSystem.getInstance();

        // Get existing restaurants and menu items for concurrent test
        List<Restaurant> restaurants = system.getRestaurantService().getAllRestaurants();
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants available for concurrent test");
            return;
        }

        Restaurant restaurant = restaurants.get(0);
        List<MenuItem> menuItems = restaurant.getMenu().getAllItems();

        // Create additional customers for concurrent test
        int numberOfCustomers = 10;
        List<Customer> customers = new ArrayList<>();
        System.out.println("=== Creating " + numberOfCustomers + " Customers ===");
        for (int i = 0; i < numberOfCustomers; i++) {
            Customer customer = new Customer(
                    "Customer" + i,
                    "customer" + i + "@example.com",
                    "555-" + (2000 + i),
                    i + " Customer Street"
            );
            system.getCustomerService().registerCustomer(customer);
            customers.add(customer);
            System.out.println("Created: " + customer.getName());
        }

        // Create additional delivery agents
        System.out.println("\n=== Creating Additional Delivery Agents ===");
        for (int i = 3; i <= 5; i++) {
            DeliveryAgent agent = new DeliveryAgent(
                    "Agent" + i,
                    "agent" + i + "@example.com",
                    "555-" + (3000 + i),
                    i + " Agent Street"
            );
            system.getDeliveryService().registerDeliveryAgent(agent);
            System.out.println("Created: " + agent.getName());
        }

        // Concurrent order placement test
        System.out.println("\n=== Starting Concurrent Order Placement ===");
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfCustomers);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfCustomers; i++) {
            final int customerIndex = i;
            final Customer customer = customers.get(i);

            executorService.submit(() -> {
                try {
                    // Add items to cart concurrently
                    if (!menuItems.isEmpty()) {
                        MenuItem item = menuItems.get(customerIndex % menuItems.size());
                        system.getCustomerService().addItemToCart(customer.getId(), item, customerIndex % 3 + 1);

                        Thread.sleep(10); // Simulate some processing time

                        // Place order
                        PaymentMethod[] paymentMethods = PaymentMethod.values();
                        PaymentMethod method = paymentMethods[customerIndex % paymentMethods.length];

                        Order order = system.placeOrder(
                                customer.getId(),
                                restaurant.getId(),
                                method,
                                customer.getAddress()
                        );

                        if (order != null) {
                            System.out.println("[Thread-" + Thread.currentThread().getId() + "] "
                                    + customer.getName() + " placed order: " + order.getId());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error in thread: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all orders to complete
        latch.await();
        long endTime = System.currentTimeMillis();

        System.out.println("\n=== Concurrent Order Placement Complete ===");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
        System.out.println("Orders processed: " + numberOfCustomers);

        // Concurrent order status update test
        System.out.println("\n=== Starting Concurrent Order Status Updates ===");
        List<Order> allOrders = system.getOrderService().getAllOrders();
        CountDownLatch statusLatch = new CountDownLatch(allOrders.size());

        startTime = System.currentTimeMillis();

        for (Order order : allOrders) {
            executorService.submit(() -> {
                try {
                    // Update order status through various stages
                    Thread.sleep((long) (Math.random() * 50));
                    system.updateOrderStatus(order.getId(), OrderStatus.READY_FOR_PICKUP);

                    Thread.sleep((long) (Math.random() * 50));
                    system.updateOrderStatus(order.getId(), OrderStatus.OUT_FOR_DELIVERY);

                    Thread.sleep((long) (Math.random() * 50));
                    system.updateOrderStatus(order.getId(), OrderStatus.DELIVERED);

                } catch (Exception e) {
                    System.err.println("Error updating status: " + e.getMessage());
                } finally {
                    statusLatch.countDown();
                }
            });
        }

        statusLatch.await();
        endTime = System.currentTimeMillis();

        System.out.println("\n=== Concurrent Status Updates Complete ===");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
        System.out.println("Status updates processed: " + (allOrders.size() * 3));

        // Concurrent cart operations test
        System.out.println("\n=== Starting Concurrent Cart Operations ===");
        int cartOperations = 50;
        CountDownLatch cartLatch = new CountDownLatch(cartOperations);

        startTime = System.currentTimeMillis();

        for (int i = 0; i < cartOperations; i++) {
            final int operationIndex = i;
            executorService.submit(() -> {
                try {
                    Customer customer = customers.get(operationIndex % customers.size());
                    MenuItem item = menuItems.get(operationIndex % menuItems.size());

                    if (operationIndex % 3 == 0) {
                        // Add to cart
                        system.getCustomerService().addItemToCart(customer.getId(), item, 1);
                    } else if (operationIndex % 3 == 1) {
                        // Update quantity
                        system.getCustomerService().updateCartItemQuantity(customer.getId(), item.getId(), 2);
                    } else {
                        // Remove from cart
                        system.getCustomerService().removeItemFromCart(customer.getId(), item.getId());
                    }
                } catch (Exception e) {
                    System.err.println("Error in cart operation: " + e.getMessage());
                } finally {
                    cartLatch.countDown();
                }
            });
        }

        cartLatch.await();
        endTime = System.currentTimeMillis();

        System.out.println("Cart operations completed: " + cartOperations);
        System.out.println("Time taken: " + (endTime - startTime) + "ms");

        // Shutdown executor
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        // Final statistics
        System.out.println("\n=== Final System Statistics (After Concurrent Tests) ===");
        System.out.println("Total Restaurants: " + system.getRestaurantService().getTotalRestaurants());
        System.out.println("Total Customers: " + system.getCustomerService().getAllCustomers().size());
        System.out.println("Total Delivery Agents: " + system.getDeliveryService().getTotalDeliveryAgents());
        System.out.println("Total Orders: " + system.getOrderService().getTotalOrders());
        System.out.println("Total Revenue: $" + String.format("%.2f", system.getOrderService().getTotalRevenue()));

        // Verify data consistency
        System.out.println("\n=== Data Consistency Verification ===");
        int deliveredOrders = 0;
        for (Order order : system.getOrderService().getAllOrders()) {
            if (order.getStatus() == OrderStatus.DELIVERED) {
                deliveredOrders++;
            }
        }
        System.out.println("Delivered Orders: " + deliveredOrders);
        System.out.println("All orders have valid IDs: "
                + system.getOrderService().getAllOrders().stream()
                        .allMatch(o -> o.getId() != null && o.getId().startsWith("ORD_")));
        System.out.println("All customers have valid IDs: "
                + system.getCustomerService().getAllCustomers().stream()
                        .allMatch(c -> c.getId() != null && c.getId().startsWith("CUST_")));

        System.out.println("\n=== Concurrent Test Completed Successfully ===");
    }
}
