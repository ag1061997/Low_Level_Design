package strategy.fee;

import entities.ParkingTicket;

public class FlatFeeStrategy implements FeeStrategy {
    private static final double RATE_PER_HOUR = 10.0;

    @Override
    public double calculatefee(ParkingTicket parkingTicket) {
        long duration = parkingTicket.getExitTime() - parkingTicket.getEntryTime();
        long hours = (duration / (1000 * 60 * 60)) + 1;
        return hours * RATE_PER_HOUR;
    }
}
