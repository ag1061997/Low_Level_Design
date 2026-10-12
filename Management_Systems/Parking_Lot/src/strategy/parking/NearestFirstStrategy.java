package strategy.parking;

import entities.ParkingFloor;
import entities.ParkingSpot;
import enums.VehicleSize;

import java.util.List;

public class NearestFirstStrategy implements SpotAllocationStrategy {
    @Override
    public ParkingSpot findSpot(List<ParkingFloor> floors, VehicleSize size) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findAvailableSpot(size);
            if (spot != null) {
                return spot;
            }
        }
        return null;
    }
}
