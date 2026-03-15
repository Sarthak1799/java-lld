import models.*;
import services.*;
import java.util.*;

public class StockXSystem {
    private static StockXSystem instance;
    private StockService stockService;
    private TraderService traderService;
    private OrderService orderService;

    private StockXSystem() {
        this.stockService = StockService.getInstance();
        this.traderService = TraderService.getInstance();
        this.orderService = OrderService.getInstance();
    }

    public static StockXSystem getInstance() {
        if (instance == null) {
            synchronized (StockXSystem.class) {
                if (instance == null) {
                    instance = new StockXSystem();
                }
            }
        }
        return instance;
    }

    // Stock related methods
    public void addStock(String symbol, String companyName, double currentPrice, int volume) {
        Stock stock = new Stock(symbol, companyName, currentPrice, volume);
        stockService.addStock(stock);
        System.out.println("Stock added: " + symbol + " - " + companyName);
    }

    public Stock getStock(String symbol) {
        return stockService.getStockBySymbol(symbol);
    }

    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    public void updateStockPrice(String symbol, double newPrice) {
        stockService.updateStockPrice(symbol, newPrice);
        System.out.println("Stock " + symbol + " price updated to: " + newPrice);
    }

    // Trader related methods
    public Trader registerTrader(String name, double initialBalance) {
        Trader trader = new Trader(name, initialBalance);
        traderService.registerTrader(trader);
        System.out.println("Trader registered: " + name + " with balance: " + initialBalance);
        return trader;
    }

    public Trader getTrader(String traderId) {
        return traderService.getTraderById(traderId);
    }

    public List<Trader> getAllTraders() {
        return traderService.getAllTraders();
    }

    // Portfolio related methods
    public Portfolio createPortfolio(Trader trader, String portfolioName) {
        Portfolio portfolio = new Portfolio(portfolioName, trader);
        trader.addPortfolio(portfolio);
        stockService.registerObserver(portfolio);
        System.out.println("Portfolio created: " + portfolioName + " for trader: " + trader.getName());
        return portfolio;
    }

    // Order related methods
    public void placeBuyOrder(Trader trader, Stock stock, int quantity) {
        try {
            BuyOrder buyOrder = new BuyOrder(trader, stock, quantity);
            orderService.placeOrder(buyOrder);
            System.out.println("Buy order placed: " + quantity + " units of " + stock.getSymbol() + 
                             " by " + trader.getName());
        } catch (Exception e) {
            System.out.println("Error placing buy order: " + e.getMessage());
        }
    }

    public void placeSellOrder(Trader trader, Stock stock, int quantity) {
        try {
            SellOrder sellOrder = new SellOrder(trader, stock, quantity);
            orderService.placeOrder(sellOrder);
            System.out.println("Sell order placed: " + quantity + " units of " + stock.getSymbol() + 
                             " by " + trader.getName());
        } catch (Exception e) {
            System.out.println("Error placing sell order: " + e.getMessage());
        }
    }

    public void executeNextOrder() {
        try {
            orderService.executeOrder();
            System.out.println("Order executed successfully");
        } catch (Exception e) {
            System.out.println("Error executing order: " + e.getMessage());
        }
    }

    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Display methods
    public void displayTraderInfo(Trader trader) {
        System.out.println("\n=== Trader Information ===");
        System.out.println("Name: " + trader.getName());
        System.out.println("ID: " + trader.getId());
        System.out.println("Balance: $" + trader.getAccount().getBalance());
        System.out.println("Number of Portfolios: " + trader.getPortfolios().size());
        System.out.println("Number of Orders: " + trader.getOrderLedger().getOrders().size());
    }

    public void displayPortfolioInfo(Portfolio portfolio) {
        System.out.println("\n=== Portfolio Information ===");
        System.out.println("Name: " + portfolio.getName());
        System.out.println("Owner: " + portfolio.getOwner().getName());
        System.out.println("Holdings:");
        if (portfolio.getItems().isEmpty()) {
            System.out.println("  No holdings");
        } else {
            for (PortfolioItem item : portfolio.getItems()) {
                System.out.println("  - " + item.getStock().getSymbol() + ": " + 
                                 item.getQuantity() + " units @ $" + 
                                 item.getStock().getCurrentPrice() + " = $" + 
                                 (item.getQuantity() * item.getStock().getCurrentPrice()));
            }
        }
    }

    public void displayAllStocks() {
        System.out.println("\n=== All Stocks ===");
        List<Stock> stocks = getAllStocks();
        if (stocks.isEmpty()) {
            System.out.println("No stocks available");
        } else {
            for (Stock stock : stocks) {
                System.out.println(stock.getSymbol() + " - " + stock.getCompanyName() + 
                                 " | Price: $" + stock.getCurrentPrice() + 
                                 " | Volume: " + stock.getVolume());
            }
        }
    }

    public void displayPendingOrders() {
        System.out.println("\n=== Pending Orders ===");
        List<Order> orders = getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No pending orders");
        } else {
            for (Order order : orders) {
                String orderType = (order instanceof BuyOrder) ? "BUY" : "SELL";
                System.out.println(orderType + " - " + order.getStock().getSymbol() + 
                                 " | Quantity: " + order.getQuantity() + 
                                 " | Price: $" + order.getPricePerUnit() + 
                                 " | Trader: " + order.getTrader().getName());
            }
        }
    }
}
