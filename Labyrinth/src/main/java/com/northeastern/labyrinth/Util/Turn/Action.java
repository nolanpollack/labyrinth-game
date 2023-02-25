package com.northeastern.labyrinth.Util.Turn;

import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;

/**
 * Represents a sequence of actions that make up a players turn:
 * Slide Action represents the slide
 * Rotation represents the counterclockwise rotation (in degrees) of the spare tile
 * toMove represents the coordinate the player wants to move to
 */
public class Action {
    // Index and Enum Direction
    private final SlideAction slideAction;
    // multiple of 90
    private final int rotation;
    // Coordinate of new position
    private final Coordinate toMove;

    public Action(SlideAction slideAction, int rotation, Coordinate toMove) {
        this.slideAction = slideAction;
        this.rotation = rotation;
        this.toMove = toMove;
    }

    public SlideAction getSlideAction() {
        return this.slideAction;
    }

    public int getIndex() {
        return this.slideAction.getIndex();
    }

    public Direction getDirection() {
        return this.slideAction.getDirection();
    }

    public int getRotation() {
        return this.rotation;
    }

    public Coordinate getToMove() {
        return this.toMove;
    }

    /**
     * returns if given object is an Action with equal SlideAction, rotation, and Coordinate values
     */
    @Override
    public boolean equals(Object testObject) {
        if (this == testObject) {
            return true;
        }

        if (testObject instanceof Action) {
            Action testAction = (Action) testObject;

            return testAction.getSlideAction().equals(this.slideAction)
                    && testAction.getRotation() == this.rotation
                    && testAction.getToMove().equals(this.toMove);
        }

        return false;
    }

    @Override
    public String toString() {
        return "[" + getIndex() + ","
                + "\"" + getDirection() + "\","
                + rotation + ","
                + toMove
                + "]";
    }
}
