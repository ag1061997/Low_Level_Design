package core;

import enums.TransactionType;
import exceptions.ATMException;
import models.Account;
import models.Card;
import models.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BankService {
    private final Map<String, Account> accounts;
    private final Map<String, Card> cards;
    private final Map<String, List<Transaction>> transactionHistory;
    private final Map<String, Integer> failedAttempts;
    private final Set<String> blockedCards;

    private static final int MAX_ATTEMPTS = 3;
    private int transactionCounter = 0;

    public BankService() {
        this.accounts = new ConcurrentHashMap<>();
        this.cards = new ConcurrentHashMap<>();
        this.transactionHistory = new ConcurrentHashMap<>();
        this.failedAttempts = new ConcurrentHashMap<>();
        this.blockedCards = ConcurrentHashMap.newKeySet();
    }

    public void createAccount(String accountNumber, double initialBalance) {
        accounts.put(accountNumber, new Account(accountNumber, initialBalance));
    }

    public void createCard(String cardNumber, String pin, String accountNumber) {
        if (!accounts.containsKey(accountNumber)) {
            throw new ATMException("Account " + accountNumber + " does not exist");
        }
        cards.put(cardNumber, new Card(cardNumber, pin, accountNumber));
    }

    public Account authenticate(String cardNumber, String pin) {
        if (blockedCards.contains(cardNumber)) {
            throw new ATMException("Card is blocked. Contact your bank.");
        }
        Card card = cards.get(cardNumber);
        if (card == null || !card.getPin().equals(pin)) {
            int attempts = failedAttempts.merge(cardNumber, 1, Integer::sum);
            if (attempts >= MAX_ATTEMPTS) {
                blockedCards.add(cardNumber);
            }
            throw new ATMException("Incorrect PIN");
        }
        failedAttempts.remove(cardNumber);
        Account account = accounts.get(card.getAccountNumber());
        if (account == null) {
            throw new ATMException("Account not found for card " + cardNumber);
        }
        return account;
    }

    public double getBalance(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new ATMException("Account " + accountNumber + " not found");
        }
        return account.getBalance();
    }

    public void debit(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new ATMException("Account " + accountNumber + " not found");
        }
        account.debit(amount);
    }

    public void credit(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new ATMException("Account " + accountNumber + " not found");
        }
        account.credit(amount);
    }

    public synchronized Transaction recordTransaction(TransactionType type, double amount, String accountNumber) {
        String id = "TXN" + (++transactionCounter);
        Transaction transaction = new Transaction(id, type, amount, accountNumber);
        transactionHistory.computeIfAbsent(accountNumber, k -> new ArrayList<>()).add(transaction);
        return transaction;
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionHistory.getOrDefault(accountNumber, new ArrayList<>());
    }
}
