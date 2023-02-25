package com.northeastern.labyrinth.Util;

import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests public methods in the Direction enum
 */
public class DirectionTest {

    @Test
    public void VERIFY_ALL_ENUM_RETURNED() {
        List<Direction> listOfDirections = Direction.getListOfDirections();

        assertEquals(4, listOfDirections.size());
        assert(listOfDirections.contains(Direction.UP));
        assert(listOfDirections.contains(Direction.LEFT));
        assert(listOfDirections.contains(Direction.DOWN));
        assert(listOfDirections.contains(Direction.RIGHT));
    }

    @Test
    public void VERIFY_IS_HORIZONTAL_TRUE() {
        Direction directionRIGHT = Direction.RIGHT;
        Direction directionLEFT = Direction.LEFT;

        assert(Direction.isHorizontal(directionRIGHT));
        assert(Direction.isHorizontal(directionLEFT));
    }

    @Test
    public void VERIFY_IS_HORIZONTAL_FALSE() {
        Direction directionUP = Direction.UP;
        Direction directionDOWN = Direction.DOWN;

        assert(!Direction.isHorizontal(directionUP));
        assert(!Direction.isHorizontal(directionDOWN));
    }

    @Test
    public void TEST_OPPOSITE_DIRECTION() {
        assertEquals(Direction.UP, Direction.getOppositeDirection(Direction.DOWN));
        assertEquals(Direction.DOWN, Direction.getOppositeDirection(Direction.UP));
        assertEquals(Direction.LEFT, Direction.getOppositeDirection(Direction.RIGHT));
        assertEquals(Direction.RIGHT, Direction.getOppositeDirection(Direction.LEFT));
    }
}
