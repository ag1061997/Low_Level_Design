package strategies;

import enums.Direction;
import enums.ElevatorState;
import exceptions.ElevatorException;
import entities.Elevator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoneBasedStrategy implements DispatchStrategy {
    private final Map<Integer, Integer> zoneAssignments;

    public ZoneBasedStrategy(int totalFloors, int numElevators) {
        this.zoneAssignments = new HashMap<>();
        int floorsPerZone = totalFloors / numElevators;
        int remainder  = totalFloors % numElevators;

        int floorStart = 1;
        for (int i = 0; i < numElevators; i++) {
            int zoneSize = floorsPerZone + (i < remainder ? 1 : 0);
            for (int f = floorStart; f < floorStart + zoneSize; f++) {
                zoneAssignments.put(f, i);
            }
            floorStart += zoneSize;
        }
    }

    @Override
    public Elevator selectElevator(List<Elevator> elevators, int floor, Direction direction) {
        Integer assignedIndex = zoneAssignments.get(floor);
        if (assignedIndex != null && assignedIndex < elevators.size()) {
            Elevator assignedElevator = elevators.get(assignedIndex);
            if (assignedElevator.getState() != ElevatorState.OUT_OF_SERVICE) {
                return assignedElevator;
            }
        }

        Elevator fallback = null;
        int minDistance = Integer.MAX_VALUE;
        for(Elevator elevator : elevators) {
            if (elevator.getState() != ElevatorState.OUT_OF_SERVICE) {
                int distance = Math.abs(elevator.getCurrentFloor() - floor);
                if (distance < minDistance) {
                    minDistance = distance;
                    fallback = elevator;
                }
            }
        }
        if (fallback == null) { throw new ElevatorException("No available elevator for floor " + floor); }
        return fallback;
    }
}
