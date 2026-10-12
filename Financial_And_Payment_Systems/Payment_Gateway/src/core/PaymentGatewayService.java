package core;

import entities.PaymentRequest;
import entities.PaymentResponse;
import entities.Transaction;
import enums.PaymentStatus;
import factory.PaymentProcessorFactory;
import observers.PaymentObserver;
import strategy.PaymentProcessor;

import java.util.ArrayList;
import java.util.List;

public class PaymentGatewayService {
    private final List<PaymentObserver> observers = new ArrayList<>();

    public void addObserver(PaymentObserver observer) { observers.add(observer); }
    public void removeObserver(PaymentObserver observer) { observers.remove(observer); }
    private void notifyObservers(Transaction transaction) { observers.forEach(o -> o.onTransactionUpdate(transaction)); }

    private void updateStatus(Transaction transaction, PaymentStatus status) {
        transaction.setStatus(status);
        notifyObservers(transaction);
    }

    public Transaction processPayment(PaymentRequest request) {
        Transaction transaction = new Transaction(request);
        try {
            PaymentProcessor processor = PaymentProcessorFactory.getProcessor(request.getPaymentMethod());
            PaymentResponse response = processor.processPayment(request);
            updateStatus(transaction, response.getStatus());
        } catch (Exception e) {
            System.err.println("Payment processing failed: " + e.getMessage());
            updateStatus(transaction, PaymentStatus.FAILED);
        }
        return transaction;
    }
}
