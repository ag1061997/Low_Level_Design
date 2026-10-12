package observers;

import enums.Direction;

public interface ElevatorObserver {
    void onElevatorStateChanged(int elevatorId, int floor, Direction direction);
}
