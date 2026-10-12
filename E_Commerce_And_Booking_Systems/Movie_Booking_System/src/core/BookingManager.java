package core;

import entities.*;
import enums.PaymentStatus;
import strategies.payment.PaymentStrategy;

import java.util.List;
import java.util.Optional;

public class BookingManager {
    private final SeatLockManager seatLockManager;

    public BookingManager(SeatLockManager seatLockManager) {
        this.seatLockManager = seatLockManager;
    }

    public Optional<Booking> createBooking(User user, Show show, List<Seat> seats, PaymentStrategy paymentStrategy) {
        if (!seatLockManager.lockSeats(show, seats, user.getId())) {
            System.out.println("Could not lock the selected seats. They may already be taken.");
            return Optional.empty();
        }

        double totalAmount = show.getPricingStrategy().calculatePrice(seats, show.getSeatPrices());

        Payment payment = paymentStrategy.pay(totalAmount);
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            System.out.println("Payment failed. Releasing seats.");
            seatLockManager.unlockSeats(show, seats, user.getId());
            return Optional.empty();
        }

        if (!seatLockManager.confirmSeats(show, seats, user.getId())) {
            System.out.println("Seat lock expired before payment completed. Refunding payment.");
            paymentStrategy.refund(payment);
            return Optional.empty();
        }

        Booking booking = new Booking(user, show, seats, totalAmount, payment);
        return Optional.of(booking);
    }
}
