package core;

import enums.Direction;
import enums.LightColor;
import observer.TrafficObserver;
import states.light.GreenState;
import states.light.RedState;
import states.light.SignalState;

import java.util.ArrayList;
import java.util.List;

public class TrafficLight {
    private final Direction direction;
    private LightColor currentColor;
    private SignalState currentState;
    private SignalState nextState;
    private final List<TrafficObserver> observers = new ArrayList<>();
    private final int intersectionId;

    public TrafficLight(int intersectionId, Direction direction) {
        this.intersectionId = intersectionId;
        this.direction = direction;
        this.currentState = new RedState();
        this.currentState.handle(this);
    }

    public void startGreen() {
        this.currentState = new GreenState();
        this.currentState.handle(this);
    }

    public void transition() {
        this.currentState = this.nextState;
        this.currentState.handle(this);
    }

    public void setColor(LightColor color) {
        if (this.currentColor != color) {
            this.currentColor = color;
            notifyObservers();
        }
    }

    public void setNextState(SignalState state) { this.nextState = state; }
    public LightColor getCurrentColor() { return currentColor; }
    public Direction getDirection() { return direction; }

    public void addObserver(TrafficObserver observer) { observers.add(observer); }
    public void removeObserver(TrafficObserver observer) { observers.remove(observer); }

    private void notifyObservers() {
        for (TrafficObserver observer : observers) {
            observer.update(intersectionId, direction, currentColor);
        }
    }
}
