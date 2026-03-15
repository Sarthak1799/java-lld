package models;

import java.util.*;

public class PortfolioItem {

    private String id;
    private Stock stock;
    private int quantity;

    public PortfolioItem(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
        this.id = "item-" + UUID.randomUUID().toString();
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getId() {
        return id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
