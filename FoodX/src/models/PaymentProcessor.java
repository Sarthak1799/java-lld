
public interface PaymentProcessor {

    boolean processPayment(double amount, PaymentMethod paymentMethod);

    boolean refund(double amount, PaymentMethod paymentMethod);
}
