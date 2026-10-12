package strategy;

import entities.PaymentRequest;
import entities.PaymentResponse;

public interface PaymentProcessor {
    PaymentResponse processPayment(PaymentRequest request);
}
