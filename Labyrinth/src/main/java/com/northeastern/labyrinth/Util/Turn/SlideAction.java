package com.northeastern.labyrinth.Util.Turn;

import com.northeastern.labyrinth.Util.Direction;

/**
 * Represents a slide move with given direction, and index.
 * Index represents row index if direction is RIGHT or LEFT
 * Index represents column index if direction is UP or DOWN
 * Represents empty Slide as ANY SlideAction with an index < 0 (direction irrelevant)
 */
public class SlideAction {
    private final int index;
    private final Direction direction;

    /**
     * Creates empty Slide
     */
    public SlideAction() {
        this.index = -1;
        this.direction = Direction.UP;
    }

    public SlideAction(int index, Direction direction) {
        this.index = index;
        this.direction = direction;
    }

    public int getIndex() {
        return index;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns the SlideAction that goes in the opposite direction on the same index
     */
    public SlideAction getReverseSlide() {
        return new SlideAction(index, Direction.getOppositeDirection(direction));
    }

    /**
     * returns if given object is a SlideAction with equal inde and direction values
     */
    @Override
    public boolean equals(Object testObject) {
        if (this == testObject) {
            return true;
        }

        if (testObject instanceof SlideAction) {
            SlideAction testSlideAction = (SlideAction) testObject;

            return testSlideAction.getIndex() == this.getIndex()
                    && testSlideAction.getDirection() == this.getDirection();
        }

        return false;
    }

    @Override
    public String toString() {
        return "[" + getIndex() + ","
                + "\"" + getDirection()
                + "\"]";
    }
}


