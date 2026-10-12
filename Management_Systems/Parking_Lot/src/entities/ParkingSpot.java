package entities;

import enums.VehicleSize;
import exceptions.ParkingException;

public class ParkingSpot {
    private final String spotId;
    private final VehicleSize size;
    private volatile Vehicle parkedVehicle;
    private final boolean chargingPoint;

    public ParkingSpot(String spotId, VehicleSize size, boolean chargingPoint) {
        this.spotId = spotId;
        this.size = size;
        this.parkedVehicle = null;
        this.chargingPoint = chargingPoint;
    }

    public boolean isAvailable() { return parkedVehicle == null; }
    public boolean hasCharger() { return chargingPoint; }
    public boolean canFitVehicle(VehicleSize vehicleSize) { return vehicleSize.ordinal() <= this.size.ordinal(); }

    public synchronized void parkVehicle(Vehicle vehicle) {
        if (!isAvailable()) {
            throw new ParkingException("Spot " + spotId + " is already occupied");
        }
        if (!canFitVehicle(vehicle.getSize())) {
            throw new ParkingException("Vehicle size " + vehicle.getSize() +
                    " cannot fit in spot size " + size);
        }
        this.parkedVehicle = vehicle;
    }

    public synchronized Vehicle unparkVehicle() {
        if (isAvailable()) {
            throw new ParkingException("Spot " + spotId + " is already empty");
        }
        Vehicle vehicle = this.parkedVehicle;
        this.parkedVehicle = null;
        return vehicle;
    }

    public String getSpotId() { return spotId; }
    public VehicleSize getSize() { return size; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }
}
