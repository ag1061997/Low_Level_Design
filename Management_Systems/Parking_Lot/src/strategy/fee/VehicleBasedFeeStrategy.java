package strategy.fee;

import entities.ParkingTicket;
import enums.VehicleSize;

import java.util.Map;

public class VehicleBasedFeeStrategy implements FeeStrategy {
    private final Map<VehicleSize, Double> ratesPerHour;

    public VehicleBasedFeeStrategy(Map<VehicleSize, Double> ratesPerHour) {
        this.ratesPerHour = ratesPerHour;
    }

    @Override
    public double calculateFee(ParkingTicket ticket) {
        VehicleSize size = ticket.getVehicle().getSize();
        double rate = ratesPerHour.getOrDefault(size, 0.0);
        return ticket.getDurationInHours() * rate;
    }
}
