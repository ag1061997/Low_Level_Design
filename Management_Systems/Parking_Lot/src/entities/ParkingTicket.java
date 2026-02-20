package entities;

import java.util.Date;
import java.util.UUID;

public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final long entryTime;
    private long exitTime;

    public ParkingTicket(Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.parkingSpot = spot;
        this.entryTime = new Date().getTime();
    }

    public String getTicketId() {
        return ticketId;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }
    public long getEntryTime() {
        return entryTime;
    }
    public long getExitTime() {
        return exitTime;
    }
    public void setExitTime() {
        exitTime = new Date().getTime();
    }
}
