package strategy;

import entities.PaymentRequest;
import entities.PaymentResponse;
import enums.PaymentStatus;

public class UPIProcessor extends AbstractPaymentProcessor {
    @Override
    protected PaymentResponse doProcess(PaymentRequest request) {
        System.out.println("Processing UPI payment of " + request.getAmount() + " " + request.getCurrency());
        return new PaymentResponse(PaymentStatus.SUCCESSFUL, "UPI payment successful.");
    }
}
