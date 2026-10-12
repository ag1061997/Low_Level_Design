package states.light;

import core.TrafficLight;
import enums.LightColor;

public class GreenState implements SignalState {
    @Override
    public void handle(TrafficLight context) {
        context.setColor(LightColor.GREEN);
        context.setNextState(new YellowState());
    }
}
