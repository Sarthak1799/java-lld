package models;
import java.util.*;

public interface ExecutionStrategy {
    void execute(List<Order> orders);
}