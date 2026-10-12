package entities;

import enums.DriverStatus;
import enums.TripStatus;

public class Driver extends User {
    private Vehicle vehicle;
    private Location currentLocation;
    private DriverStatus status;

    public Driver(String name, String contact, Vehicle vehicle, Location initialLocation) {
        super(name, contact);
        this.vehicle = vehicle;
        this.currentLocation = initialLocation;
        this.status = DriverStatus.OFFLINE;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
        System.out.println("Driver " + getName() + " is now " + status);
    }

    public synchronized boolean tryClaimForTrip() {
        if (status != DriverStatus.ONLINE) {
            return false;
        }
        setStatus(DriverStatus.IN_TRIP);
        return true;
    }

    public Vehicle getVehicle() { return vehicle; }
    public DriverStatus getStatus() { return status;}
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }

    @Override
    public void onUpdate(Trip trip) {
        System.out.printf("--- Notification for Driver %s ---\n", getName());
        System.out.printf("  Trip %s status: %s.\n", trip.getId(), trip.getStatus());
        if (trip.getStatus() == TripStatus.REQUESTED) {
            System.out.println("  A new ride is available for you to accept.");
        }
        System.out.println("--------------------------------\n");
    }
}
