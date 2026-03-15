package splitstrategies;

public class ExactSplit extends Split {

    public ExactSplit(String userId, double amount) {
        super(userId);
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ExactSplit{"
                + "userId='" + userId + '\''
                + ", amount=" + amount
                + '}';
    }
}
