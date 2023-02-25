package com.northeastern.labyrinth.Strategy;

import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import java.awt.Color;
import java.util.*;
import org.junit.jupiter.api.Test;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EuclidStrategyTests {

    private final char[][] sampleConnectors = new char[][]{
            {'┼', '┼', '┌', '┌', '┌', '┌', '┌'},
            {'┌', '┌', '┼', '┌', '┌', '┌', '┤'},
            {'┌', '┴', '┼', '┌', '┌', '┌', '┌'},
            {'┼', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┼', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┼', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┼', '┌', '┌', '┌', '┌', '┌', '┌'}
    };

    private IPlayerState initPlayerState(char[][] connectors, char spare, int row, int col) {
        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard_7x7(connectors));

        Tile spareTile = new Tile(spare, "diamond", "diamond");
        Queue<PrivatePlayerData> playerData = new LinkedList<>();

        PrivatePlayerData player1 = new PrivatePlayerData(Color.BLUE,
                new Coordinate(row, col),
                new Coordinate(0, 2),
                new Coordinate(0, 2),
                false, 0);
        PrivatePlayerData player2 = new PrivatePlayerData(Color.RED,
                new Coordinate(1, 0),
                new Coordinate(0, 2),
                new Coordinate(0, 2),
                false, 0);
        PrivatePlayerData player3 = new PrivatePlayerData(Color.PINK,
                new Coordinate(2, 0),
                new Coordinate(0, 2),
                new Coordinate(0, 2),
                false, 0);

        playerData.add(player1);
        playerData.add(player2);
        playerData.add(player3);
        IPlayerState state = new RefereeState(mazeBoard, spareTile, playerData);
        return state;
    }

    @Test
    public void TEST_EUCLID_GET_TO_CORNER() {
        IStrategy euclid = new EuclidStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '┘', 2, 2);
        Coordinate goal = new Coordinate(0, 0);

        Action expectedAction = new Action(new SlideAction(0, Direction.RIGHT), 180, goal);
        Optional<Action> optAction = euclid.getMove(goal, playerState);
        Action actualAction = optAction.get();

        assertEquals(expectedAction, actualAction);
    }

    @Test
    public void TEST_EUCLID_FIND_PUSH_OFF_BOARD() {
        IStrategy euclid = new EuclidStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '┘', 0, 0);
        Coordinate goal = new Coordinate(1, 6);

        Action expectedAction = new Action(new SlideAction(0, Direction.LEFT), 90, goal);
        Optional<Action> optAction = euclid.getMove(goal, playerState);
        Action actualAction = optAction.get();

        assertEquals(expectedAction, actualAction);
    }

    @Test
    public void TEST_EUCLID_MAKE_CANDIDATE_6x1() {
        IStrategy euclid = new EuclidStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '┘', 2, 2);
        Coordinate goal = new Coordinate(6, 6);

        Action expectedAction = new Action(new SlideAction(6, Direction.RIGHT), 270, new Coordinate(6, 1));
        Optional<Action> optAction = euclid.getMove(goal, playerState);
        Action actualAction = optAction.get();

        assertEquals(expectedAction, actualAction);
    }

    @Test
    public void TEST_EUCLID_LONG_RUNTIME_FOR_CANDIDATE() {
        char[][] sampleConnectors = new char[][]{
                {'─', '─', '─', '─', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '─', '─'},
                {'─', '─', '─', '─', '│', '─', '─'},
                {'─', '─', '─', '│', '─', '│', '─'},
                {'─', '─', '─', '─', '│', '└', '─'},
                {'─', '─', '─', '─', '─', '─', '└'}
        };

        IStrategy euclid = new EuclidStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '└', 5, 5);
        Coordinate goal = new Coordinate(0, 0);

        Action expectedAction = new Action(new SlideAction(0, Direction.LEFT), 0, new Coordinate(4, 5));
        Optional<Action> optAction = euclid.getMove(goal, playerState);
        Action actualAction = optAction.get();

        assertEquals(expectedAction, actualAction);
    }

    @Test
    public void TEST_EUCLID_CANT_GET_GOAL() {
        char[][] sampleConnectors = new char[][]{
                {'─', '─', '─', '─', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '─', '─'},
                {'─', '─', '─', '─', '│', '─', '─'},
                {'─', '─', '─', '│', '─', '│', '─'},
                {'─', '─', '─', '─', '│', '└', '─'},
                {'─', '─', '─', '─', '─', '─', '└'}
        };

        IStrategy euclid = new EuclidStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '└', 5, 5);
        Coordinate goal = new Coordinate(5, 5);

        Action expectedAction = new Action(new SlideAction(0, Direction.LEFT), 0, new Coordinate(4, 5));
        Optional<Action> optAction = euclid.getMove(goal, playerState);
        Action actualAction = optAction.get();

        assertEquals(expectedAction, actualAction);
    }
}