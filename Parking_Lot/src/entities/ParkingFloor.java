package entities;

import enums.VehicleSize;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ParkingFloor {
    private final int floorNumber;
    private final Map<String, ParkingSpot> spots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ConcurrentHashMap<>();
    }

    public void addSpot(ParkingSpot spot) {
        spots.put(spot.getSpotId(), spot);
    }

    public synchronized Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle) {
        return spots.values().stream()
                .filter(spot -> spot.isAvailable() && spot.canFit(vehicle))
                .sorted(Comparator.comparing(ParkingSpot::getSpotSize))
                .findFirst();
    }

    public void displayAvailability() {
        Map<VehicleSize, Long> availableCounts = spots.values().stream()
                .filter(spot -> spot.isAvailable())
                .collect(Collectors.groupingBy(ParkingSpot::getSpotSize, Collectors.counting()));
        for(VehicleSize size: VehicleSize.values()) {
            System.out.println(size + " " + availableCounts.getOrDefault(size, 0L));
        }
    }
}
