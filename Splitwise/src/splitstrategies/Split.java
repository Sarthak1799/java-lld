package splitstrategies;

public abstract class Split {

    protected String userId;
    protected double amount;

    public Split(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Split{"
                + "userId='" + userId + '\''
                + ", amount=" + amount
                + '}';
    }
}
