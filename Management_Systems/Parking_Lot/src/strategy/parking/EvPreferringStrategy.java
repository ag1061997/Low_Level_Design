package strategy.parking;

import entities.ParkingFloor;
import entities.ParkingSpot;
import enums.VehicleSize;

import java.util.List;

public class EvPreferringStrategy implements SpotAllocationStrategy {
    @Override
    public ParkingSpot findSpot(List<ParkingFloor> floors, VehicleSize size) {
        for (ParkingFloor floor : floors) {
            for (ParkingSpot spot : floor.getSpots()) {
                if (spot.isAvailable() && spot.canFitVehicle(size) && spot.hasCharger()) {
                    return spot;
                }
            }
        }
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findAvailableSpot(size);
            if (spot != null) return spot;
        }
        return null;
    }
}
