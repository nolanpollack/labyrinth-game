package com.northeastern.labyrinth.Util;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * com.northeastern.labyrinth.Util class for colors
 */
public class ColorHandler {
    public static final Color GUI_BACKGROUND_COLOR = new Color(154, 154, 154);

    /**
     * Computers equivalent rgb color from string. Expects a hexcode RGB value or one of:
     * "purple"
     * "orange"
     * "pink"
     * "red"
     * "blue"
     * "green"
     * "yellow"
     * "white"
     * "black"
     */
    public static Color stringToColor(String color) {
        Pattern pattern = Pattern.compile("^[A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d]$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(color);

        if (matcher.find()) {
            return Color.decode("#" + color);
        }

        switch (color) {
            case "purple":
                return new Color(255, 0, 255);
            case "orange":
                return new Color(255, 125, 0);
            case "pink":
                return new Color(255, 50, 100);
            case "red":
                return new Color(255, 0, 0);
            case "blue":
                return new Color(0, 0, 255);
            case "green":
                return new Color(0, 255, 0);
            case "yellow":
                return new Color(255, 255, 0);
            case "white":
                return new Color(255, 255, 255);
            case "black":
                return new Color(0, 0, 0);
            default:
                throw new IllegalArgumentException("Not a valid color, given: " + color);
        }
    }

    public static String colorToString(Color color) {

        int rgb = color.getRGB();

        switch (rgb) {
            case -65281:
                return "purple";
            case -33536:
                return "orange";
            case -52636:
                return "pink";
            case -65536:
                return "red";
            case -16776961:
                return "blue";
            case -16711936:
                return "green";
            case -256:
                return "yellow";
            case -1:
                return "white";
            case -16777216:
                return "black";
            default:
                throw new IllegalArgumentException("I don't recognize this color");
        }
    }
}
