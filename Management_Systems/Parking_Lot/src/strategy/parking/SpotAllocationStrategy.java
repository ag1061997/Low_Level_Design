package strategy.parking;

import entities.ParkingFloor;
import entities.ParkingSpot;
import enums.VehicleSize;

import java.util.List;

public interface SpotAllocationStrategy {
    ParkingSpot findSpot(List<ParkingFloor> floors, VehicleSize size);
}
