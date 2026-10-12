package factory;

import enums.PaymentMethod;
import strategy.CreditCardProcessor;
import strategy.PayPalProcessor;
import strategy.PaymentProcessor;
import strategy.UPIProcessor;

public class PaymentProcessorFactory {
    public static PaymentProcessor getProcessor(PaymentMethod method) {
        return switch (method) {
            case CREDIT_CARD -> new CreditCardProcessor();
            case UPI -> new UPIProcessor();
            case PAYPAL -> new PayPalProcessor();
            default -> throw new IllegalArgumentException("Unsupported payment method: " + method);
        };
    }
}
