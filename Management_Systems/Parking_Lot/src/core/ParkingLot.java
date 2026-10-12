package core;

import entities.ParkingFloor;
import entities.ParkingSpot;
import entities.ParkingTicket;
import entities.Vehicle;
import enums.VehicleSize;
import exceptions.ParkingException;
import strategy.fee.FeeStrategy;
import strategy.parking.SpotAllocationStrategy;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLot {
    private static volatile ParkingLot instance;

    private List<ParkingFloor> floors;
    private final Map<String, ParkingTicket> activeTickets;
    private FeeStrategy feeStrategy;
    private SpotAllocationStrategy allocationStrategy;

    private ParkingLot() {
        this.activeTickets = new ConcurrentHashMap<>();
    }

    public static ParkingLot getInstance() {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot();
                }
            }
        }
        return instance;
    }

    public void initialize(List<ParkingFloor> floors,
                           FeeStrategy feeStrategy,
                           SpotAllocationStrategy allocationStrategy) {
        this.floors = floors;
        this.feeStrategy = feeStrategy;
        this.allocationStrategy = allocationStrategy;
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot;
        while (true) {
            spot = allocationStrategy.findSpot(floors, vehicle.getSize());

            if (spot == null) {
                throw new ParkingException("No available spot for vehicle size: " +
                        vehicle.getSize());
            }

            try {
                spot.parkVehicle(vehicle);
                break;
            } catch (ParkingException e) {
            }
        }

        String ticketId = UUID.randomUUID().toString().substring(0, 8);
        ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, spot);
        activeTickets.put(ticketId, ticket);

        System.out.println("Parked " + vehicle + " at spot " + spot.getSpotId() +
                ". Ticket: " + ticketId);

        return ticket;
    }

    public double unparkVehicle(String ticketId) {
        ParkingTicket ticket = activeTickets.remove(ticketId);

        if (ticket == null) {
            throw new ParkingException("Invalid ticket: " + ticketId);
        }

        ticket.setExitTime(java.time.LocalDateTime.now());
        double fee = feeStrategy.calculateFee(ticket);

        ticket.getSpot().unparkVehicle();

        System.out.printf("Unparked %s from spot %s. Fee: $%.2f%n",
                ticket.getVehicle(), ticket.getSpot().getSpotId(), fee);

        return fee;
    }

    public void displayAvailability() {
        System.out.println("\n===== PARKING AVAILABILITY =====");
        for (ParkingFloor floor : floors) {
            StringBuilder sb = new StringBuilder();
            sb.append("Floor ").append(floor.getFloorNumber()).append(": ");

            for (VehicleSize size : VehicleSize.values()) {
                int count = 0;
                for (ParkingSpot spot : floor.getSpots()) {
                    if (spot.isAvailable() && spot.getSize() == size) {
                        count++;
                    }
                }
                sb.append(size).append("=").append(count);
                if (size.ordinal() < VehicleSize.values().length - 1) {
                    sb.append(", ");
                }
            }
            System.out.println(sb);
        }
        System.out.println("================================\n");
    }

    public void setFeeStrategy(FeeStrategy feeStrategy) { this.feeStrategy = feeStrategy; }
    public void setAllocationStrategy(SpotAllocationStrategy allocationStrategy) { this.allocationStrategy = allocationStrategy; }
}
