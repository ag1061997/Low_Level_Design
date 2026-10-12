package states.light;

import core.TrafficLight;
import enums.LightColor;

public class YellowState implements SignalState {
    @Override
    public void handle(TrafficLight context) {
        context.setColor(LightColor.YELLOW);
        context.setNextState(new RedState());
    }
}
