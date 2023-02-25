package com.northeastern.labyrinth.Controller;

import com.northeastern.labyrinth.Controller.Player.AIPlayer;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Strategy.EuclidStrategy;
import com.northeastern.labyrinth.Strategy.RiemannStrategy;
import com.northeastern.labyrinth.Util.Coordinate;
import org.junit.jupiter.api.Test;
import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Turn.SlideAction;
import com.northeastern.labyrinth.Util.Turn.Action;

import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AIPlayerTest {


    AIPlayer player1;
    AIPlayer player2;
    Optional<IPlayerState> playerState;

    private void init() {
        player1 = new AIPlayer("Diego", new RiemannStrategy());
        player2 = new AIPlayer("Jacob", new EuclidStrategy());
    }

    private void initState() {
        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard());
        Tile spareTile = new Tile('┐', "diamond", "diamond");
        Queue<PrivatePlayerData> playerData = new LinkedList<>();

        playerData.add(
                new PrivatePlayerData(new Color(255),
                        new Coordinate(0, 0),
                        new Coordinate(0, 2),
                        new Coordinate(0, 2),
                        false, 0)
        );

        playerState = Optional.of(new RefereeState(mazeBoard, spareTile, playerData, new SlideAction()));
    }

    @Test
    public void VERIFY_GET_NAME() {
        init();
        assertEquals("Diego", player1.name());
        assertEquals("Jacob", player2.name());
    }

    @Test
    public void VERIFY_SETUP_WITH_STATE() {
        init();
        initState();
        Coordinate coordinate =  new Coordinate(4,5);
        player1.setup(playerState, coordinate);
        assertEquals(coordinate, player1.getTargetTile());
    }

    @Test
    public void VERIFY_SETUP_WITHOUT_STATE() {
        init();
        Coordinate coordinate =  new Coordinate(4,5);
        player1.setup(Optional.empty(), new Coordinate(4,5));
        assertEquals(coordinate, player1.getTargetTile());
    }


    @Test
    public void TEST_TAKE_TURN() {
        init();
        initState();
        Optional<Action> returnMove = player1.takeTurn(playerState.get());
        Action expectedAction = new Action(new SlideAction(0, Direction.UP), 90, new Coordinate(0, 0));
        assertEquals(expectedAction, returnMove.get());
    }
}

