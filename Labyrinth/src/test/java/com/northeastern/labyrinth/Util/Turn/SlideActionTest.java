package com.northeastern.labyrinth.Util.Turn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import com.northeastern.labyrinth.Util.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SlideActionTest {
    @Test
    public void VERIFY_EQUALS_RANDOM_OBJECT_FAILS() {
        List<Object> randomObject = new ArrayList<>();
        SlideAction slide = new SlideAction(5, Direction.UP);
        assert(!slide.equals(randomObject));
    }

    @Test
    public void VERIFY_EQUALS_SELF_PASS() {
        SlideAction slide = new SlideAction(5, Direction.UP);
        assert(slide.equals(slide));
    }

    @Test
    public void VERIFY_EQUALS_SAME_INDEX_DIRECTION_PASS() {
        SlideAction slide1 = new SlideAction(5, Direction.UP);
        SlideAction slide2 = new SlideAction(5, Direction.UP);
        assert(slide1.equals(slide2));
    }

    @Test
    public void VERIFY_EQUALS_DIFFERENT_INDEX_DIRECTION_FAIL() {
        SlideAction slide1 = new SlideAction(5, Direction.UP);
        SlideAction slide2 = new SlideAction(4, Direction.UP);
        SlideAction slide3 = new SlideAction(5, Direction.DOWN);
        SlideAction slide4 = new SlideAction(4, Direction.DOWN);
        assert(!slide1.equals(slide2));
        assert(!slide1.equals(slide3));
        assert(!slide1.equals(slide4));
    }

    @Test
    public void TEST_GET_REVERSE() {
        SlideAction slide1 = new SlideAction(5, Direction.UP);
        SlideAction slide2 = new SlideAction(5, Direction.RIGHT);
        SlideAction slide3 = new SlideAction(5, Direction.DOWN);
        SlideAction slide4 = new SlideAction(5, Direction.LEFT);

        assertEquals(slide1.getReverseSlide(), new SlideAction(5, Direction.DOWN));
        assertEquals(slide2.getReverseSlide(), new SlideAction(5, Direction.LEFT));
        assertEquals(slide3.getReverseSlide(), new SlideAction(5, Direction.UP));
        assertEquals(slide4.getReverseSlide(), new SlideAction(5, Direction.RIGHT));
    }
}
