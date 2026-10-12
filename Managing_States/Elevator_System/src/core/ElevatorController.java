package core;

import enums.Direction;
import enums.ElevatorState;
import entities.Elevator;

public class ElevatorController implements Runnable {
    private final Elevator elevator;
    private volatile boolean running;

    public ElevatorController(Elevator elevator) {
        this.elevator = elevator;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            if (elevator.hasRequests()) {
                processRequests();
            } else {
                elevator.setDirection(Direction.IDLE);
                elevator.setState(ElevatorState.IDLE);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    public void stop() {
        this.running = false;
    }

    private void processRequests() {
        Direction dir = elevator.getDirection();

        if (dir == Direction.IDLE) {
            if (elevator.hasUpRequests()) {
                elevator.setDirection(Direction.UP);
            } else if (elevator.hasDownRequests()) {
                elevator.setDirection(Direction.DOWN);
            }
            return;
        }

        int nextStop = elevator.getNextStop();
        if (nextStop == -1) {
            if (dir == Direction.UP && elevator.hasDownRequests()) {
                elevator.setDirection(Direction.DOWN);
            } else if (dir == Direction.DOWN && elevator.hasUpRequests()) {
                elevator.setDirection(Direction.UP);
            } else {
                elevator.setDirection(Direction.IDLE);
                elevator.setState(ElevatorState.IDLE);
            }
            return;
        }

        int currentFloor = elevator.getCurrentFloor();
        if (nextStop > currentFloor) {
            elevator.moveToFloor(currentFloor + 1);
        } else if (nextStop < currentFloor) {
            elevator.moveToFloor(currentFloor - 1);
        } else {
            serveFloor();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void serveFloor() {
        elevator.openDoor();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        elevator.closeDoor();
        elevator.removeCurrentFloorFromRequests();
    }
}
