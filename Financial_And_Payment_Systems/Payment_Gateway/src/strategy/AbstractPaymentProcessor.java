package strategy;

import entities.PaymentRequest;
import entities.PaymentResponse;
import enums.PaymentStatus;

public abstract class AbstractPaymentProcessor implements PaymentProcessor {
    private static final int MAX_RETRIES = 3;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        int attempts = 0;
        PaymentResponse response;
        do {
            response = doProcess(request);
            attempts++;
        } while (response.getStatus() == PaymentStatus.FAILED && attempts < MAX_RETRIES);
        return response;
    }

    protected abstract PaymentResponse doProcess(PaymentRequest request);
}
