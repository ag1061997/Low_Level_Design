package strategy.parking;

import entities.ParkingFloor;
import entities.ParkingSpot;
import entities.Vehicle;

import java.util.*;

public class FarthestFirstStrategy implements ParkingStrategy {
    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        List<ParkingFloor> reverse = new ArrayList<>(floors);
        Collections.reverse(reverse);

        for(ParkingFloor floor:reverse) {
            Optional<ParkingSpot> spot = floor.findAvailableSpot(vehicle);
            if(spot.isPresent()) {
                return spot;
            }
        }
        return Optional.empty();
    }
}
