package com.northeastern.labyrinth.Model.Board;

import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Gem;

import java.util.*;

/**
 * Represents a tile as a <Direction, Boolean> Map that represents which Tiles it connects to.
 * The map always has 4 entries each corresponding to a value in the Direction enum.
 * Example for └ Tile:
 * <UP, true>
 * <LEFT, false>
 * <DOWN, false>
 * <RIGHT, true>
 * Each Tile also has a unique, unordered combination of two gems that can be used to identify a
 * specific Tile.
 */
public final class Tile {
    //Represents where Tile connects to
    private final Map<Direction, Boolean> connections;
    //Array of 2 Gems representing gems. Order is insignificant
    private final Gem[] gems;

    /**
     * Constructs a Tile from a symbol
     */
    public Tile(char symbol, String gem1, String gem2) {
        this.connections = decodeSymbolToConnection(symbol);
        this.gems = stringsToGem(gem1, gem2);
    }

    /**
     * Constructs a Tile from a connection Map and String gems
     */
    public Tile(Map<Direction, Boolean> connections, String gem1, String gem2) {
        this.connections = new HashMap<>(connections);
        this.gems = stringsToGem(gem1, gem2);
    }

    /**
     * Constructs a Tile from a connection Map and Enum gems.
     */
    public Tile(Map<Direction, Boolean> connections, Gem gem1, Gem gem2) {
        this.connections = new HashMap<>(connections);
        this.gems = new Gem[]{gem1, gem2};
    }

    /**
     * Converts two strings to an array of two gems.
     */
    private Gem[] stringsToGem(String gem1, String gem2) {
        Gem[] gems = new Gem[2];
        gems[0] = Gem.decodeToEnum(gem1);
        gems[1] = Gem.decodeToEnum(gem2);

        return gems;
    }

    /**
     * Rotates a tile counter-clockwise by the given degree.
     *
     * @param degrees int representing a degree of rotation. Negative represents clockwise movement.
     * @return A new copy with the applied rotation to the connector Map.
     * @throws IllegalArgumentException if given degrees is not a multiple of 90.
     */
    public Tile rotateClockwise(int degrees) throws IllegalArgumentException {
        if (degrees % 90 != 0) {
            throw new IllegalArgumentException("Can only rotate by multiples of 90! Given: " + degrees);
        }

        int numRotations = (degrees % 360) / 90;

        if (degrees < 0) {
            numRotations += 360;
        }

        Map<Direction, Boolean> newConnections = new HashMap<>(connections);

        for (int i = 0; i < numRotations; i++) {
            boolean tempWESTValue = newConnections.get(Direction.LEFT);
            newConnections.put(Direction.LEFT, newConnections.get(Direction.DOWN));
            newConnections.put(Direction.DOWN, newConnections.get(Direction.RIGHT));
            newConnections.put(Direction.RIGHT, newConnections.get(Direction.UP));
            newConnections.put(Direction.UP, tempWESTValue);
        }

        return new Tile(newConnections, gems[0], gems[1]);
    }

    /**
     * Returns map representation of which edges the Tile has a connection to
     */
    public Map<Direction, Boolean> getConnections() {
        return new HashMap<>(this.connections);
    }


    /**
     * Returns an array of length 2 holding Tile gems
     */
    public Gem[] getTreasure() {
        return new Gem[]{this.gems[0], this.gems[1]};
    }

    /**
     * Returns if given object is a Tile with equal unordered gem values.
     */
    @Override
    public boolean equals(Object testObject) {
        if (this == testObject) {
            return true;
        }

        if ((testObject instanceof Tile)) {
            Tile testTile = (Tile) testObject;

            Set<Gem> testGems = new HashSet<>(Arrays.asList(testTile.getTreasure()));

            return new HashSet<>(Arrays.asList(gems)).containsAll(testGems)
                    && testGems.containsAll(Arrays.asList(gems));
        }

        return false;
    }


    /**
     * Decodes symbol character to HashMap data representation.
     *
     * @param symbol one of the 16 possible tile symbols. Will throw an exception if symbol is anything else.
     * @return HashMap representation of the symbol.
     */
    private Map<Direction, Boolean> decodeSymbolToConnection(char symbol) {
        switch (symbol) {
            case '│':
                return createCustomTile(true, false, true, false);
            case '─':
                return createCustomTile(false, true, false, true);
            case '┐':
                return createCustomTile(false, false, true, true);
            case '└':
                return createCustomTile(true, true, false, false);
            case '┌':
                return createCustomTile(false, true, true, false);
            case '┘':
                return createCustomTile(true, false, false, true);
            case '┬':
                return createCustomTile(false, true, true, true);
            case '├':
                return createCustomTile(true, true, true, false);
            case '┴':
                return createCustomTile(true, true, false, true);
            case '┤':
                return createCustomTile(true, false, true, true);
            case '┼':
                return createCustomTile(true, true, true, true);
            default:
                throw new IllegalArgumentException("Unexpected symbol value: " + symbol);
        }
    }


    /**
     * Creates a HashMap Tile representation from booleans of is connecting edge.
     */
    private Map<Direction, Boolean> createCustomTile(boolean up, boolean right, boolean down, boolean left) {
        Map<Direction, Boolean> map = new HashMap<>();
        map.put(Direction.UP, up);
        map.put(Direction.RIGHT, right);
        map.put(Direction.DOWN, down);
        map.put(Direction.LEFT, left);
        return map;
    }
}
