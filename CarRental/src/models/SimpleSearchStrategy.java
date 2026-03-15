package models;

import java.util.*;

public class SimpleSearchStrategy implements SearchStrategy {

    @Override
    public List<Car> search(List<Car> cars) {
        // Simple search implementation: return all available cars
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.isAvailable()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }
}
