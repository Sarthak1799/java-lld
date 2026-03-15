package models;

public class Balance {

    private String userId1;  // who owes
    private String userId2;  // to whom
    private double amount;

    public Balance(String userId1, String userId2, double amount) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.amount = amount;
    }

    public String getUserId1() {
        return userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Balance{"
                + "userId1='" + userId1 + '\''
                + ", userId2='" + userId2 + '\''
                + ", amount=" + amount
                + '}';
    }
}
