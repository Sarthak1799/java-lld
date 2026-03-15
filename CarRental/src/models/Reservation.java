package models;

import java.util.*;
import enums.*;
import java.time.LocalDate;

public class Reservation {

    private String id;
    private String carId;
    private String userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationStatus status;
    private PaymentMode paymentMode;

    public Reservation(String carId, String userId, LocalDate startDate, LocalDate endDate, PaymentMode paymentMode) {
        this.id = "res-" + UUID.randomUUID().toString();
        this.carId = carId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ReservationStatus.RESERVED;
        this.paymentMode = paymentMode;
    }

    public String getId() {
        return id;
    }

    public String getCarId() {
        return carId;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
