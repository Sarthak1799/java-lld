package models;

import java.util.*;

public class Trader {

    private String id;
    private String name;
    private List<Portfolio> portfolios;
    private Account account;
    private OrderLedger orderLedger;

    public Trader(String name, double initialBalance) {
        this.name = name;
        this.portfolios = new ArrayList<>();
        this.id = "trader-" + UUID.randomUUID().toString();
        this.account = new Account(this, initialBalance);
        this.orderLedger = new OrderLedger(this);
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public OrderLedger getOrderLedger() {
        return orderLedger;
    }
    public List<Portfolio> getPortfolios() {
        return portfolios;
    }
    public Account getAccount() {
        return account;
    }

    public synchronized void addPortfolio(Portfolio portfolio) {
        this.portfolios.add(portfolio);
    }
    public synchronized void removePortfolio(Portfolio portfolio) {
        this.portfolios.remove(portfolio);
    }
    public synchronized void addOrder(Order order) {
        this.orderLedger.addOrder(order);
    }
    
}
