package models;

import enums.*;
import java.util.*;

public class Car {

    private String id;
    private String model;
    private String licensePlate;
    private ReservationStatus reservationStatus;
    private CarType carType;
    private double rentPerDay;

    public Car(String model, String licensePlate, CarType carType, double rentPerDay) {
        this.id = "car-" + UUID.randomUUID().toString();
        this.model = model;
        this.licensePlate = licensePlate;
        this.carType = carType;
        this.rentPerDay = rentPerDay;
        this.reservationStatus = ReservationStatus.AVAILABLE;
    }

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public CarType getCarType() {
        return carType;
    }

    public double getRentPerDay() {
        return rentPerDay;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public boolean isAvailable() {
        return this.reservationStatus == ReservationStatus.AVAILABLE;
    }
}
