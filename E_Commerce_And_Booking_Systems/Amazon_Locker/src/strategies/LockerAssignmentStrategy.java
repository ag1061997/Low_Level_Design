package strategies;

import entities.Locker;
import enums.LockerSize;

import java.util.List;

public interface LockerAssignmentStrategy {
    Locker assignLocker(LockerSize requiredSize, List<Locker> availableLockers);
}
