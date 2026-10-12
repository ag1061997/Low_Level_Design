package strategies;

import enums.Direction;
import entities.Elevator;

import java.util.List;

public interface DispatchStrategy {
    Elevator selectElevator(List<Elevator> elevators, int floor, Direction direction);
}
