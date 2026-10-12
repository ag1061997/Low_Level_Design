package strategies;

import enums.Direction;
import enums.ElevatorState;
import exceptions.ElevatorException;
import entities.Elevator;

import java.util.List;

public class NearestElevatorStrategy implements DispatchStrategy {
    private final int totalfloors;

    public NearestElevatorStrategy(int totalfloors) {
        this.totalfloors = totalfloors;
    }

    @Override
    public Elevator selectElevator(List<Elevator> elevators, int floor, Direction direction) {
        Elevator bestElevator = null;
        int bestScore = -1;

        for (Elevator elevator : elevators) {
            if (elevator.getState() == ElevatorState.OUT_OF_SERVICE) { continue; }

            int distance = Math.abs(elevator.getCurrentFloor() - floor);
            int score = 0;

            if (elevator.getDirection() == Direction.IDLE) {
                score = totalfloors - distance;
            } else if (elevator.getDirection() == direction) {
                boolean hasPassed = direction == Direction.UP ? elevator.getCurrentFloor() > floor : elevator.getCurrentFloor() < floor;
                score = hasPassed ? 1 : totalfloors - distance + totalfloors;
            } else {
                score = 1;
            }

            if (score > bestScore) {
                bestElevator = elevator;
                score = bestScore;
            }
        }

        if (bestElevator == null) { throw new ElevatorException("No available elevator for floor " + floor); }

        return bestElevator;
    }
}
