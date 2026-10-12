package observer;

import entities.Trip;

public interface TripObserver {
    void onUpdate(Trip trip);
}
