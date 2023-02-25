package com.northeastern.labyrinth.Util;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColorHandlerTests {

    @Test
    public void TEST_COLOR_HANDLER_RED() {
        Color red = ColorHandler.stringToColor("red");

        assertEquals(255, red.getRed());
        assertEquals(0, red.getGreen());
        assertEquals(0, red.getBlue());
    }

    @Test
    public void TEST_COLOR_HANDLER_HEXCODE() {
        Color red = ColorHandler.stringToColor("FFFFFF");

        assertEquals(255, red.getRed());
        assertEquals(255, red.getGreen());
        assertEquals(255, red.getBlue());
    }
}
