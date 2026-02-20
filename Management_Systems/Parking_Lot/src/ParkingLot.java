import entities.ParkingFloor;
import entities.ParkingSpot;
import entities.ParkingTicket;
import entities.Vehicle;
import strategy.fee.FeeStrategy;
import strategy.fee.FlatFeeStrategy;
import strategy.parking.BestFitStrategy;
import strategy.parking.ParkingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLot {
    private static ParkingLot INSTANCE;
    private final List<ParkingFloor> floors = new ArrayList<>();
    private final Map<String, ParkingTicket> activeTickets;
    private FeeStrategy feeStrategy;
    private ParkingStrategy parkingStrategy;

    private ParkingLot() {
        this.feeStrategy = new FlatFeeStrategy();
        this.parkingStrategy = new BestFitStrategy();
        this.activeTickets = new ConcurrentHashMap<>();
    }

    public static synchronized ParkingLot getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ParkingLot();
        }
        return INSTANCE;
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public void setFeeStrategy(FeeStrategy feeStrategy) {
        this.feeStrategy = feeStrategy;
    }

    public void setParkingStrategy(ParkingStrategy parkingStrategy) {
        this.parkingStrategy = parkingStrategy;
    }

    public synchronized ParkingTicket parkVehicle(Vehicle vehicle) throws Exception {
        Optional<ParkingSpot> parkingSpot = parkingStrategy.findSpot(floors, vehicle);

        if(parkingSpot.isPresent()) {
            ParkingSpot spot = parkingSpot.get();
            spot.parkVehicle(vehicle);
            ParkingTicket parkingTicket = new ParkingTicket(vehicle, spot);
            activeTickets.put(vehicle.getLicenseNumber(), parkingTicket);
            return parkingTicket;
        }
        throw new Exception("No available spot");
    }

    public synchronized double unparkVehicle(String license) throws Exception {
        ParkingTicket ticket = activeTickets.remove(license);
        if(ticket == null) throw new Exception("No Ticket Found");
        ticket.getParkingSpot().unParkVehicle();

        ticket.setExitTime();
        return feeStrategy.calculatefee(ticket);
    }
}
