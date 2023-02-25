package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Controller.Player.AIPlayer;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.northeastern.labyrinth.Strategy.EuclidStrategy;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class RefereeStateTest {

    IRefereeState gameState;
    Queue<PrivatePlayerData> playerData;

    private void initPlayers() {
        playerData = new LinkedList<>();

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
    }

    private void initIncorrectPlayers() {
        playerData = new LinkedList<>();

        PrivatePlayerData player1 = new PrivatePlayerData(new Color(255),
                new Coordinate(0, 0),
                new Coordinate(0, 2),
                new Coordinate(0, 2),
                false, 0);
        PrivatePlayerData player3 = new PrivatePlayerData(new Color(0,255, 0),
                new Coordinate(2, 0),
                new Coordinate(8, 8),
                new Coordinate(0, 2),
                false, 0);
        playerData.add(player1);
        playerData.add(player3);
    }

    private void initState() {
        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard());
        Tile spareTile = new Tile('┐', "diamond", "diamond");

        gameState = new RefereeState(mazeBoard, spareTile, playerData, new SlideAction());
    }

    @Test
    public void KICK_PLAYER() {
        initPlayers();
        initState();
        assertEquals(new Color(255), gameState.getCurrentPlayerData().getAvatarColor());
        assertEquals(3, gameState.numberOfPlayers());

        gameState = gameState.kickOutCurrentPlayer();


        assertEquals(new Color(255, 0, 0), gameState.getCurrentPlayerData().getAvatarColor());
        assertEquals(2, gameState.numberOfPlayers());

        gameState = gameState.kickOutCurrentPlayer();

        assertEquals(new Color(0, 255, 0), gameState.getCurrentPlayerData().getAvatarColor());
        assertEquals(1, gameState.numberOfPlayers());

        gameState = gameState.kickOutCurrentPlayer();

        assertEquals(0, gameState.numberOfPlayers());
    }

    @Test
    public void SET_NEXT_PLAYER() {
        initPlayers();
        initState();

        assertEquals(new Color(255), gameState.getCurrentPlayerData().getAvatarColor());
        assertEquals(3, gameState.numberOfPlayers());

        gameState.setNextPlayer();

        assertEquals(new Color(255), gameState.getCurrentPlayerData().getAvatarColor());
        assertEquals(3, gameState.numberOfPlayers());

        gameState = gameState.setNextPlayer();

        assertEquals(new Color(255, 0, 0), gameState.getCurrentPlayerData().getAvatarColor());
        assertEquals(3, gameState.numberOfPlayers());

        gameState = gameState.setNextPlayer();

        assertEquals(new Color(0, 255, 0), gameState.getCurrentPlayerData().getAvatarColor());
        assertEquals(3, gameState.numberOfPlayers());

        gameState = gameState.setNextPlayer();

        assertEquals(new Color(255), gameState.getCurrentPlayerData().getAvatarColor());
        assertEquals(3, gameState.numberOfPlayers());
    }



    @Test
    public void INVALID_STATE_CREATED() {
        initIncorrectPlayers();
        String expectMessage = "java.lang.IllegalArgumentException: Invalid Player";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            initState();
        });
        assertEquals(expectMessage, exception.toString());
    }


    @Test
    public void NOT_ENOUGH_PLAYER_TO_PLAYER_DATA() {
        initPlayers();
        initState();
        List<IPlayer> listPlayers = new ArrayList<>();
        IPlayer player = new AIPlayer("FOO", new EuclidStrategy());
        listPlayers.add(player);

        String exceptionMessage = "java.lang.IllegalArgumentException: " +
                "Number of players in Game state not equal to number of connected players!";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            gameState.assignPlayerToPlayerData(listPlayers);
        });
        assertEquals(exceptionMessage, exception.toString());
    }

    @Test
    public void ASSIGN_PLAYER_TO_PLAYER_DATA() {
        initPlayers();
        initState();
        List<IPlayer> listPlayers = new ArrayList<>();


        PrivatePlayerData currentPlayer = gameState.getCurrentPlayerData();
        assertEquals(Optional.empty(), currentPlayer.getPlayer());

        IPlayer player1 = new AIPlayer("FOO1", new EuclidStrategy());
        IPlayer player2 = new AIPlayer("FOO2", new EuclidStrategy());
        IPlayer player3 = new AIPlayer("FOO3", new EuclidStrategy());
        listPlayers.add(player1);
        listPlayers.add(player2);
        listPlayers.add(player3);
        IRefereeState newState = gameState.assignPlayerToPlayerData(listPlayers);

        Coordinate current = new Coordinate(0, 0);
        Coordinate home = new Coordinate(0, 2);
        Coordinate goal = new Coordinate(0, 2);

        currentPlayer = newState.getCurrentPlayerData();

        assertEquals(current, currentPlayer.getCurrentPosition());
        assertEquals(home, currentPlayer.getHomePosition());
        assertEquals(goal, currentPlayer.getTargetTile());
        assertEquals(player1, currentPlayer.getPlayer().get());
        assertFalse(currentPlayer.isTerminateCondition());
        assertFalse(currentPlayer.isGoingHome());

    }

    @Test
    public void RECTANGLE_STATE() {
         char[][] rectangle = new char[][]{
                {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
                {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
                {'┌', '┌', '┼', '┼', '┼', '┼', '┌'},
                {'┌', '┌', '┼', '┼', '┼', '┼', '┌'},
                {'┌', '┌', '┼', '┼', '┼', '┼', '┌'},
                {'┌', '┌', '┌', '┼', '┼', '┼', '┌'},
                {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
                {'┌', '┌', '┌', '┼', '┼', '┼', '┌'},
                {'┌', '┌', '┌', '┼', '┼', '┼', '┌'}
         };

        Queue<PrivatePlayerData> playerSample1 = new ArrayDeque<>();
        PrivatePlayerData player1 = new PrivatePlayerData(new Color(255),
                new Coordinate(4, 4),
                new Coordinate(3, 1),
                new Coordinate(1, 1),
                false, 0);
        PrivatePlayerData player2 = new PrivatePlayerData(new Color(255, 0, 0),
                new Coordinate(5, 4),
                new Coordinate(1, 5),
                new Coordinate(1, 1),
                false, 0);
        PrivatePlayerData player3 = new PrivatePlayerData(new Color(0, 255, 0),
                new Coordinate(5, 4),
                new Coordinate(1, 3),
                new Coordinate(1, 1),
                true, 0);

        playerSample1.add(player1);
        playerSample1.add(player2);
        playerSample1.add(player3);

         MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_Rectangle_9x7(rectangle));
        Tile spareTile = new Tile('┐', "diamond", "diamond");
        IRefereeState state = new RefereeState(mazeBoard, spareTile, playerSample1, new SlideAction());

        state.performMove(new Action(new SlideAction(0, Direction.LEFT), 0, new Coordinate(1, 1)));

    }




}