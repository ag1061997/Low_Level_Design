package state;

import entities.Driver;
import entities.Trip;
import enums.TripStatus;

public class RequestedState implements TripState {
    @Override
    public TripStatus getStatus() { return TripStatus.REQUESTED; }

    @Override
    public void assign(Trip trip, Driver driver) {
        trip.setDriver(driver);
        trip.setState(new AssignedState());
    }

    @Override
    public void start(Trip trip) {
        System.out.println("Cannot start a trip that has not been assigned a driver.");
    }

    @Override
    public void end(Trip trip) {
        System.out.println("Cannot end a trip that has not been assigned a driver.");
    }
}
