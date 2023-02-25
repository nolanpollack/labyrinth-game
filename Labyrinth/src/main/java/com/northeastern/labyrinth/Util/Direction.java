package com.northeastern.labyrinth.Util;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents a direction
 */
public enum Direction {
    @SerializedName("UP")
    UP,
    @SerializedName("RIGHT")
    RIGHT,
    @SerializedName("DOWN")
    DOWN,
    @SerializedName("LEFT")
    LEFT;

    /**
     * Returns a list of all Direction values
     */
    public static List<Direction> getListOfDirections() {
        return List.of(values());
    }

    /**
     * Returns if a direction is a horizontal direction
     */
    public static boolean isHorizontal(Direction direction) {
        return direction == LEFT || direction == RIGHT;
    }

    /**
     * Returns if a direction is a horizontal direction
     * Throws error in case of null due to Java Enums allowing null, requiring default switch case
     */
    public static Direction getOppositeDirection(Direction direction) {
        switch (direction) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
            default:
                throw new IllegalArgumentException("Given null direction!");
        }
    }
}
