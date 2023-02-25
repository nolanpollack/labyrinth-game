package com.northeastern.labyrinth.Strategy;

import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import java.awt.Color;
import org.junit.jupiter.api.Test;

import java.util.*;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RiemannStrategyTest {

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

        return new RefereeState(mazeBoard, spareTile, playerData);
    }

    @Test
    public void TEST_RIEMANN_GET_TO_CORNER() {
        IStrategy riemann = new RiemannStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '┘', 2, 2);
        Coordinate goal = new Coordinate(0, 0);

        Action expectedAction = new Action(new SlideAction(0, Direction.RIGHT), 180, goal);
        Optional<Action> optAction = riemann.getMove(goal, playerState);
        Action actualAction = optAction.get();

        assertEquals(expectedAction, actualAction);
    }

    @Test
    public void TEST_RIEMANN_FIND_PUSH_OFF_BOARD() {
        IStrategy riemann = new RiemannStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '┘', 0, 0);
        Coordinate goal = new Coordinate(1, 6);

        Action expectedAction = new Action(new SlideAction(0, Direction.LEFT), 90, goal);
        Optional<Action> optAction = riemann.getMove(goal, playerState);
        Action actualAction = optAction.get();

        assertEquals(expectedAction, actualAction);
    }

    @Test
    public void TEST_RIEMANN_MAKE_CANDIDATE_0x0() {
        IStrategy riemann = new RiemannStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '┘', 2, 2);
        Coordinate goal = new Coordinate(6, 6);

        Action expectedAction = new Action(new SlideAction(0, Direction.RIGHT), 180, new Coordinate(0, 0));
        Optional<Action> optAction = riemann.getMove(goal, playerState);
        Action actualAction = optAction.get();

        assertEquals(expectedAction, actualAction);
    }

    @Test
    public void TEST_RIEMANN_LONG_RUNTIME_FOR_CANDIDATE() {
        char[][] sampleConnectors = new char[][]{
                {'─', '─', '─', '─', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '─', '─'},
                {'─', '─', '─', '─', '│', '─', '─'},
                {'─', '─', '─', '│', '─', '│', '─'},
                {'─', '─', '─', '─', '│', '└', '─'},
                {'─', '─', '─', '─', '─', '─', '└'}
        };

        IStrategy riemann = new RiemannStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '└', 5, 5);
        Coordinate goal = new Coordinate(0, 0);

        Action expectedAction = new Action(new SlideAction(0, Direction.LEFT), 0, new Coordinate(4, 5));
        Optional<Action> optAction = riemann.getMove(goal, playerState);
        Action actualAction = optAction.get();

        assertEquals(expectedAction, actualAction);
    }

    @Test
    public void TEST_RIEMANN_GOAL_IS_CURRENT_SHOULD_PASS() {
        char[][] sampleConnectors = new char[][]{
                {'─', '─', '─', '─', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '─', '─'},
                {'─', '─', '─', '─', '│', '─', '─'},
                {'─', '─', '─', '│', '─', '│', '─'},
                {'─', '─', '─', '─', '│', '└', '─'},
                {'─', '─', '─', '─', '─', '─', '└'}
        };

        IStrategy riemann = new RiemannStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '└', 5, 5);
        Coordinate goal = new Coordinate(5, 5);

        Action expectedAction = new Action(new SlideAction(0, Direction.LEFT), 0, new Coordinate(4, 5));
        Optional<Action> optAction = riemann.getMove(goal, playerState);
        Action actualAction = optAction.get();

        assertEquals(expectedAction, actualAction);
    }

    @Test
    public void TEST_RIEMANN_CANNOT_MOVE_PASS() {
        char[][] sampleConnectors = new char[][]{
                {'─', '─', '─', '─', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '─', '─'},
                {'─', '─', '─', '│', '─', '│', '─'},
                {'─', '─', '─', '─', '─', '─', '─'},
                {'─', '─', '─', '│', '│', '│', '│'},
                {'─', '─', '─', '─', '─', '─', '─'}
        };

        IStrategy riemann = new RiemannStrategy();
        IPlayerState playerState = initPlayerState(sampleConnectors, '└', 5, 5);
        Coordinate goal = new Coordinate(0, 0);

        Optional<Action> actualAction = riemann.getMove(goal, playerState);

        assertEquals(Optional.empty(), actualAction);
    }
}