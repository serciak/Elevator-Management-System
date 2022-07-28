package com.company.request;

import com.company.Direction;

public class Request {

    private final int targetFloor;
    private final Direction direction;
    private final RequestType type;

    public Request(int targetFloor, Direction direction, RequestType type) {
        this.targetFloor = targetFloor;
        this.direction = direction;
        this.type = type;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public RequestType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "{" + "FLOOR [" + targetFloor + "] | DIRECTION [" + direction + "] | TYPE [" + type + "]}";
    }
}
