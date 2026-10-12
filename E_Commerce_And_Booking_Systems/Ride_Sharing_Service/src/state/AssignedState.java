package state;

import entities.Driver;
import entities.Trip;
import enums.TripStatus;

public class AssignedState implements TripState {
    @Override
    public TripStatus getStatus() { return TripStatus.ASSIGNED; }

    @Override
    public void assign(Trip trip, Driver driver) {
        System.out.println("Trip is already assigned. To re-assign, cancel first.");
    }

    @Override
    public void start(Trip trip) {
        trip.setState(new InProgressState());
    }

    @Override
    public void end(Trip trip) {
        System.out.println("Cannot end a trip that has not started.");
    }
}
