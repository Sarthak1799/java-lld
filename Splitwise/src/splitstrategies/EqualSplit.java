package splitstrategies;

public class EqualSplit extends Split {

    public EqualSplit(String userId) {
        super(userId);
    }

    @Override
    public String toString() {
        return "EqualSplit{"
                + "userId='" + userId + '\''
                + ", amount=" + amount
                + '}';
    }
}
