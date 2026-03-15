package models;

import enums.*;
import java.util.*;

public class CarTypeSearchStrategy implements SearchStrategy {

    private CarType carType;

    public CarTypeSearchStrategy(CarType carType) {
        this.carType = carType;
    }

    @Override
    public List<Car> search(List<Car> cars) {
        List<Car> result = new ArrayList<>();
        for (Car car : cars) {
            if (car.getCarType() == this.carType && car.isAvailable()) {
                result.add(car);
            }
        }
        return result;
    }
}
