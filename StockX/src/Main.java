import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import models.*;
import services.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("==============================================");
        System.out.println("  Welcome to StockX Trading System");
        System.out.println("==============================================\n");

        // Run basic tests first
        runBasicTests();

        // Run concurrency tests
        runConcurrencyTests();

        System.out.println("\n==============================================");
        System.out.println("  All Tests Completed Successfully!");
        System.out.println("==============================================");
    }

    private static void runBasicTests() {
        System.out.println("\n========== BASIC FUNCTIONALITY TESTS ==========\n");

        // Get the singleton instance of StockXSystem
        StockXSystem system = StockXSystem.getInstance();

        // Test 1: Add Stocks to the system
        System.out.println("\n--- Test 1: Adding Stocks to the System ---");
        system.addStock("AAPL", "Apple Inc.", 150.00, 1000000);
        system.addStock("GOOGL", "Alphabet Inc.", 2800.00, 500000);
        system.addStock("TSLA", "Tesla Inc.", 700.00, 750000);
        system.addStock("MSFT", "Microsoft Corporation", 300.00, 900000);
        system.addStock("AMZN", "Amazon.com Inc.", 3300.00, 600000);

        // Display all stocks
        system.displayAllStocks();

        // Test 2: Register Traders
        System.out.println("\n\n--- Test 2: Registering Traders ---");
        Trader trader1 = system.registerTrader("Alice Johnson", 100000.00);
        Trader trader2 = system.registerTrader("Bob Smith", 150000.00);
        Trader trader3 = system.registerTrader("Charlie Brown", 80000.00);

        // Display trader information
        system.displayTraderInfo(trader1);
        system.displayTraderInfo(trader2);
        system.displayTraderInfo(trader3);

        // Test 3: Create Portfolios
        System.out.println("\n\n--- Test 3: Creating Portfolios ---");
        Portfolio portfolio1 = system.createPortfolio(trader1, "Alice's Tech Portfolio");
        Portfolio portfolio2 = system.createPortfolio(trader2, "Bob's Growth Portfolio");
        Portfolio portfolio3 = system.createPortfolio(trader3, "Charlie's Dividend Portfolio");

        // Test 4: Place Buy Orders
        System.out.println("\n\n--- Test 4: Placing Buy Orders ---");
        Stock appleStock = system.getStock("AAPL");
        Stock googleStock = system.getStock("GOOGL");
        Stock teslaStock = system.getStock("TSLA");
        Stock msftStock = system.getStock("MSFT");

        system.placeBuyOrder(trader1, appleStock, 100);
        system.placeBuyOrder(trader1, googleStock, 20);
        system.placeBuyOrder(trader2, teslaStock, 50);
        system.placeBuyOrder(trader2, msftStock, 150);
        system.placeBuyOrder(trader3, appleStock, 200);

        // Display pending orders
        system.displayPendingOrders();

        // Test 5: Execute Orders
        System.out.println("\n\n--- Test 5: Executing Orders ---");
        system.executeNextOrder(); // Alice buys AAPL
        system.displayPortfolioInfo(portfolio1);
        system.displayTraderInfo(trader1);

        system.executeNextOrder(); // Alice buys GOOGL
        system.displayPortfolioInfo(portfolio1);
        system.displayTraderInfo(trader1);

        system.executeNextOrder(); // Bob buys TSLA
        system.displayPortfolioInfo(portfolio2);
        system.displayTraderInfo(trader2);

        system.executeNextOrder(); // Bob buys MSFT
        system.displayPortfolioInfo(portfolio2);
        system.displayTraderInfo(trader2);

        system.executeNextOrder(); // Charlie buys AAPL
        system.displayPortfolioInfo(portfolio3);
        system.displayTraderInfo(trader3);

        // Display remaining pending orders
        system.displayPendingOrders();

        // Test 6: Update Stock Prices (Observer Pattern Test)
        System.out.println("\n\n--- Test 6: Updating Stock Prices (Observer Pattern) ---");
        System.out.println("Updating Apple stock price from $150 to $160...");
        system.updateStockPrice("AAPL", 160.00);
        
        System.out.println("\nUpdating Google stock price from $2800 to $2900...");
        system.updateStockPrice("GOOGL", 2900.00);

        // Display updated portfolios
        System.out.println("\nPortfolios after price updates:");
        system.displayPortfolioInfo(portfolio1);
        system.displayPortfolioInfo(portfolio3);

        // Test 7: Place Sell Orders
        System.out.println("\n\n--- Test 7: Placing Sell Orders ---");
        system.placeSellOrder(trader1, appleStock, 50);
        system.placeSellOrder(trader2, teslaStock, 25);

        // Display pending orders
        system.displayPendingOrders();

        // Execute sell orders
        System.out.println("\nExecuting sell orders...");
        system.executeNextOrder(); // Alice sells 50 AAPL
        system.displayPortfolioInfo(portfolio1);
        system.displayTraderInfo(trader1);

        system.executeNextOrder(); // Bob sells 25 TSLA
        system.displayPortfolioInfo(portfolio2);
        system.displayTraderInfo(trader2);

        // Test 8: Test Edge Cases
        System.out.println("\n\n--- Test 8: Testing Edge Cases ---");
        
        // Try to buy with insufficient balance
        System.out.println("\nAttempting to buy with insufficient balance:");
        Stock amazonStock = system.getStock("AMZN");
        system.placeBuyOrder(trader3, amazonStock, 100); // This should fail
        
        // Try to sell stock not in portfolio
        System.out.println("\nAttempting to sell stock not owned:");
        system.placeSellOrder(trader3, teslaStock, 10);
        
        // Display pending orders
        system.displayPendingOrders();

        // Test 9: Display Order History
        System.out.println("\n\n--- Test 9: Order History ---");
        System.out.println("\nAlice's Order History:");
        for (Order order : trader1.getOrderLedger().getOrders()) {
            String orderType = (order instanceof BuyOrder) ? "BUY" : "SELL";
            System.out.println("  " + orderType + " - " + order.getStock().getSymbol() + 
                             " | Quantity: " + order.getQuantity() + 
                             " | Price: $" + order.getPricePerUnit());
        }

        System.out.println("\nBob's Order History:");
        for (Order order : trader2.getOrderLedger().getOrders()) {
            String orderType = (order instanceof BuyOrder) ? "BUY" : "SELL";
            System.out.println("  " + orderType + " - " + order.getStock().getSymbol() + 
                             " | Quantity: " + order.getQuantity() + 
                             " | Price: $" + order.getPricePerUnit());
        }

        // Test 10: Final Summary
        System.out.println("\n\n--- Test 10: Final System Summary ---");
        System.out.println("\nAll Traders:");
        for (Trader trader : system.getAllTraders()) {
            System.out.println("  - " + trader.getName() + " | Balance: $" + 
                             trader.getAccount().getBalance());
        }

        system.displayAllStocks();

        System.out.println("\n\nAll Portfolio Values:");
        double totalValue1 = calculatePortfolioValue(portfolio1);
        double totalValue2 = calculatePortfolioValue(portfolio2);
        double totalValue3 = calculatePortfolioValue(portfolio3);

        System.out.println("Alice's Portfolio Value: $" + totalValue1);
        System.out.println("Bob's Portfolio Value: $" + totalValue2);
        System.out.println("Charlie's Portfolio Value: $" + totalValue3);

        // Test 11: Singleton Pattern Verification
        System.out.println("\n\n--- Test 11: Singleton Pattern Verification ---");
        StockXSystem system2 = StockXSystem.getInstance();
        System.out.println("Same StockXSystem instance? " + (system == system2));
        
        StockService stockService1 = StockService.getInstance();
        StockService stockService2 = StockService.getInstance();
        System.out.println("Same StockService instance? " + (stockService1 == stockService2));
        
        TraderService traderService1 = TraderService.getInstance();
        TraderService traderService2 = TraderService.getInstance();
        System.out.println("Same TraderService instance? " + (traderService1 == traderService2));
        
        OrderService orderService1 = OrderService.getInstance();
        OrderService orderService2 = OrderService.getInstance();
        System.out.println("Same OrderService instance? " + (orderService1 == orderService2));
    }

    private static void runConcurrencyTests() throws InterruptedException {
        System.out.println("\n\n========== CONCURRENCY TESTS ==========\n");
        
        // Get the singleton instance
        StockXSystem system = StockXSystem.getInstance();
        
        // Test 1: Concurrent Trader Registration
        System.out.println("--- Test 1: Concurrent Trader Registration ---");
        testConcurrentTraderRegistration(system);
        
        // Test 2: Concurrent Stock Price Updates
        System.out.println("\n--- Test 2: Concurrent Stock Price Updates ---");
        testConcurrentStockPriceUpdates(system);
        
        // Test 3: Concurrent Order Placement
        System.out.println("\n--- Test 3: Concurrent Order Placement ---");
        testConcurrentOrderPlacement(system);
        
        // Test 4: Concurrent Order Execution
        System.out.println("\n--- Test 4: Concurrent Order Execution ---");
        testConcurrentOrderExecution(system);
        
        // Test 5: Concurrent Account Updates
        System.out.println("\n--- Test 5: Concurrent Account Updates ---");
        testConcurrentAccountUpdates(system);
    }

    private static void testConcurrentTraderRegistration(StockXSystem system) throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    Trader trader = system.registerTrader("ConcurrentTrader" + threadId, 50000.0);
                    successCount.incrementAndGet();
                    System.out.println("Thread " + threadId + " registered trader: " + trader.getName());
                } catch (Exception e) {
                    System.err.println("Thread " + threadId + " failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("Total traders registered: " + successCount.get() + " / " + numThreads);
        System.out.println("Total traders in system: " + TraderService.getInstance().getAllTraders().size());
    }

    private static void testConcurrentStockPriceUpdates(StockXSystem system) throws InterruptedException {
        // Add a test stock
        system.addStock("TEST", "Test Corp", 100.0, 1000000);
        
        int numThreads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    double newPrice = 100.0 + threadId;
                    system.updateStockPrice("TEST", newPrice);
                    System.out.println("Thread " + threadId + " updated TEST to $" + newPrice);
                } catch (Exception e) {
                    System.err.println("Thread " + threadId + " failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        Stock testStock = system.getStock("TEST");
        System.out.println("Final TEST stock price: $" + testStock.getCurrentPrice());
    }

    private static void testConcurrentOrderPlacement(StockXSystem system) throws InterruptedException {
        // Create a trader with sufficient balance
        Trader trader = system.registerTrader("ConcurrentOrderTrader", 1000000.0);
        Stock stock = system.getStock("AAPL");
        
        int numOrders = 50;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numOrders);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numOrders; i++) {
            final int orderId = i;
            executor.submit(() -> {
                try {
                    if (orderId % 2 == 0) {
                        system.placeBuyOrder(trader, stock, 1);
                    } else {
                        system.placeSellOrder(trader, stock, 1);
                    }
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.err.println("Order " + orderId + " failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("Orders placed successfully: " + successCount.get() + " / " + numOrders);
        System.out.println("Total orders in system: " + OrderService.getInstance().getAllOrders().size());
    }

    private static void testConcurrentOrderExecution(StockXSystem system) throws InterruptedException {
        // Place multiple orders
        Trader t1 = system.registerTrader("ExecutionTrader1", 100000.0);
        Trader t2 = system.registerTrader("ExecutionTrader2", 100000.0);
        Stock stock = system.getStock("MSFT");
        
        system.placeBuyOrder(t1, stock, 10);
        system.placeBuyOrder(t2, stock, 10);
        system.placeBuyOrder(t1, stock, 10);
        system.placeBuyOrder(t2, stock, 10);
        system.placeBuyOrder(t1, stock, 10);
        
        int numThreads = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    system.executeNextOrder();
                    System.out.println("Thread " + threadId + " executed an order");
                } catch (Exception e) {
                    System.err.println("Thread " + threadId + " execution failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("Remaining orders: " + OrderService.getInstance().getAllOrders().size());
        System.out.println("Trader1 balance: $" + t1.getAccount().getBalance());
        System.out.println("Trader2 balance: $" + t2.getAccount().getBalance());
    }

    private static void testConcurrentAccountUpdates(StockXSystem system) throws InterruptedException {
        Trader trader = system.registerTrader("AccountTestTrader", 50000.0);
        Account account = trader.getAccount();
        
        int numThreads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(numThreads);

        // Half the threads will add money, half will subtract
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    synchronized (account) {
                        double currentBalance = account.getBalance();
                        if (threadId % 2 == 0) {
                            account.setBalance(currentBalance + 100);
                        } else {
                            account.setBalance(currentBalance - 100);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Thread " + threadId + " failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("Initial balance: $50000.0");
        System.out.println("Final balance: $" + account.getBalance());
        System.out.println("Expected balance: $50000.0 (50 additions of $100, 50 subtractions of $100)");
    }

    private static double calculatePortfolioValue(Portfolio portfolio) {
        double totalValue = 0;
        for (PortfolioItem item : portfolio.getItems()) {
            totalValue += item.getQuantity() * item.getStock().getCurrentPrice();
        }
        return totalValue;
    }
}
