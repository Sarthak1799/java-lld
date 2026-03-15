package models;

public class Account {

    private String id;
    private Trader trader;
    private double balance;

    public Account(Trader trader, double initialBalance) {
        this.trader = trader;
        this.balance = initialBalance;
        this.id = "account-" + java.util.UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public Trader getTrader() {
        return trader;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void setBalance(double balance) {
        this.balance = balance;
    }
}
