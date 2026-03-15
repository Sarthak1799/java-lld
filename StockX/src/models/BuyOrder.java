package models;

public class BuyOrder extends Order {
    public BuyOrder(Trader trader, Stock stock, int quantity) {
        super(trader, stock, quantity, stock.getCurrentPrice());
    }

    @Override
    public void execute() {
        double totalCost = getQuantity() * getPricePerUnit();
        Account account = getTrader().getAccount();
        if (account.getBalance() < totalCost) {
            throw new IllegalArgumentException("Insufficient balance to execute buy order.");   
        }
        account.setBalance(account.getBalance() - totalCost);
        getTrader().addOrder(this);
    }
           
}