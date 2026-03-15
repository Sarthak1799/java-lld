package models;

import enums.*;

public interface PaymentProcessor {

    void processPayment(double amount);

    PaymentMode getPaymentMode();
}
