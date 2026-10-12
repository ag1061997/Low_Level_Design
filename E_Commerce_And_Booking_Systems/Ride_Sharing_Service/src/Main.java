import core.RideSharingService;
import entities.*;
import enums.RideType;
import strategies.matching.NearestDriverMatchingStrategy;
import strategies.pricing.VehicleBasedPricingStrategy;

public class Main {
    public static void main(String[] args) {
        RideSharingService service = RideSharingService.getInstance();
        service.setDriverMatchingStrategy(new NearestDriverMatchingStrategy());
        service.setPricingStrategy(new VehicleBasedPricingStrategy());

        Rider alice = service.registerRider("Alice", "123-456-7890");

        Driver bob = service.registerDriver("Bob",
                "243-987-2860",
                new Vehicle("KA01-1234", "Toyota Prius", RideType.SEDAN),
                new Location(1.0, 1.0));

        Driver charlie = service.registerDriver("Charlie",
                "313-486-2691",
                new Vehicle("KA02-5678", "Honda CRV", RideType.SUV),
                new Location(2.0, 2.0));

        Driver david = service.registerDriver("David",
                "613-586-3241",
                new Vehicle("KA03-9012", "Honda CRV", RideType.SEDAN),
                new Location(1.2, 1.2));

        service.goOnline(bob.getId());
        service.goOnline(charlie.getId());
        service.goOnline(david.getId());

        david.setCurrentLocation(new Location(10.0, 10.0));

        Location pickupLocation = new Location(0.0, 0.0);
        Location dropoffLocation = new Location(5.0, 5.0);

        Trip trip1 = service.requestRide(alice.getId(), pickupLocation, dropoffLocation, RideType.SEDAN);

        if (trip1 != null) {
            service.acceptRide(bob.getId(), trip1.getId());

            service.startTrip(trip1.getId());
            service.endTrip(trip1.getId());
        }

        System.out.println("\n--- Checking Trip History ---");
        System.out.println("Alice's trip history: " + alice.getTripHistory());
        System.out.println("Bob's trip history: " + bob.getTripHistory());

        System.out.println("\n=============================================");
        Rider harry = service.registerRider("Harry", "167-342-7834");

        Trip trip2 = service.requestRide(harry.getId(),
                new Location(2.5, 2.5),
                new Location(8.0, 8.0),
                RideType.SUV);

        if(trip2 != null) {
            service.acceptRide(charlie.getId(), trip2.getId());
            service.startTrip(trip2.getId());
            service.endTrip(trip2.getId());
        }
    }
}