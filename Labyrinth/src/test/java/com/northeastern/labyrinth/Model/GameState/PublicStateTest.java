package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Turn.SlideAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PublicStateTest {

    IPlayerState playerState;

    private void init() {
        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard());
        Tile spareTile = new Tile('┐', "diamond", "diamond");
        Queue<PrivatePlayerData> playerData = new LinkedList<>();



        PrivatePlayerData player1 = new PrivatePlayerData(new Color(255),
                new Coordinate(0, 0),
                new Coordinate(0, 2),
                new Coordinate(0, 2),
                false, 0);
        PrivatePlayerData player2 = new PrivatePlayerData(new Color(255, 0, 0),
                new Coordinate(1, 0),
                new Coordinate(0, 2),
                new Coordinate(0, 2),
                false, 0);
        PrivatePlayerData player3 = new PrivatePlayerData(new Color(0,255, 0),
                new Coordinate(2, 0),
                new Coordinate(0, 2),
                new Coordinate(0, 2),
                false, 0);

        playerData.add(player1);
        playerData.add(player2);
        playerData.add(player3);

        playerState = new RefereeState(mazeBoard, spareTile, playerData, new SlideAction());
    }

    @Test
    public void GET_SPARE_PIECE() {
        init();
        Tile mockSpare = new Tile('┐', "diamond", "diamond");
        assertEquals(playerState.getSpareTile().getConnections(), mockSpare.getConnections());
    }

    @Test
    public void ROTATE_SPARE_PIECE() {
        init();
        playerState = playerState.rotateSpare(90);
        Tile mockTile = new Tile('┌', "diamond", "diamond");
        assertEquals(playerState.getSpareTile().getConnections(), mockTile.getConnections());
    }

    @Test
    public void CAN_REACH_LOCATION() {
        init();
        boolean canReach = playerState.canReachGivenLocation(new Coordinate(0, 0));
        assert(canReach);
    }

    @Test
    public void CANNOT_REACH_LOCATION() {
        init();
        boolean canReach = playerState.canReachGivenLocation(new Coordinate(0, 2));
        assert(!canReach);
    }

    @Test
    public void TEST_SHIFTING_NEW_PLAYER_LOCATION() {
        init();

        playerState = playerState.insertSpare(new SlideAction(0, Direction.DOWN));

        Coordinate expectedPlayer1Location = new Coordinate(1, 0);
        Coordinate expectedPlayer2Location = new Coordinate(2, 0);
        Coordinate expectedPlayer3Location = new Coordinate(0, 0);

        List<PrivatePlayerData> updatedPlayerData = playerState.getListOfPlayers();

        assertEquals(expectedPlayer1Location, updatedPlayerData.get(0).getCurrentPosition());
        assertEquals(expectedPlayer2Location, updatedPlayerData.get(1).getCurrentPosition());
        assertEquals(expectedPlayer3Location, updatedPlayerData.get(2).getCurrentPosition());

        playerState = playerState.insertSpare(new SlideAction(2, Direction.UP));

        expectedPlayer1Location = new Coordinate(1, 0);
        expectedPlayer2Location = new Coordinate(2, 0);
        expectedPlayer3Location = new Coordinate(0, 0);

        updatedPlayerData = playerState.getListOfPlayers();

        assertEquals(expectedPlayer1Location, updatedPlayerData.get(0).getCurrentPosition());
        assertEquals(expectedPlayer2Location, updatedPlayerData.get(1).getCurrentPosition());
        assertEquals(expectedPlayer3Location, updatedPlayerData.get(2).getCurrentPosition());

        playerState = playerState.insertSpare(new SlideAction(2, Direction.RIGHT));

        expectedPlayer1Location = new Coordinate(1, 0);
        expectedPlayer2Location = new Coordinate(2, 1);
        expectedPlayer3Location = new Coordinate(0, 0);

        updatedPlayerData = playerState.getListOfPlayers();

        assertEquals(expectedPlayer1Location, updatedPlayerData.get(0).getCurrentPosition());
        assertEquals(expectedPlayer2Location, updatedPlayerData.get(1).getCurrentPosition());
        assertEquals(expectedPlayer3Location, updatedPlayerData.get(2).getCurrentPosition());

        playerState = playerState.insertSpare(new SlideAction(0, Direction.LEFT));

        expectedPlayer1Location = new Coordinate(1, 0);
        expectedPlayer2Location = new Coordinate(2, 1);
        expectedPlayer3Location = new Coordinate(0, 2);

        updatedPlayerData = playerState.getListOfPlayers();

        assertEquals(expectedPlayer1Location, updatedPlayerData.get(0).getCurrentPosition());
        assertEquals(expectedPlayer2Location, updatedPlayerData.get(1).getCurrentPosition());
        assertEquals(expectedPlayer3Location, updatedPlayerData.get(2).getCurrentPosition());
    }

    @Test
    public void TEST_SHIFTING_WHEN_WOULD_UNDO() {
        init();

        String expectMessage = "java.lang.IllegalStateException: Given move would undo previous slide!";

        playerState = playerState.insertSpare(new SlideAction(0, Direction.DOWN));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            playerState = playerState.insertSpare(new SlideAction(0, Direction.UP));
        });

        assertEquals(expectMessage, exception.toString());

        exception = assertThrows(RuntimeException.class, () -> {
            playerState = playerState.insertSpare(new SlideAction(0, Direction.UP));
        });

        assertEquals(expectMessage, exception.toString());

        playerState = playerState.insertSpare(new SlideAction(0, Direction.LEFT));

        exception = assertThrows(RuntimeException.class, () -> {
            playerState = playerState.insertSpare(new SlideAction(0, Direction.RIGHT));
        });

        assertEquals(expectMessage, exception.toString());

        try {
            playerState = playerState.insertSpare(new SlideAction(0, Direction.DOWN));
            playerState = playerState.insertSpare(new SlideAction(2, Direction.UP));
            assert(true);
        } catch (IllegalStateException e) {
            assert(false);
        }
    }
}