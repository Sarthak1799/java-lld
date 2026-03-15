package models;

public class Stock {

    private String id;
    private String symbol;
    private String companyName;
    private double currentPrice;
    private int volume;

    public Stock(String symbol, String companyName, double currentPrice, int volume) {
        this.id = "stock-" + java.util.UUID.randomUUID().toString();
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.volume = volume;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return companyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public synchronized double getCurrentPrice() {
        return currentPrice;
    }

    public synchronized void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
