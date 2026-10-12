package entities;

import enums.Direction;
import enums.ElevatorState;
import exceptions.ElevatorException;
import observers.Display;
import observers.ElevatorObserver;

import java.util.Collections;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class Elevator {
    private final int id;
    private final Display display;
    private final Door door;
    private final int totalFloors;
    private final TreeSet<Integer> upRequests;
    private final TreeSet<Integer> downRequests;
    private final CopyOnWriteArrayList<ElevatorObserver> observers;
    private int currentFloor;
    private Direction direction;
    private ElevatorState state;

    public Elevator(int id, int totalFloors) {
        this.id = id;
        this.display = new Display(0);
        this.door = new Door();
        this.totalFloors = totalFloors;
        this.upRequests = new TreeSet<>();
        this.downRequests = new TreeSet<>(Collections.reverseOrder());
        this.observers = new CopyOnWriteArrayList<>();
        this.currentFloor = 1;
        this.direction = Direction.IDLE;
        this.state = ElevatorState.IDLE;
        addObserver(display);
    }

    public int getId() { return id; }
    public int getTotalFloors() { return totalFloors; }
    public Display getDisplay() { return display; }
    public synchronized int getCurrentFloor() { return currentFloor; }
    public synchronized Direction getDirection() { return direction; }
    public synchronized ElevatorState getState() { return state; }

    public synchronized void setDirection(Direction direction) { this.direction = direction; }
    public synchronized void setState(ElevatorState state) { this.state = state; }

    public synchronized boolean hasRequests() { return !upRequests.isEmpty() || !downRequests.isEmpty(); }
    public synchronized boolean hasUpRequests() { return !upRequests.isEmpty(); }
    public synchronized boolean hasDownRequests() { return !downRequests.isEmpty(); }

    public void openDoor() {
        door.open();
        synchronized (this) {
            state = ElevatorState.DOOR_OPEN;
        }
        System.out.println("Elevator " + id + " arrived at floor " + currentFloor + ", opening door");
    }

    public void closeDoor() {
        door.close();
        System.out.println("Elevator " + id + " door closed");
    }

    public synchronized void addRequest(int floor, Direction direction) {
        if (state == ElevatorState.OUT_OF_SERVICE) { throw new ElevatorException("Elevator " + id + " is out of service"); }
        if (floor < 1 || floor > totalFloors) { throw new ElevatorException("Invalid floor: " + floor); }
        if(floor == currentFloor) { return; }

        if (direction == Direction.UP || floor > currentFloor) {
            upRequests.add(floor);
        } else {
            downRequests.add(floor);
        }
        System.out.println("Elevator " + id + " received request for floor " + floor);
    }

    public synchronized int getNextStop() {
        if (direction == Direction.UP) {
            Integer next = upRequests.ceiling(currentFloor);
            if (next != null) return next;
            if(!downRequests.isEmpty()) return -1;
        } else if (direction == Direction.DOWN) {
            Integer next = downRequests.ceiling(currentFloor);
            if (next != null) return next;
            if(!upRequests.isEmpty()) return -1;
        }
        return -1;
    }

    public synchronized void removeCurrentFloorFromRequests() {
        upRequests.remove(currentFloor);
        downRequests.remove(currentFloor);
    }

    public synchronized void moveToFloor(int floor) {
        this.currentFloor = floor;
        if (direction == Direction.UP) {
            this.state = ElevatorState.MOVING_UP;
        } else if (direction == Direction.DOWN) {
            this.state = ElevatorState.MOVING_DOWN;
        }
        notifyObservers();
    }

    public void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (ElevatorObserver observer : observers) {
            observer.onElevatorStateChanged(id, currentFloor, direction);
        }
    }
}
