package core;

import entities.Locker;
import entities.LockerCode;
import entities.LockerLocation;
import entities.Package;
import enums.LockerStatus;
import enums.PackageStatus;
import exceptions.InvalidCodeException;
import exceptions.PackageAlreadyPickedUpException;
import exceptions.PackageExpiredException;
import services.NotificationService;
import strategies.LockerAssignmentStrategy;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class LockerSystem {
    private static volatile LockerSystem instance;

    private final ConcurrentHashMap<String, LockerLocation> locations;
    private final ConcurrentHashMap<String, String> packageLockerMap;
    private final ConcurrentHashMap<String, String> packageLocationMap;
    private final ConcurrentHashMap<String, String> packageCodeMap;
    private LockerAssignmentStrategy assignmentStrategy;
    private NotificationService notificationService;

    private LockerSystem() {
        this.locations = new ConcurrentHashMap<>();
        this.packageLockerMap = new ConcurrentHashMap<>();
        this.packageLocationMap = new ConcurrentHashMap<>();
        this.packageCodeMap = new ConcurrentHashMap<>();
    }

    public static LockerSystem getInstance() {
        if (instance == null) {
            synchronized (LockerSystem.class) {
                if (instance == null) {
                    instance = new LockerSystem();
                }
            }
        }
        return instance;
    }

    public void setAssignmentStrategy(LockerAssignmentStrategy strategy) { this.assignmentStrategy = strategy; }
    public void setNotificationService(NotificationService service) { this.notificationService = service; }

    public void addLocation(LockerLocation location) {
        locations.put(location.getId(), location);
    }

    public void deliverPackage(Package pkg, String locationId) {
        deliverPackageWithCustomExpiry(pkg, locationId, 3 * 24 * 60 * 60);
    }

    public void deliverPackageWithCustomExpiry(Package pkg, String locationId, long expirySeconds) {
        LockerLocation location = locations.get(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location " + locationId + " not found");
        }

        List<Locker> availableLockers = location.getAllLockers();

        Locker locker = assignmentStrategy.assignLocker(pkg.getLockerSize(), availableLockers);

        String code = generateCode();
        long expirationTime = System.currentTimeMillis() + (expirySeconds * 1000);
        LockerCode lockerCode = new LockerCode(code, pkg.getId(), expirationTime);

        locker.assignPackage(pkg, lockerCode);
        pkg.setStatus(PackageStatus.DELIVERED);

        packageLockerMap.put(pkg.getId(), locker.getId());
        packageLocationMap.put(pkg.getId(), locationId);
        packageCodeMap.put(pkg.getId(), code);

        notificationService.notifyCustomer(
                pkg.getOrderId(), code, location.getAddress(), expirationTime
        );
    }

    public Package pickupPackage(String locationId, String lockerId, String code) {
        LockerLocation location = locations.get(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location " + locationId + " not found");
        }

        Locker locker = location.getLocker(lockerId);

        synchronized (locker) {
            if (locker.getStatus() != LockerStatus.OCCUPIED) {
                throw new IllegalStateException("Locker " + lockerId + " is not occupied");
            }

            LockerCode lockerCode = locker.getCurrentCode();
            Package pkg = locker.getCurrentPackage();

            if (pkg.getStatus() == PackageStatus.PICKED_UP) {
                throw new PackageAlreadyPickedUpException(
                        "Package " + pkg.getId() + " has already been picked up"
                );
            }

            if (lockerCode.isExpired()) {
                throw new PackageExpiredException(
                        "Package " + pkg.getId() + " has expired. Please contact support."
                );
            }

            if (!lockerCode.getCode().equals(code)) {
                throw new InvalidCodeException("Invalid pickup code for locker " + lockerId);
            }

            locker.releasePackage();
            pkg.setStatus(PackageStatus.PICKED_UP);

            packageLockerMap.remove(pkg.getId());
            packageLocationMap.remove(pkg.getId());
            packageCodeMap.remove(pkg.getId());

            return pkg;
        }
    }

    public void cleanupExpiredPackages() {
        for (LockerLocation location : locations.values()) {
            for (Locker locker : location.getAllLockers()) {
                synchronized (locker) {
                    if (locker.getStatus() == LockerStatus.OCCUPIED) {
                        LockerCode code = locker.getCurrentCode();
                        if (code != null && code.isExpired()) {
                            Package pkg = locker.releasePackage();
                            pkg.setStatus(PackageStatus.RETURNED);

                            packageLockerMap.remove(pkg.getId());
                            packageLocationMap.remove(pkg.getId());
                            packageCodeMap.remove(pkg.getId());

                            System.out.println("[CLEANUP] Package " + pkg.getId()
                                    + " expired. Returning package and freeing locker "
                                    + locker.getId() + ".");
                        }
                    }
                }
            }
        }
    }

    public String getCodeForPackage(String packageId) {
        return packageCodeMap.get(packageId);
    }

    private String generateCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
