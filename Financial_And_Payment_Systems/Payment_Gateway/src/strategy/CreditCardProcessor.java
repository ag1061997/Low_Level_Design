package strategy;

import entities.PaymentRequest;
import entities.PaymentResponse;
import enums.PaymentStatus;

public class CreditCardProcessor extends AbstractPaymentProcessor {
    @Override
    protected PaymentResponse doProcess(PaymentRequest request) {
        System.out.println("Processing credit card payment of amount " + request.getAmount() + " " + request.getCurrency());
        return new PaymentResponse(PaymentStatus.SUCCESSFUL, "Credit Card payment successful.");
    }
}
