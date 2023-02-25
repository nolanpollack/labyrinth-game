package com.northeastern.labyrinth.Util;

import java.util.Map;

/**
 * Provides utility methods to convert connector maps to symbols
 */
public class TileSymbolConverter {
    /**
     * Converts connector map from a Tile to an equivalent symbol character. If the connector doesn't
     * match, returns a space ' '
     */
    public static char connectorsToSymbol(Map<Direction, Boolean> connectors) {
        if (connectorMatches(connectors, true, false, true, false)) {
            return '│';
        }
        if (connectorMatches(connectors, true, true, false, false)) {
            return '└';
        }
        if (connectorMatches(connectors, true, false, false, true)) {
            return '┘';
        }
        if (connectorMatches(connectors, true, true, true, false)) {
            return '├';
        }
        if (connectorMatches(connectors, true, true, false, true)) {
            return '┴';
        }
        if (connectorMatches(connectors, true, false, true, true)) {
            return '┤';
        }
        if (connectorMatches(connectors, true, true, true, true)) {
            return '┼';
        }
        if (connectorMatches(connectors, false, true, false, true)) {
            return '─';
        }
        if (connectorMatches(connectors, false, false, true, true)) {
            return '┐';
        }
        if (connectorMatches(connectors, false, true, true, false)) {
            return '┌';
        }
        if (connectorMatches(connectors, false, true, true, true)) {
            return '┬';
        }

        return ' ';
    }

    /**
     * returns if a connector map is equal to given 4 boolean values in order:
     * UP, RIGHT, DOWN, LEFT
     */
    private static boolean connectorMatches(Map<Direction, Boolean> connectors, boolean up, boolean right, boolean down, boolean left) {
        return connectors.get(Direction.UP).equals(up)
                && connectors.get(Direction.RIGHT).equals(right)
                && connectors.get(Direction.DOWN).equals(down)
                && connectors.get(Direction.LEFT).equals(left);
    }
}
