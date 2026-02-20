package entities;

import enums.VehicleSize;

public class Truck extends Vehicle {
    public Truck(String licenseNumber) {
        super(licenseNumber, VehicleSize.LARGE);
    }
}
