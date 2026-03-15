package service;

import enums.*;
import java.time.LocalDate;
import java.util.*;
import models.*;

public class CarService {

    private List<Car> carInventory;
    private Map<String, Reservation> reservations;
    private Map<String, User> users;
    private static CarService instance = null;

    public CarService() {
        this.carInventory = new ArrayList<>();
        this.reservations = new HashMap<>();
        this.users = new HashMap<>();
    }

    public static CarService getInstance() {
        if (instance == null) {
            instance = new CarService();
            instance.initializeSampleData();
        }
        return instance;
    }

    private void initializeSampleData() {
        // Sample Cars
        carInventory.add(new Car("Toyota Camry", "ABC123", CarType.SEDAN, 50.0));
        carInventory.add(new Car("Honda CRV", "XYZ789", CarType.SUV, 70.0));
        carInventory.add(new Car("Ford Mustang", "MUS456", CarType.CONVERTIBLE, 100.0));
    }

    public void registerUser(String name, String email, String phoneNumber) {
        User user = new User(name, email, phoneNumber);
        users.put(user.getId(), user);
    }

    public User getUserById(String userId) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        return users.get(userId);
    }

    public List<Car> listCars() {
        return carInventory;
    }

    public List<Car> searchCars(SearchStrategy strategy) {
        return strategy.search(carInventory);
    }

    public String reserveCar(String carId, String userId, LocalDate startDate, LocalDate endDate, PaymentMode paymentMode) {
        Car carToReserve = null;
        for (Car car : carInventory) {
            if (car.getId().equals(carId) && car.isAvailable()) {
                carToReserve = car;
                break;
            }
        }
        if (carToReserve == null) {
            throw new IllegalArgumentException("Car not available for reservation");
        }

        Reservation reservation = new Reservation(carId, userId, startDate, endDate, paymentMode);
        reservations.put(reservation.getId(), reservation);
        carToReserve.setReservationStatus(ReservationStatus.RESERVED);

        User user = getUserById(userId);
        user.addReservation(reservation.getId());

        return reservation.getId();
    }

    public void cancelReservation(String reservationId) {
        if (!reservations.containsKey(reservationId)) {
            throw new IllegalArgumentException("Reservation not found");
        }
        Reservation reservation = reservations.get(reservationId);
        reservation.setStatus(ReservationStatus.CANCELLED);

        for (Car car : carInventory) {
            if (car.getId().equals(reservation.getCarId())) {
                car.setReservationStatus(ReservationStatus.AVAILABLE);
                break;
            }
        }
    }

    public void completeReservation(String reservationId, PaymentProcessor paymentProcessor, LocalDate currentDate) {
        if (!reservations.containsKey(reservationId)) {
            throw new IllegalArgumentException("Reservation not found");
        }
        Reservation reservation = reservations.get(reservationId);
        PaymentMode reservationMode = reservation.getPaymentMode();
        if (!reservationMode.equals(paymentProcessor.getPaymentMode())) {
            throw new IllegalArgumentException("Payment mode mismatch");
        }

        Car car = null;
        for (Car c : carInventory) {
            if (c.getId().equals(reservation.getCarId())) {
                car = c;
                break;
            }
        }
        if (car == null) {
            throw new IllegalArgumentException("Car not found for the reservation");
        }

        var days = reservation.getStartDate().until(currentDate).getDays();
        double amount = days * car.getRentPerDay();

        paymentProcessor.processPayment(amount);
        reservation.setStatus(ReservationStatus.COMPLETED);
        car.setReservationStatus(ReservationStatus.AVAILABLE);
    }

    public List<Reservation> listReservationForUser(String userId) {
        User user = getUserById(userId);
        List<Reservation> userReservations = new ArrayList<>();
        for (String resId : user.getReservations()) {
            if (reservations.containsKey(resId)) {
                userReservations.add(reservations.get(resId));
            }
        }
        return userReservations;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
