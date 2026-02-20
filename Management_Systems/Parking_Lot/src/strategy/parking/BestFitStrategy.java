package strategy.parking;

import entities.ParkingFloor;
import entities.ParkingSpot;
import entities.Vehicle;

import java.util.List;
import java.util.Optional;

public class BestFitStrategy implements ParkingStrategy {
    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        Optional<ParkingSpot> bestSpot = Optional.empty();

        for(ParkingFloor floor:floors) {
            Optional<ParkingSpot> spot = floor.findAvailableSpot(vehicle);
            if (spot.isPresent()) {
                if(bestSpot.isEmpty()) {
                    bestSpot = spot;
                } else {
                    if(spot.get().getSpotSize().ordinal() < bestSpot.get().getSpotSize().ordinal()) {
                        bestSpot = spot;
                    }
                }
            }
        }
        return bestSpot;
    }
}
