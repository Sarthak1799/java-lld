package services;
import java.util.*;
import models.*;

public class OrderService {
    private static OrderService instance;
    private List<Order> allOrders;
    private ExecutionStrategy executionStrategy;

    private OrderService() {
        this.allOrders = new ArrayList<>();
        this.executionStrategy = new FirstOrderStrategy();
    }

    public static OrderService getInstance() {
        if (instance == null) {
            synchronized (OrderService.class) {
                if (instance == null) {
                    instance = new OrderService();
                }
            }
        }
        return instance;
    }

    public synchronized void placeOrder(Order order) {
        allOrders.add(order);
    }

    public synchronized void executeOrder() {
        executionStrategy.execute(allOrders);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(allOrders);
    }

    public void setExecutionStrategy(ExecutionStrategy strategy) {
        this.executionStrategy = strategy;
    }
}