import core.PaymentGatewayService;
import entities.PaymentRequest;
import enums.PaymentMethod;
import observers.CustomerNotifier;
import observers.MerchantNotifier;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        PaymentGatewayService paymentGateway = new PaymentGatewayService();

        paymentGateway.addObserver(new MerchantNotifier());
        paymentGateway.addObserver(new CustomerNotifier());

        System.out.println("----------- SCENARIO 1: Successful Credit Card Payment -----------");
        PaymentRequest ccRequest = new PaymentRequest(
                "idem-cc-001", "U-123", 150.75, "INR",
                PaymentMethod.CREDIT_CARD, Map.of("cardNumber", "1234..."));

        paymentGateway.processPayment(ccRequest);

        System.out.println("\n----------- SCENARIO 2: Successful PayPal Payment -----------");
        PaymentRequest paypalRequest = new PaymentRequest(
                "idem-pp-001", "U-456", 88.50, "USD",
                PaymentMethod.PAYPAL, Map.of("email", "customer@example.com"));

        paymentGateway.processPayment(paypalRequest);
    }
}