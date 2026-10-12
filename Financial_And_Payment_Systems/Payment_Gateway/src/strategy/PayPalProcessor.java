package strategy;

import entities.PaymentRequest;
import entities.PaymentResponse;
import enums.PaymentStatus;

public class PayPalProcessor extends AbstractPaymentProcessor {
    @Override
    protected PaymentResponse doProcess(PaymentRequest request) {
        System.out.println("Redirecting to PayPal for payer " + request.getPayerId());
        return new PaymentResponse(PaymentStatus.SUCCESSFUL, "Paypal payment successful.");
    }
}
