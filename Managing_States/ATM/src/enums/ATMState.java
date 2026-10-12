package enums;

public enum ATMState {
    IDLE,                    // No Card Inserted
    CARD_INSERTED,           // Card Inserted, waiting for pin
    AUTHENTICATED            // PIN verified, ready for transactions
}
