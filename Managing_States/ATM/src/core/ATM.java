package core;

import enums.ATMState;
import enums.Denomination;
import enums.TransactionType;
import exceptions.ATMException;
import models.Account;
import models.Card;
import states.ATMStateHandler;
import states.AuthenticatedState;
import states.CardInsertedState;
import states.IdleState;

import java.util.HashMap;
import java.util.Map;

public class ATM {
    private static volatile ATM instance;

    private ATMState currentState;
    private final Map<ATMState, ATMStateHandler> stateHandlers;
    private final BankService bankService;
    private final CashDispenser cashDispenser;
    private Card currentCard;
    private Account currentAccount;

    private ATM(BankService bankService, CashDispenser cashDispenser) {
        this.bankService = bankService;
        this.cashDispenser = cashDispenser;
        this.currentState = ATMState.IDLE;

        this.stateHandlers = new HashMap<>();
        stateHandlers.put(ATMState.IDLE, new IdleState());
        stateHandlers.put(ATMState.CARD_INSERTED, new CardInsertedState());
        stateHandlers.put(ATMState.AUTHENTICATED, new AuthenticatedState());
    }

    public static ATM getInstance(BankService bankService, CashDispenser cashDispenser) {
        if (instance == null) {
            synchronized (ATM.class) {
                if (instance == null) {
                    instance = new ATM(bankService, cashDispenser);
                }
            }
        }
        return instance;
    }

    public synchronized void insertCard(Card card) {
        stateHandlers.get(currentState).insertCard(this, card);
    }

    public synchronized void authenticate(String pin) {
        stateHandlers.get(currentState).authenticate(this, pin);
    }

    public synchronized void withdraw(double amount) {
        stateHandlers.get(currentState).withdraw(this, amount);
    }

    public synchronized void deposit(double amount) {
        stateHandlers.get(currentState).deposit(this, amount);
    }

    public synchronized double checkBalance() {
        return stateHandlers.get(currentState).checkBalance(this);
    }

    public synchronized void ejectCard() {
        stateHandlers.get(currentState).ejectCard(this);
    }

    public void performWithdrawal(double amount) {
        int intAmount = (int) amount;
        if (intAmount <= 0 || intAmount % 10 != 0) {
            throw new ATMException("Amount must be a positive multiple of $10");
        }

        String accountNumber = currentAccount.getAccountNumber();
        double balance = bankService.getBalance(accountNumber);
        if (amount > balance) {
            throw new ATMException(
                    "Insufficient funds. Account balance: $" + balance + ", requested: $" + amount
            );
        }

        if (!cashDispenser.canDispense(intAmount)) {
            throw new ATMException("ATM cannot dispense $" + intAmount +
                    " with available denominations");
        }

        Map<Denomination, Integer> dispensed = cashDispenser.dispense(intAmount);
        bankService.debit(accountNumber, amount);

        System.out.println("Dispensing $" + intAmount + ": " + dispensed);
        bankService.recordTransaction(TransactionType.WITHDRAWAL, amount, accountNumber);
    }

    public void performDeposit(double amount) {
        if (amount <= 0) {
            throw new ATMException("Deposit amount must be positive");
        }

        String accountNumber = currentAccount.getAccountNumber();
        bankService.credit(accountNumber, amount);
        System.out.println("Deposited $" + amount + " successfully");
        bankService.recordTransaction(TransactionType.DEPOSIT, amount, accountNumber);
    }

    public double performBalanceInquiry() {
        String accountNumber = currentAccount.getAccountNumber();
        bankService.recordTransaction(TransactionType.BALANCE_ENQUIRY, 0, accountNumber);
        return bankService.getBalance(accountNumber);
    }

    public void setState(ATMState state) {
        this.currentState = state;
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }

    public void setCurrentCard(Card card) {
        this.currentCard = card;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public BankService getBankService() {
        return bankService;
    }

    public ATMState getCurrentState() {
        return currentState;
    }
}
