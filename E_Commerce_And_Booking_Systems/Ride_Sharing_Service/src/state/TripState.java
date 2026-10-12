package state;

import entities.Driver;
import entities.Trip;
import enums.TripStatus;

public interface TripState {
    TripStatus getStatus();
    void assign(Trip trip, Driver driver);
    void start(Trip trip);
    void end(Trip trip);
}
