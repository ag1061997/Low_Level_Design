package observers;

import enums.Direction;

public class Display implements ElevatorObserver {
    private final int elevatorId;
    private int currentFloor;
    private Direction currentDirection;

    public Display(int elevatorId) {
        this.elevatorId = elevatorId;
        this.currentDirection = Direction.IDLE;
        this.currentFloor = 1;
    }

    @Override
    public void onElevatorStateChanged(int elevatorId, int floor, Direction direction) {
        if (this.elevatorId == elevatorId) {
            this.currentFloor = floor;
            this.currentDirection = direction;
            show();
        }
    }

    public void show() {
        System.out.println("Display [Elevator " + elevatorId + "]: Floor " + currentFloor + " | Direction: " + currentDirection);
    }

    public int getElevatorId() { return elevatorId; }

}
