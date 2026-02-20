package strategy.fee;

import entities.ParkingTicket;

public interface FeeStrategy {
    double calculatefee(ParkingTicket parkingTicket);
}
