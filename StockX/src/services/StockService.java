package services;
import java.util.*;
import models.*;

public class StockService {
    private static StockService instance;
    private Map<String, Stock> stockMarket;
    private List<StockObserver> observers;

    private StockService() {
        this.stockMarket = new HashMap<>();
        this.observers = new ArrayList<>();
    }

    public static StockService getInstance() {
        if (instance == null) {
            synchronized (StockService.class) {
                if (instance == null) {
                    instance = new StockService();
                }
            }
        }
        return instance;
    }

    public synchronized void registerObserver(StockObserver observer) {
        observers.add(observer);
    }

    public synchronized void addStock(Stock stock) {
        stockMarket.put(stock.getSymbol(), stock);
    }

    public Stock getStockBySymbol(String symbol) {
        return stockMarket.get(symbol);
    }

    public List<Stock> getAllStocks() {
        return new ArrayList<>(stockMarket.values());
    }

    public synchronized void notifyObservers(Stock stock) {
        for (StockObserver observer : observers) {
            observer.onStockUpdate(stock);
        }
    }

    public synchronized void updateStockPrice(String symbol, double newPrice) {
        Stock stock = stockMarket.get(symbol);
        if (stock != null) {
            stock.setCurrentPrice(newPrice);
            notifyObservers(stock);
        }
    }
}