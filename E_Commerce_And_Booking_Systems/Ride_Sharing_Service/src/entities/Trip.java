package entities;

import enums.TripStatus;
import observer.TripObserver;
import state.RequestedState;
import state.TripState;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Trip {
    private final String id;
    private final Rider rider;
    private Driver driver;
    private final Location pickupLocation;
    private final Location dropoffLocation;
    private final double fare;

    private TripState currentState;
    private final List<TripObserver> observers = new CopyOnWriteArrayList<>();

    private Trip(TripBuilder builder) {
        this.id = builder.id;
        this.rider = builder.rider;
        this.pickupLocation = builder.pickupLocation;
        this.dropoffLocation = builder.dropoffLocation;
        this.fare = builder.fare;
        this.currentState = new RequestedState(); // Initial state
        addObserver(this.rider);
    }

    public void addObserver(TripObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        observers.forEach(o -> o.onUpdate(this));
    }

    public synchronized void assignDriver(Driver driver) {
        TripState previousState = currentState;
        currentState.assign(this, driver);
        if (currentState != previousState) {
            addObserver(driver);
            notifyObservers();
        }
    }

    public synchronized void startTrip() {
        TripState previousState = currentState;
        currentState.start(this);
        if (currentState != previousState) {
            notifyObservers();
        }
    }

    public synchronized void endTrip() {
        TripState previousState = currentState;
        currentState.end(this);
        if (currentState != previousState) {
            notifyObservers();
        }
    }

    public String getId() { return id; }
    public Rider getRider() { return rider; }
    public Driver getDriver() { return driver; }
    public Location getPickupLocation() { return pickupLocation; }
    public Location getDropoffLocation() { return dropoffLocation; }
    public double getFare() { return fare; }
    public TripStatus getStatus() { return currentState.getStatus(); }

    public void setState(TripState state) {
        this.currentState = state;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public static class TripBuilder {
        private final String id;
        private Rider rider;
        private Location pickupLocation;
        private Location dropoffLocation;
        private double fare;

        public TripBuilder() {
            this.id = UUID.randomUUID().toString();
        }

        public TripBuilder withRider(Rider rider) {
            this.rider = rider;
            return this;
        }

        public TripBuilder withPickupLocation(Location pickupLocation) {
            this.pickupLocation = pickupLocation;
            return this;
        }

        public TripBuilder withDropoffLocation(Location dropoffLocation) {
            this.dropoffLocation = dropoffLocation;
            return this;
        }

        public TripBuilder withFare(double fare) {
            this.fare = fare;
            return this;
        }

        public Trip build() {
            if (rider == null || pickupLocation == null || dropoffLocation == null) {
                throw new IllegalStateException("Rider, pickup, and dropoff locations are required to build a trip.");
            }
            return new Trip(this);
        }
    }
}
