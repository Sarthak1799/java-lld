package models;

public class SellOrder extends Order {
    public SellOrder(Trader trader, Stock stock, int quantity) {
        super(trader, stock, quantity, stock.getCurrentPrice());
    }

    @Override
    public void execute() {
        double totalAmount = getQuantity() * getPricePerUnit();
        Account account = getTrader().getAccount();
        account.setBalance(account.getBalance() + totalAmount);
        getTrader().addOrder(this);
    }
           
}