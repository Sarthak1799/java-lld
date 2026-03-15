package models;

import java.util.*;

public class User {

    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private List<String> reservations; // List of reservation IDs

    public User(String name, String email, String phoneNumber) {
        this.id = "user-" + UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.reservations = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getReservations() {
        return reservations;
    }

    public void addReservation(String reservationId) {
        this.reservations.add(reservationId);
    }
}
