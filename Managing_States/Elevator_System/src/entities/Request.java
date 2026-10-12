package entities;

import enums.Direction;
import enums.RequestType;

public class Request {
    private final int floor;
    private final Direction direction;
    private final RequestType type;
    private final long timestamp;

    public Request (int floor, Direction direction, RequestType type) {
        this.floor = floor;
        this.direction = direction;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public int getFloor() { return floor; }
    public Direction getDirection() { return direction; }
    public RequestType getType() { return type; }
    public long getTimestamp() { return timestamp; }
}
