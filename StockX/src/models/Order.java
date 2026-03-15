package models;


public abstract class Order {
    private String id;
    private Trader trader;
    private Stock stock;
    private int quantity;
    private double pricePerUnit;

    public Order(Trader trader, Stock stock, int quantity, double pricePerUnit) {
        this.trader = trader;
        this.stock = stock;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.id = "order-" + java.util.UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
    public Trader getTrader() {
        return trader;
    }
    public Stock getStock() {
        return stock;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public abstract void execute();
}