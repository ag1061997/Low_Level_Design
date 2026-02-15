package strategy.parking;

import entities.ParkingFloor;
import entities.ParkingSpot;
import entities.Vehicle;

import java.util.List;
import java.util.Optional;

public interface ParkingStrategy {
    Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle);
}
