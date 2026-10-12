package strategy.fee;

import entities.ParkingTicket;

public class FlatRateFeeStrategy implements FeeStrategy {
    private final double flatRate;

    public FlatRateFeeStrategy(double flatRate) {
        this.flatRate = flatRate;
    }

    @Override
    public double calculateFee(ParkingTicket ticket) {
        return flatRate;
    }
}
