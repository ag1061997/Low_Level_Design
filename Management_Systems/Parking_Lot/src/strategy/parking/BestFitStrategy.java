package strategy.parking;

import entities.ParkingFloor;
import entities.ParkingSpot;
import enums.VehicleSize;

import java.util.List;

public class BestFitStrategy implements SpotAllocationStrategy {
    @Override
    public ParkingSpot findSpot(List<ParkingFloor> floors, VehicleSize size) {
        for (VehicleSize spotSize : VehicleSize.values()) {
            if (spotSize.ordinal() < size.ordinal()) {
                continue;
            }
            for (ParkingFloor floor : floors) {
                for (ParkingSpot spot : floor.getSpots()) {
                    if (spot.isAvailable() && spot.getSize() == spotSize) {
                        return spot;
                    }
                }
            }
        }
        return null;
    }
}
