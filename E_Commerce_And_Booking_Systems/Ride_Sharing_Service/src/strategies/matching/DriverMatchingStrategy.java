package strategies.matching;

import entities.Driver;
import entities.Location;
import enums.RideType;

import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> findDrivers(List<Driver> allDrivers, Location pickupLocation, RideType rideType);
}
