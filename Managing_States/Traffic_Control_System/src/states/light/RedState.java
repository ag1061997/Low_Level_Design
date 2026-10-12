package states.light;

import core.TrafficLight;
import enums.LightColor;

public class RedState implements SignalState {
    @Override
    public void handle(TrafficLight context) {
        context.setColor(LightColor.RED);
        context.setNextState(new RedState());
    }
}
