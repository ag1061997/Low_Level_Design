package strategy.fine;

import entities.Loan;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PerDayFineStrategy implements FineStrategy {
    private static final double RATE_PER_DAY = 1.0;
    private static final int LOAN_PERIOD_DAYS = 14;

    @Override
    public double calculateFine(Loan loan, LocalDate returnDate) {
        LocalDate dueDate = loan.getCheckoutDate().plusDays(LOAN_PERIOD_DAYS);
        long overdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        return overdue > 0 ? overdue * RATE_PER_DAY : 0.0;
    }
}
