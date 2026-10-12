package strategy.fee;

import entities.ParkingTicket;

public interface FeeStrategy {
    double calculateFee(ParkingTicket ticket);
}
