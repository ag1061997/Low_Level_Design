package strategy.fee;

import entities.ParkingTicket;

public class HourlyFeeStrategy implements FeeStrategy {
    private final double ratePerHour;

    public HourlyFeeStrategy(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    @Override
    public double calculateFee(ParkingTicket ticket) {
        return ticket.getDurationInHours() * ratePerHour;
    }
}
