package services;

import entities.BookCopy;
import entities.Loan;
import entities.Member;

import java.util.HashMap;
import java.util.Map;

public class TransactionService {
    private static volatile TransactionService INSTANCE;
    private final Map<String, Loan> activeLoans = new HashMap<>(); // Key: BookCopy ID

    private TransactionService() {}
    public static TransactionService getInstance() {
        if (INSTANCE == null) {
            synchronized (TransactionService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TransactionService();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized void createLoan(BookCopy copy, Member member) {
        if (activeLoans.containsKey(copy.getId())) { throw new IllegalStateException("This copy is already on loan."); }
        Loan loan = new Loan(copy, member);
        activeLoans.put(copy.getId(), loan);
        member.addLoan(loan);
    }

    public void endLoan(BookCopy copy) {
        Loan loan = activeLoans.remove(copy.getId());
        if (loan != null) {
            loan.getMember().removeLoan(loan);
        }
    }
}
