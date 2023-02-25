package com.northeastern.labyrinth.Model.Board;

import static com.northeastern.labyrinth.Model.BoardTestUtil.create_SampleBoard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.Util.Turn.SlideAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Tests public methods in the MazeBoard class
 */
public class MazeBoardTest {


    public Map<Direction, Boolean> tileCreatorHelper(boolean up, boolean left, boolean down, boolean right) {
        Map<Direction, Boolean> map = new HashMap<>();
        map.put(Direction.UP, up);
        map.put(Direction.RIGHT, left);
        map.put(Direction.DOWN, down);
        map.put(Direction.LEFT, right);
        return map;
    }

    @Test
    public void GET_REACHABLE_TILES() {
        MazeBoard mazeBoard2 = new MazeBoard(BoardTestUtil.create_SampleBoard());

        List<Coordinate> reachableTiles2 = mazeBoard2.reachableTiles(new Coordinate(1, 1));
        List<Coordinate> reachableTiles3 = mazeBoard2.reachableTiles(new Coordinate(0, 2));

        assertEquals(8, reachableTiles2.size());
        assertEquals(1, reachableTiles3.size());
    }

    @Test
    public void CAN_REACH_TILE() {
        MazeBoard mazeBoard = new MazeBoard(create_SampleBoard());

        Coordinate coordinate1 = new Coordinate(0, 0);
        Coordinate coordinate2 = new Coordinate(0, 2);
        Coordinate coordinate3 = new Coordinate(2, 2);


        assert(!mazeBoard.canReachTile(coordinate2, coordinate3));
        assert(mazeBoard.canReachTile(coordinate1, coordinate3));
    }

