package strategy.fine;

import entities.Loan;

import java.time.LocalDate;

public interface FineStrategy {
    double calculateFine(Loan loan, LocalDate returnDate);
}
