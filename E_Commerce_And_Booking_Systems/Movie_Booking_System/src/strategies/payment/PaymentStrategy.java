package strategies.payment;

import entities.Payment;

public interface PaymentStrategy {
    Payment pay(double amount);
    void refund(Payment payment);
}