    @Test
    public void GET_REACHABLE_TILES_BAD_INPUT() {
        MazeBoard mazeBoard = new MazeBoard(create_SampleBoard());

        String expectMessage = "java.lang.IllegalArgumentException: Location doesn't exits on the board!";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            List<Coordinate> reachableTiles = mazeBoard.reachableTiles(new Coordinate(100, 100));
        });

        assertEquals(expectMessage, exception.toString());

        exception = assertThrows(RuntimeException.class, () -> {
            List<Coordinate> reachableTiles = mazeBoard.reachableTiles(null);
        });

        assertEquals(expectMessage, exception.toString());
    }

    @Test
    public void OUT_OF_RANGE_DIMENSION_SHIFT() {
        String expectMessage = "java.lang.IllegalArgumentException: Check the boards dimensions! Given: 4";

        MazeBoard mazeBoard = new MazeBoard(create_SampleBoard());
        SlideAction action = new SlideAction(4, Direction.RIGHT);
        Exception exception = assertThrows(RuntimeException.class, () -> {

            mazeBoard.slide(action, BoardTestUtil.create_l());
        });

        assertEquals(expectMessage, exception.toString());
    }


    @Test
    public void INVALID_BOARD_CONSTRUCTOR() {
        // treasure at (0,0) and (1,1) are the same.
        String expectMessage = "java.lang.IllegalArgumentException: NON UNIQUE TREASURES IN PROVIDED BOARD";
        Tile[][] toTest = new Tile[2][2];
        toTest[0][0] = new Tile('│', "AMETHYST", "AMETHYST");
        toTest[0][1] = new Tile('│', "AVENTURINE", "AMETHYST");
        toTest[1][0] = new Tile('│', "AVENTURINE", "AVENTURINE");
        toTest[1][1] = new Tile('│', "AMETHYST", "AMETHYST");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            new MazeBoard(toTest);
        });

        assertEquals(expectMessage, exception.toString());
    }

    @Test
    public void NEGATIVE_DIMENSION_SHIFT() {
        String expectMessage = "java.lang.IllegalArgumentException: Check the boards dimensions! Given: -1";

        MazeBoard mazeBoard = new MazeBoard(create_SampleBoard());
        SlideAction action = new SlideAction(-1, Direction.RIGHT);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            mazeBoard.slide(action, BoardTestUtil.create_l());
        });

        assertEquals(expectMessage, exception.toString());
    }

    @Test
    public void OUT_OF_RANGE_POINT() {
        String expectMessage = "java.lang.IllegalArgumentException: Location doesn't exits on the board!";

        MazeBoard mazeBoard = new MazeBoard(create_SampleBoard());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            mazeBoard.getTileAtLocation(new Coordinate(100, 10));
        });

        assertEquals(expectMessage, exception.toString());
    }

    @Test
    public void GET_TILE_AT() {
        Tile[][] tBoard = BoardTestUtil.create_SampleBoard();
        tBoard[0][0] = BoardTestUtil.create_L();
        MazeBoard mazeBoard = new MazeBoard(tBoard);

        Tile tile1 = mazeBoard.getTileAtLocation(new Coordinate(1, 1));
        Map<Direction, Boolean> expected = new HashMap<>();
        assertEquals(tileCreatorHelper(true, true, true, true), tile1.getConnections());

        Tile tile2 = mazeBoard.getTileAtLocation(new Coordinate(0, 0));
        assertEquals(BoardTestUtil.create_L(), tile2);

    }

    @Test
    public void TEST_VALID_SLIDE_6_UP() {
        char[][] sampleConnectors = new char[][]{
                {'─','─','│','─','│','│','─'},
                {'─','─','│','─','─','─','┐'},
                {'─','─','│','─','─','│','│'},
                {'─','─','─','─','─','│','│'},
                {'─','─','─','─','─','│','│'},
                {'└','─','─','└','└','─','┘'},
                {'┼','─','─','─','─','│','─'}
        };

        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard_7x7(sampleConnectors));
        SlideAction testSlide = new SlideAction(6, Direction.UP);

        assert(mazeBoard.validSlideDimension(testSlide));
    }

    @Test
    public void SHIFT_ROW_RIGHT() {
        MazeBoard mazeBoard = new MazeBoard(create_SampleBoard());
        SlideAction action = new SlideAction(0, Direction.RIGHT);
        Pair<Tile, MazeBoard> newBoardSpare = mazeBoard.slide(action, BoardTestUtil.create_l());

        mazeBoard = newBoardSpare.getSecond();

        Tile[][] mockBoard = create_SampleBoard();
        mockBoard[0][0] = BoardTestUtil.create_l();
        mockBoard[0][1] = BoardTestUtil.create_L().rotateClockwise(90);
        mockBoard[0][2] = new Tile('└', "apatite", "beryl").rotateClockwise(180);

        MazeBoard mockMaze = new MazeBoard(mockBoard);

        compareBoards(mockMaze, mazeBoard, newBoardSpare.getFirst(), BoardTestUtil.create_l().rotateClockwise(90));
    }

    @Test
    public void SHIFT_ROW_LEFT() {
        MazeBoard mazeBoard = new MazeBoard(create_SampleBoard());
        SlideAction action = new SlideAction(0, Direction.LEFT);
        Pair<Tile, MazeBoard> newBoardSpare = mazeBoard.slide(action, BoardTestUtil.create_l());

        mazeBoard = newBoardSpare.getSecond();

        Tile[][] mockBoard = create_SampleBoard();
        mockBoard[0][0] =  BoardTestUtil.create_L().rotateClockwise(180);
        mockBoard[0][1] = BoardTestUtil.create_l().rotateClockwise(90);
        mockBoard[0][2] = new Tile('│', "apatite", "apatite");

        MazeBoard mockMaze = new MazeBoard(mockBoard);

        compareBoards(mockMaze, mazeBoard, newBoardSpare.getFirst(), BoardTestUtil.create_L().rotateClockwise(90));
    }

    @Test
    public void SHIFT_COL_UP() {
        MazeBoard mazeBoard = new MazeBoard(create_SampleBoard());
        SlideAction action = new SlideAction(0, Direction.UP);
        Pair<Tile, MazeBoard> newBoardSpare = mazeBoard.slide(action, BoardTestUtil.create_l());

        mazeBoard = newBoardSpare.getSecond();

        Tile[][] mockBoard = create_SampleBoard();
        mockBoard[0][0] =  BoardTestUtil.create_L().rotateClockwise(90);
        mockBoard[1][0] = BoardTestUtil.create_T().rotateClockwise(90);
        mockBoard[2][0] = BoardTestUtil.create_l();

        MazeBoard mockMaze = new MazeBoard(mockBoard);

        compareBoards(mockMaze, mazeBoard, newBoardSpare.getFirst(), BoardTestUtil.create_L().rotateClockwise(90));
    }

    @Test
    public void SHIFT_COL_DOWN() {
        MazeBoard mazeBoard = new MazeBoard(create_SampleBoard());
        SlideAction action = new SlideAction(0, Direction.DOWN);
        Pair<Tile, MazeBoard> newBoardSpare = mazeBoard.slide(action, BoardTestUtil.create_l());

        mazeBoard = newBoardSpare.getSecond();
        Tile[][] mockBoard = create_SampleBoard();

        mockBoard[0][0] =  BoardTestUtil.create_l();
        mockBoard[1][0] = BoardTestUtil.create_L().rotateClockwise(90);
        mockBoard[2][0] = new Tile('└', "apatite", "beryl").rotateClockwise(90);

        MazeBoard mockMaze = new MazeBoard(mockBoard);

        compareBoards(mockMaze, mazeBoard, newBoardSpare.getFirst(), BoardTestUtil.create_T().rotateClockwise(90));
    }

    private void compareBoards(MazeBoard expected, MazeBoard provided, Tile actualSpare, Tile expectedSpare) {
        Tile[][] expectedBoard = expected.copyBoard();
        Tile[][] providedBoard = provided.copyBoard();
        assertEquals(expectedBoard.length, providedBoard.length);
        for (int i = 0; i < expectedBoard.length; i++) {
            assertEquals(expectedBoard[i].length, providedBoard[i].length);

            for (int j = 0; j < expectedBoard[i].length; j++) {
                Map<Direction, Boolean> expectedTileShape = expectedBoard[i][j].getConnections();
                Map<Direction, Boolean> providedTileShape = providedBoard[i][j].getConnections();
                assertEquals(expectedTileShape, providedTileShape);
            }
        }
        assertEquals(expectedSpare.getConnections(), actualSpare.getConnections());
    }
}
