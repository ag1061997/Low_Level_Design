package observers;

import entities.Transaction;

public interface PaymentObserver {
    void onTransactionUpdate(Transaction transaction);
}
