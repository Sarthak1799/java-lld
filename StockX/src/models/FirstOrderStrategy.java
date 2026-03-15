package models;
import java.util.*;

public class FirstOrderStrategy implements ExecutionStrategy {
    @Override
    public void execute(List<Order> orders) {
        if (orders.isEmpty()) {
            return;
        }
        Order firstOrder = orders.get(0);
        firstOrder.execute();
        orders.remove(0);
    }
}