package models;

import enums.*;

public class CreditCard implements PaymentProcessor {

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    public CreditCard(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public void processPayment(double amount) {
        // Logic to process credit card payment
        System.out.println("Processing credit card payment of $" + amount);
    }

    @Override
    public PaymentMode getPaymentMode() {
        return PaymentMode.CREDIT_CARD;
    }
}
