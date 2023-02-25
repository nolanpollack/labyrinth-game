package com.northeastern.labyrinth.Util.Turn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;

public class ActionTest {
    @Test
    public void VERIFY_EQUALS_RANDOM_OBJECT_FAILS() {
        List<Object> randomObject = new ArrayList<>();
        SlideAction slide = new SlideAction(5, Direction.UP);
        Coordinate coordinate = new Coordinate(0, 0);

        Action action = new Action(slide, 0, coordinate);
        assert(!action.equals(randomObject));
    }

    @Test
    public void VERIFY_EQUALS_SELF_PASS() {
        SlideAction slide = new SlideAction(5, Direction.UP);
        Coordinate coordinate = new Coordinate(0, 0);

        Action action = new Action(slide, 0, coordinate);
        assert(action.equals(action));
    }

    @Test
    public void VERIFY_EQUALS_SAME_INDEX_DIRECTION_PASS() {
        SlideAction slide1 = new SlideAction(5, Direction.UP);
        SlideAction slide2 = new SlideAction(5, Direction.UP);
        Coordinate coordinate1 = new Coordinate(0, 0);
        Coordinate coordinate2 = new Coordinate(0, 0);

        Action action1 = new Action(slide1, 0, coordinate1);
        Action action2 = new Action(slide2, 0, coordinate2);
        assert(action1.equals(action2));
    }

    @Test
    public void VERIFY_EQUALS_DIFFERENT_INDEX_DIRECTION_FAIL() {
        SlideAction slide1 = new SlideAction(5, Direction.UP);
        SlideAction slide2 = new SlideAction(2, Direction.UP);
        Coordinate coordinate1 = new Coordinate(0, 0);
        Coordinate coordinate2 = new Coordinate(1, 1);
        Action action1 = new Action(slide1, 0, coordinate1);
        Action action2 = new Action(slide2, 0, coordinate2);
        Action action3 = new Action(slide1, 0, coordinate2);
        Action action4 = new Action(slide2, 0, coordinate1);
        Action action5 = new Action(slide1, 90, coordinate1);


        assert(!action1.equals(action2));
        assert(!action1.equals(action3));
        assert(!action1.equals(action4));
        assert(!action1.equals(action5));
    }
}
