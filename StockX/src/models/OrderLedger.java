package models;
import java.util.*;

public class OrderLedger{
    private Trader trader;
    private List<Order> orders;

    public OrderLedger(Trader trader) {
        this.trader = trader;
        this.orders = new ArrayList<>();
    }

    public Trader getTrader() {
        return trader;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
    public void removeOrder(Order order) {
        this.orders.remove(order);
    }
}