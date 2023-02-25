package com.northeastern.labyrinth.Util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CoordinateTest {
    @Test
    public void VERIFY_EQUALS_RANDOM_OBJECT_FAILS() {
        List<Object> randomObject = new ArrayList<>();
        Coordinate location = new Coordinate(5, 5);
        assert(!location.equals(randomObject));
    }

    @Test
    public void VERIFY_EQUALS_SELF_PASS() {
        Coordinate location = new Coordinate(5, 5);
        assert(location.equals(location));
    }

    @Test
    public void VERIFY_EQUALS_SAME_ROW_COL_PASS() {
        Coordinate location = new Coordinate(5, 5);
        Coordinate location2 = new Coordinate(5, 5);
        assert(location.equals(location2));
    }

    @Test
    public void VERIFY_EQUALS_DIFFERENT_ROW_COL_FAIL() {
        Coordinate location = new Coordinate(5, 5);
        Coordinate location2 = new Coordinate(4, 5);
        Coordinate location3 = new Coordinate(5, 4);
        Coordinate location4 = new Coordinate(4, 4);
        assert(!location.equals(location2));
        assert(!location.equals(location3));
        assert(!location.equals(location4));
    }

    @Test
    public void CREATE_COPY() {
        Coordinate location = new Coordinate(5, 5);
        Coordinate locationCopy = location.createCopy();

        assert(location != locationCopy);
        assertEquals(location, locationCopy);
    }

    @Test
    public void TEST_COMPARE_EQUAL() {
        Coordinate location1 = new Coordinate(5, 5);
        Coordinate location2 = new Coordinate(5, 5);

        assertEquals(location1.compareTo(location2), 0);
    }

    @Test
    public void TEST_COMPARE_GREATER() {
        Coordinate location1 = new Coordinate(5, 5);
        Coordinate location2 = new Coordinate(4, 5);
        Coordinate location3 = new Coordinate(5, 3);

        assertEquals(location1.compareTo(location2), 1);
        assertEquals(location1.compareTo(location3), 2);
    }

    @Test
    public void TEST_COMPARE_LESS() {
        Coordinate location1 = new Coordinate(3, 3);
        Coordinate location2 = new Coordinate(4, 5);
        Coordinate location3 = new Coordinate(3, 5);

        assertEquals(location1.compareTo(location2), -1);
        assertEquals(location1.compareTo(location3), -2);
    }
}
