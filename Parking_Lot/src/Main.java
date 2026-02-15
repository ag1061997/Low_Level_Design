import entities.*;
import enums.VehicleSize;
import strategy.fee.VehicleBasedStrategy;

import java.util.Optional;

public class Main {
    public static void main(String[] args) throws Exception {
        ParkingLot parkingLot = ParkingLot.getInstance();

        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSpot(new ParkingSpot("1.1", VehicleSize.SMALL));
        floor1.addSpot(new ParkingSpot("1.2", VehicleSize.MEDIUM));
        floor1.addSpot(new ParkingSpot("1.3", VehicleSize.LARGE));

        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSpot(new ParkingSpot("2.1", VehicleSize.SMALL));
        floor2.addSpot(new ParkingSpot("2.2", VehicleSize.MEDIUM));

        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);

        parkingLot.setFeeStrategy(new VehicleBasedStrategy());

        floor1.displayAvailability();
        floor2.displayAvailability();

        Vehicle bike = new Bike("B1");
        Vehicle car = new Car("C1");
        Vehicle truck = new Truck("T1");

        ParkingTicket bikeTicket = parkingLot.parkVehicle(bike);
        ParkingTicket carTicket = parkingLot.parkVehicle(car);
        ParkingTicket truckTicket = parkingLot.parkVehicle(truck);

        floor1.displayAvailability();
        floor2.displayAvailability();

        Vehicle car2 = new Car("C2");
        ParkingTicket carTicket2 = parkingLot.parkVehicle(car2);

        Vehicle bike2 = new Bike("B1");
        ParkingTicket bikeTicket2 = parkingLot.parkVehicle(bike2);

        floor1.displayAvailability();
        floor2.displayAvailability();

        double feeOpt = parkingLot.unparkVehicle(car.getLicenseNumber());
        System.out.println(feeOpt);
    }
}