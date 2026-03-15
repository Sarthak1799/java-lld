package splitstrategies;

public class PercentSplit extends Split {

    private double percent;

    public PercentSplit(String userId, double percent) {
        super(userId);
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    @Override
    public String toString() {
        return "PercentSplit{"
                + "userId='" + userId + '\''
                + ", percent=" + percent
                + ", amount=" + amount
                + '}';
    }
}
