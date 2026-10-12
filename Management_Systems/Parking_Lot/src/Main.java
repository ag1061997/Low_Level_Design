import core.ParkingLot;
import entities.*;
import enums.VehicleSize;
import exceptions.ParkingException;
import strategy.fee.HourlyFeeStrategy;
import strategy.parking.BestFitStrategy;
import strategy.parking.NearestFirstStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<VehicleSize, Integer> floor1Counts = new HashMap<>();
        floor1Counts.put(VehicleSize.SMALL, 2);
        floor1Counts.put(VehicleSize.MEDIUM, 2);
        floor1Counts.put(VehicleSize.LARGE, 1);

        Map<VehicleSize, Integer> floor2Counts = new HashMap<>();
        floor2Counts.put(VehicleSize.SMALL, 1);
        floor2Counts.put(VehicleSize.MEDIUM, 1);
        floor2Counts.put(VehicleSize.LARGE, 1);

        List<ParkingFloor> floors = List.of(
                new ParkingFloor(1, floor1Counts),
                new ParkingFloor(2, floor2Counts)
        );

        ParkingLot lot = ParkingLot.getInstance();
        lot.initialize(floors, new HourlyFeeStrategy(10.0), new NearestFirstStrategy());

        lot.displayAvailability();

        System.out.println("========== SCENARIO 1: Park Vehicles (Nearest First) ==========");
        ParkingTicket bikeTicket = lot.parkVehicle(new Vehicle("KA-01-1111", VehicleSize.SMALL));
        ParkingTicket carTicket = lot.parkVehicle(new Vehicle("KA-02-2222", VehicleSize.MEDIUM));
        lot.parkVehicle(new Vehicle("KA-03-3333", VehicleSize.LARGE));

        lot.displayAvailability();

        System.out.println("========== SCENARIO 2: Park With Best Fit ==========");
        lot.setAllocationStrategy(new BestFitStrategy());
        lot.parkVehicle(new Vehicle("KA-04-4444", VehicleSize.MEDIUM));

        System.out.println("\n========== SCENARIO 3: Unpark Vehicles ==========");
        lot.unparkVehicle(bikeTicket.getTicketId());
        lot.unparkVehicle(carTicket.getTicketId());

        lot.displayAvailability();

        System.out.println("========== SCENARIO 4: No Spot Available ==========");
        try {
            lot.setAllocationStrategy(new NearestFirstStrategy());
            lot.parkVehicle(new Vehicle("KA-05-5555", VehicleSize.LARGE));
            lot.parkVehicle(new Vehicle("KA-06-6666", VehicleSize.LARGE));
        } catch (ParkingException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
    }
}