package models;

import java.util.*;

public class Portfolio implements StockObserver {

    private String id;
    private String name;
    private Trader owner;
    private List<PortfolioItem> items;

    public Portfolio(String name, Trader owner) {
        this.name = name;
        this.owner = owner;
        this.items = new ArrayList<>();
        this.id = "portfolio-" + UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PortfolioItem> getItems() {
        return items;
    }

    public Trader getOwner() {
        return owner;
    }

    public synchronized void addItem(PortfolioItem item) {
        this.items.add(item);
    }

    public synchronized void removeItem(PortfolioItem item) {
        this.items.remove(item);
    }

    public synchronized void addStock(Stock stock, int quantity) {
        for (PortfolioItem item : items) {
            if (item.getStock().getId().equals(stock.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new PortfolioItem(stock, quantity));
    }

    public synchronized void removeStock(Stock stock, int quantity) {
        for (PortfolioItem item : items) {
            if (item.getStock().getId().equals(stock.getId())) {
                if (item.getQuantity() < quantity) {
                    throw new IllegalArgumentException("Insufficient stock quantity in portfolio.");
                }
                item.setQuantity(item.getQuantity() - quantity);
                if (item.getQuantity() == 0) {
                    items.remove(item);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Stock not found in portfolio.");
    }

    @Override
    public void onStockUpdate(Stock stock) {
        for (PortfolioItem item : items) {
            if (item.getStock().getId().equals(stock.getId())) {
                // Here you can implement any logic needed when the stock is updated.
                // For example, you might want to log the update or notify the owner.
                System.out.println("Trader " + owner.getName() + "'s portfolio received update for stock " + stock.getName());
                System.out.println("Portfolio " + name + " has stock " + stock.getName() + " updated to price " + stock.getCurrentPrice());
            }
        }
    }
}
