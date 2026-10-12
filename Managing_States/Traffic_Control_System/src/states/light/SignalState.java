package states.light;

import core.TrafficLight;

public interface SignalState {
    void handle(TrafficLight context);
}
