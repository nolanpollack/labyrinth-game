package com.northeastern.labyrinth.Model.Board;

import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.Util.Direction;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests public methods in the Tile class
 */
public class TileTest {
    @Test
    public void TEST_CONSTRUCTION_FROM_SYMBOL() {
        assert (constructionHelper('│', true, false, true, false));
        assert (constructionHelper('─', false, true, false, true));
        assert (constructionHelper('┐', false, false, true, true));
        assert (constructionHelper('└', true, true, false, false));
        assert (constructionHelper('┌', false, true, true, false));
        assert (constructionHelper('┘', true, false, false, true));
        assert (constructionHelper('┬', false, true, true, true));
        assert (constructionHelper('├', true, true, true, false));
        assert (constructionHelper('┴', true, true, false, true));
        assert (constructionHelper('┤', true, false, true, true));
        assert (constructionHelper('┼', true, true, true, true));
    }

    private boolean constructionHelper(char symbol, boolean up, boolean right, boolean down, boolean left) {
        Tile testTile = new Tile(symbol, "alexandrite", "alexandrite");

        Map<Direction, Boolean> connections = testTile.getConnections();

        boolean testPass = (up == connections.get(Direction.UP))
                && (right == connections.get(Direction.RIGHT))
                && (down == connections.get(Direction.DOWN))
                && (left == connections.get(Direction.LEFT));

        return testPass;
    }

    @Test
    public void VALIDATE_NO_MUTATION_ON_ROTATE() {
        Tile beforeTile = new Tile('┴', "alexandrite", "alexandrite");
        Tile afterTile = new Tile('┴', "alexandrite", "alexandrite");
        beforeTile.rotateClockwise(90);
        assertEquals(beforeTile.getConnections(), afterTile.getConnections());

        beforeTile = beforeTile.rotateClockwise(90);
        assertNotEquals(beforeTile.getConnections(), afterTile.getConnections());
    }

    @Test
    public void ROTATE_TILE_NON_90_MULTIPLE_THROWS_IOEXCEPTION() {
        testRotate(BoardTestUtil.create_l(), 91, new Boolean[]{true, false, true, false}, true);
        testRotate(BoardTestUtil.create_l(), -91, new Boolean[]{true, false, true, false}, true);

    }

    @Test
    public void ROTATE_GREATER_THAN_EQ_360() {
        testRotate(BoardTestUtil.create_L(), 360, new Boolean[]{true, true, false, false}, false);
        testRotate(BoardTestUtil.create_L(), 450, new Boolean[]{false, true, true, false}, false);
        testRotate(BoardTestUtil.create_L(), 7200, new Boolean[]{true, true, false, false}, false);
    }

    @Test
    public void ROTATE_NEG() {
        testRotate(BoardTestUtil.create_L(), -90, new Boolean[]{true, false, false, true}, false);
        testRotate(BoardTestUtil.create_L(), -180, new Boolean[]{false, false, true, true}, false);
        testRotate(BoardTestUtil.create_L(), -270, new Boolean[]{false, true, true, false}, false);
        testRotate(BoardTestUtil.create_L(), -360, new Boolean[]{true, true, false, false}, false);
    }

    @Test
    public void ROTATE_l_PIECE() {
        testRotate(BoardTestUtil.create_l(), 0, new Boolean[]{true, false, true, false}, false);
        testRotate(BoardTestUtil.create_l(), 90, new Boolean[]{false, true, false, true}, false);
        testRotate(BoardTestUtil.create_l(), 180, new Boolean[]{true, false, true, false}, false);
        testRotate(BoardTestUtil.create_l(), 270, new Boolean[]{false, true, false, true}, false);
    }

    @Test
    public void ROTATE_L_PIECE() {
        testRotate(BoardTestUtil.create_L(), 0, new Boolean[]{true, true, false, false}, false);
        testRotate(BoardTestUtil.create_L(), 90, new Boolean[]{false, true, true, false}, false);
        testRotate(BoardTestUtil.create_L(), 180, new Boolean[]{false, false, true, true}, false);
        testRotate(BoardTestUtil.create_L(), 270, new Boolean[]{true, false, false, true}, false);
    }

    @Test
    public void ROTATE_T_PIECE() {
        testRotate(BoardTestUtil.create_T(), 0, new Boolean[]{false, true, true, true}, false);
        testRotate(BoardTestUtil.create_T(), 90, new Boolean[]{true, false, true, true}, false);
        testRotate(BoardTestUtil.create_T(), 180, new Boolean[]{true, true, false, true}, false);
        testRotate(BoardTestUtil.create_T(), 270, new Boolean[]{true, true, true, false}, false);
    }

    @Test
    public void ROTATE_PLUS_PIECE() {
        testRotate(BoardTestUtil.create_PLUS(), 0, new Boolean[]{true, true, true, true}, false);
        testRotate(BoardTestUtil.create_PLUS(), 90, new Boolean[]{true, true, true, true}, false);
        testRotate(BoardTestUtil.create_PLUS(), 180, new Boolean[]{true, true, true, true}, false);
        testRotate(BoardTestUtil.create_PLUS(), 270, new Boolean[]{true, true, true, true}, false);
    }

    private void testRotate(Tile tile, int degrees, Boolean[] expected, boolean expectIOException) {
        Map<Direction, Boolean> map = new HashMap<>();
        map.put(Direction.UP, expected[0]);
        map.put(Direction.RIGHT, expected[1]);
        map.put(Direction.DOWN, expected[2]);
        map.put(Direction.LEFT, expected[3]);

        try {
            tile = tile.rotateClockwise(degrees);
            assert (!expectIOException);
        } catch (IllegalArgumentException e) {
            assert (expectIOException);
        }

        assertEquals(map, tile.getConnections());
    }

    @Test
    public void TEST_EQUALS() {
        Tile tile1 = new Tile('│', "alexandrite", "alexandrite");
        Tile tile2 = new Tile('│', "alexandrite", "alexandrite");
        Tile tile3 = new Tile('│', "alexandrite", "aplite");
        Tile tile4 = new Tile('│', "aplite", "alexandrite");
        assert(tile1.equals(tile2));
        assert(!tile1.equals(tile3));
        assert(!tile3.equals(tile1));
        assert(tile3.equals(tile4));
    }

    @Test
    public void NOT_GEM_ENUM() {
        String expectMessage = "java.lang.IllegalArgumentException: No enum constant com.northeastern.labyrinth.Util.Gem.WRONG";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            new Tile('│', "Wrong", "Doesn't Matter");
        });

        assertEquals(expectMessage, exception.toString());
    }
}