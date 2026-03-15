
public class CardPaymentProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(double amount, PaymentMethod paymentMethod) {
        // Simulate card payment processing
        System.out.println("Processing payment of $" + amount + " via " + paymentMethod);
        return true;
    }

    @Override
    public boolean refund(double amount, PaymentMethod paymentMethod) {
        // Simulate card refund processing
        System.out.println("Processing refund of $" + amount + " via " + paymentMethod);
        return true;
    }
}
