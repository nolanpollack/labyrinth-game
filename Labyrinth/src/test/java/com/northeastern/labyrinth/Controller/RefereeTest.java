package com.northeastern.labyrinth.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.northeastern.labyrinth.Controller.Player.AIPlayer;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Controller.Referee.Referee;
import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Strategy.EuclidStrategy;
import com.northeastern.labyrinth.Strategy.RiemannStrategy;
import com.northeastern.labyrinth.Util.ColorHandler;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.Util.Turn.SlideAction;
import java.awt.Color;
import java.util.*;

import com.northeastern.labyrinth.json.Adapter.BadPlayerFactory;
import org.junit.jupiter.api.Test;

public class RefereeTest {

    Referee referee;
    IRefereeState state;
    List<IPlayer> playerList;

    private final char[][] sampleConnectors = new char[][]{
            {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┌', '┌', '┌', '┌', '┌', '┌', '┌'}
    };

    private final char[][] sampleConnectors1 = new char[][]{
            {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┌', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┌', '┌', '┼', '┼', '┼', '┼', '┌'},
            {'┌', '┌', '┼', '┼', '┼', '┼', '┌'},
            {'┌', '┌', '┼', '┼', '┼', '┼', '┌'},
            {'┌', '┌', '┌', '┼', '┼', '┼', '┌'},
            {'┌', '┌', '┌', '┌', '┌', '┌', '┌'}
    };

    private final char[][] rectangle = new char[][]{
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

    // Correct Data
    private Queue<PrivatePlayerData> initData1() {
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

        return playerSample1;
    }

    // goal tiles on movable/slidable positions.
    private Queue<PrivatePlayerData> initData2() {
        Queue<PrivatePlayerData> playerSample1 = new ArrayDeque<>();

        PrivatePlayerData player1 = new PrivatePlayerData(new Color(255),
                new Coordinate(4, 4),
                new Coordinate(1, 1),
                new Coordinate(5, 5),
                true, 0);
        PrivatePlayerData player2 = new PrivatePlayerData(new Color(255, 0, 0),
                new Coordinate(5, 4),
                new Coordinate(0, 0),
                new Coordinate(1, 1),
                true, 0);
        PrivatePlayerData player3 = new PrivatePlayerData(new Color(0, 255, 0),
                new Coordinate(5, 5),
                new Coordinate(2, 2),
                new Coordinate(6, 0),
                true, 0);

        playerSample1.add(player1);
        playerSample1.add(player2);
        playerSample1.add(player3);

        return playerSample1;
    }

    // duplicate home tile
    private Queue<PrivatePlayerData> initData3() {
        Queue<PrivatePlayerData> playerSample1 = new ArrayDeque<>();

        PrivatePlayerData player1 = new PrivatePlayerData(new Color(255),
                new Coordinate(4, 4),
                new Coordinate(1, 1),
                new Coordinate(5, 3),
                false, 0);
        PrivatePlayerData player2 = new PrivatePlayerData(new Color(255, 0, 0),
                new Coordinate(0, 0),
                new Coordinate(1, 1),
                new Coordinate(1, 1),
                false, 0);
        PrivatePlayerData player3 = new PrivatePlayerData(new Color(0, 255, 0),
                new Coordinate(5, 5),
                new Coordinate(1, 1),
                new Coordinate(3, 5),
                false, 0);

        playerSample1.add(player1);
        playerSample1.add(player2);
        playerSample1.add(player3);

        return playerSample1;
    }

    // Correct data, Player has same current, home, goal position
    private Queue<PrivatePlayerData> initData4() {
        Queue<PrivatePlayerData> playerSample1 = new ArrayDeque<>();

        PrivatePlayerData player1 = new PrivatePlayerData(new Color(255),
                new Coordinate(1, 1),
                new Coordinate(3, 3),
                new Coordinate(5, 5),
                false, 0);
        PrivatePlayerData player2 = new PrivatePlayerData(new Color(255, 0, 0),
                new Coordinate(1, 1),
                new Coordinate(1, 1),
                new Coordinate(1, 3),
                false, 0);
        PrivatePlayerData player3 = new PrivatePlayerData(new Color(0, 255, 0),
                new Coordinate(5, 5),
                new Coordinate(5, 5),
                new Coordinate(5, 5),
                false, 0);

        playerSample1.add(player1);
        playerSample1.add(player2);
        playerSample1.add(player3);

        return playerSample1;
    }


    private void initPlayer() {
        IPlayer player1 = new AIPlayer("BluePlayer", new RiemannStrategy());
        IPlayer player2 = new AIPlayer("RedPlayer", new EuclidStrategy());
        IPlayer player3 = new AIPlayer("GreenPlayer", new RiemannStrategy());
        List<IPlayer> playerList = new LinkedList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        this.playerList = playerList;
    }


    private void initReferee() {
        referee = new Referee();
    }


    private void initState(char[][] sampleConnectors, Queue<PrivatePlayerData> playerData) {
        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard_7x7(sampleConnectors));
        Tile spareTile = new Tile('┐', "diamond", "diamond");
        state = new RefereeState(mazeBoard, spareTile, playerData, new SlideAction());
    }


    @Test
    public void TEST_ALL_PASS_ONE_HEADING_HOME() {
        initState(this.sampleConnectors, initData1());
        initReferee();
        initPlayer();

        Pair<List<IPlayer>, List<IPlayer>> returnList = referee.runGame(state, playerList);
        List<IPlayer> winnerPlayers = returnList.getFirst();
        List<IPlayer> misbehavingPlayers = returnList.getSecond();

        assertEquals(1, winnerPlayers.size());
        assertEquals(0, misbehavingPlayers.size());
        assertEquals("BluePlayer", winnerPlayers.get(0).name());
    }

    @Test
    public void PLAYER_ON_GOAL_HOME_NOT_WINNING() {
        initState(this.sampleConnectors1, initData4());
        initReferee();
        initPlayer();
        Pair<List<IPlayer>, List<IPlayer>> returnList = referee.runGame(state, playerList);
        List<IPlayer> winnerPlayers = returnList.getFirst();
        List<IPlayer> misbehavingPlayers = returnList.getSecond();

        assertEquals(1, winnerPlayers.size());
        assertEquals(0, misbehavingPlayers.size());
        assertEquals("BluePlayer", winnerPlayers.get(0).name());

    }

    @Test
    public void NO_MORE_PLAYERS() {
        IPlayer player1 = new MockPlayer();
        IPlayer player2 = new MockPlayer();
        IPlayer player3 = new MockPlayer();
        List<IPlayer> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        initReferee();
        initState(this.sampleConnectors, initData1());

        Pair<List<IPlayer>, List<IPlayer>> returnList = referee.runGame(state, playerList);
        List<IPlayer> winnerPlayers = returnList.getFirst();
        List<IPlayer> misbehavingPlayers = returnList.getSecond();

        assertEquals(0, winnerPlayers.size());
        assertEquals(3, misbehavingPlayers.size());
    }

    @Test
    public void ONE_PLAYER_LEFT() {
        IPlayer player1 = new AIPlayer("BluePlayer", new RiemannStrategy());
        IPlayer player2 = new MockPlayer();
        IPlayer player3 = new MockPlayer();
        List<IPlayer> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        initReferee();
        initState(this.sampleConnectors, initData1());

        Pair<List<IPlayer>, List<IPlayer>> returnList = referee.runGame(state, playerList);
        List<IPlayer> winnerPlayers = returnList.getFirst();
        List<IPlayer> misbehavingPlayers = returnList.getSecond();

        assertEquals(1, winnerPlayers.size());
        assertEquals(2, misbehavingPlayers.size());
        assertEquals("BluePlayer", winnerPlayers.get(0).name());
    }



    @Test
    public void INVALID_GOAL_POSITION() {
        initReferee();
        initState(sampleConnectors1, initData2());
        initPlayer();

        String expectMessage = "java.lang.IllegalArgumentException: Player Goal Tile(s) is not correct in this state";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            referee.runGame(state, playerList);
        });
        assertEquals(expectMessage, exception.toString());
    }


    @Test
    public void INVALID_HOME_DUPLICATE() {
        initReferee();
        initState(sampleConnectors1, initData3());
        initPlayer();

        String expectMessage = "java.lang.IllegalArgumentException: Player Home Tile(s) is not correct in this state";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            referee.runGame(state, playerList);
        });
        assertEquals(expectMessage, exception.toString());
    }


    @Test
    public void SMALL_BOARD() {
        // two players cant play on a 3x3 board due to lack of unique goal tiles.
        // Players data incompatible on board
        // 3 x 3 board
        MazeBoard board = new MazeBoard(BoardTestUtil.create_SampleBoard());
        Queue<PrivatePlayerData> list = new LinkedList<>();
        PrivatePlayerData playerData = new PrivatePlayerData(
                        new Color(255),
                        new Coordinate(0, 0),
                        new Coordinate(1, 1),
                        new Coordinate(1, 1),
                        false, 0);
        list.add(playerData);
        state = new RefereeState(board, new Tile('┌', "hackmanite", "alexandrite"), list);
        initReferee();

        List<IPlayer> list2 = new ArrayList<>();
        IPlayer player = new AIPlayer("BlueName", new EuclidStrategy());
        list2.add(player);

        Pair<List<IPlayer>, List<IPlayer>> end = referee.runGame(state, list2);


        assertEquals(1, end.getFirst().size());
        assertEquals(0, end.getSecond().size());
        assertEquals("BlueName", end.getFirst().get(0).name());
    }

    @Test
    public void RECTANGLE_BOARD() {
        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_Rectangle_9x7(rectangle));
        Tile spareTile = new Tile('┐', "diamond", "diamond");
        state = new RefereeState(mazeBoard, spareTile, initData1(), new SlideAction());
        initPlayer();
        initReferee();

        Pair<List<IPlayer>, List<IPlayer>> end = referee.runGame(state, playerList);


        assertEquals(1, end.getFirst().size());
        assertEquals(0, end.getSecond().size());
        assertEquals("BluePlayer", end.getFirst().get(0).name());
    }



    private final char[][] failingBoard = new char[][]
            {{'┐','─','└','│','─','┐','└'},
            {'│','┼','│','│','┌','┘','┬'},
            {'┐','─','┌','│','├','┴','┤'},
            {'─','─','─','│','┼','│','─'},
            {'┐','└','┌','┘','┬','├','┴'},
            {'┤','┼','│','─','┐','└','┌'},
            {'┘','┬','├','┴','┤','┼','│'}};


    @Test
    public void FAILING_INTEGRATION_TEST() {
        MazeBoard mz = new MazeBoard(BoardTestUtil.create_SampleBoard_7x7(failingBoard));
        Tile spare = new Tile('┤', "diamond", "diamond");

        Queue<PrivatePlayerData> playerSample1 = new ArrayDeque<>();

        PrivatePlayerData player1 = new PrivatePlayerData(new Color(255, 165,0),
                new Coordinate(3, 2),
                new Coordinate(3, 1),
                new Coordinate(3, 1),
                false, 0);

        Color temp = ColorHandler.stringToColor("FFFFCC");

        PrivatePlayerData playerBad = new PrivatePlayerData(new Color(255, 255, 204),
                new Coordinate(1, 3),
                new Coordinate(5, 1),
                new Coordinate(5, 5),
                false, 0);
        PrivatePlayerData player2 = new PrivatePlayerData(new Color(255),
                new Coordinate(0, 1),
                new Coordinate(1, 1),
                new Coordinate(3, 3),
                false, 0);
        PrivatePlayerData player3 = new PrivatePlayerData(new Color(255, 0, 0),
                new Coordinate(0, 2),
                new Coordinate(3, 5),
                new Coordinate(1, 3),
                false, 0);
        PrivatePlayerData player4 = new PrivatePlayerData(new Color(0, 255, 0),
                new Coordinate(0, 3),
                new Coordinate(5, 3),
                new Coordinate(5, 1),
                false, 0);

        playerSample1.add(player1);
        playerSample1.add(playerBad);
        playerSample1.add(player2);
        playerSample1.add(player3);
        playerSample1.add(player4);
        IRefereeState state = new RefereeState(mz, spare, playerSample1, new SlideAction());

        IPlayer iplayer1 = new AIPlayer("ecarl", new EuclidStrategy());
        IPlayer iplayer2 = new AIPlayer("ebob", new EuclidStrategy());
        IPlayer iplayer3 = new AIPlayer("eadam", new EuclidStrategy());
        IPlayer iplayerBad = BadPlayerFactory.createLoopPlayer("Zena", new RiemannStrategy(), "setUp", 2);
        IPlayer iplayer4 = new AIPlayer("oli", new RiemannStrategy());
        List<IPlayer> playerList = new LinkedList<>();

        // Order of the list matters !!!!
        playerList.add(iplayer4);
        playerList.add(iplayerBad);
        playerList.add(iplayer3);
        playerList.add(iplayer2);
        playerList.add(iplayer1);

        initReferee();

        Pair<List<IPlayer>, List<IPlayer>> end = referee.runGame(state, playerList);

        assertEquals(1, end.getFirst().size());
        assertEquals(1, end.getSecond().size());
        assertEquals("oli", end.getFirst().get(0).name());
        assertEquals("Zena", end.getSecond().get(0).name());
    }


    @Test
    public void MOST_TREASURE_SCORING() {
        Queue<PrivatePlayerData> playerSample1 = new ArrayDeque<>();

        PrivatePlayerData player1 = new PrivatePlayerData(new Color(255),
                new Coordinate(1, 1),
                new Coordinate(3, 3),
                new Coordinate(5, 5),
                true, 0);
        PrivatePlayerData player2 = new PrivatePlayerData(new Color(255, 0, 0),
                new Coordinate(1, 1),
                new Coordinate(1, 1),
                new Coordinate(1, 3),
                false, 5);
        playerSample1.add(player1);
        playerSample1.add(player2);

        initState(sampleConnectors1,playerSample1);

        IPlayer iplayer1 = new AIPlayer("eadam", new EuclidStrategy());
        IPlayer iplayer2 = new AIPlayer("oli", new RiemannStrategy());
        List<IPlayer> playerList = new LinkedList<>();
        playerList.add(iplayer1);
        playerList.add(iplayer2);

        initReferee();

        Pair<List<IPlayer>, List<IPlayer>> end = referee.runGame(state, playerList);

        assertEquals(1, end.getFirst().size());
        assertEquals(0, end.getSecond().size());
        //assertEquals("oli", end.getFirst().get(0).name());
    }


    @Test
    public void LAST_TREASURE_SAME_AS_HOME() {

        Queue<Coordinate> goalTiles = new ArrayDeque<>();
        goalTiles.add(new Coordinate(1, 1));
        goalTiles.add(new Coordinate(3, 1));
        goalTiles.add(new Coordinate(3, 5));
        goalTiles.add(new Coordinate(5, 3));
        goalTiles.add(new Coordinate(5, 5));
        goalTiles.add(new Coordinate(3, 3));
        goalTiles.add(new Coordinate(1, 3));

        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard_7x7(this.sampleConnectors1));
        Tile spareTile = new Tile('┐', "diamond", "diamond");
        state = new RefereeState(mazeBoard, spareTile, initData1(), new SlideAction(), goalTiles);

        initReferee();
        initPlayer();

        Pair<List<IPlayer>, List<IPlayer>> returnList = referee.runGame(state, playerList);
        List<IPlayer> winnerPlayers = returnList.getFirst();
        List<IPlayer> misbehavingPlayers = returnList.getSecond();

        assertEquals(1, winnerPlayers.size());
        assertEquals(0, misbehavingPlayers.size());
        assertEquals("RedPlayer", winnerPlayers.get(0).name());
    }

    @Test
    public void LIST_OF_GOAL_TILES_GAME() {

        Queue<Coordinate> goalTiles = new ArrayDeque<>();
        goalTiles.add(new Coordinate(1, 1));
        goalTiles.add(new Coordinate(1, 3));
        goalTiles.add(new Coordinate(3, 1));
        goalTiles.add(new Coordinate(3, 5));
        goalTiles.add(new Coordinate(5, 3));
        goalTiles.add(new Coordinate(5, 5));
        goalTiles.add(new Coordinate(3, 3));

        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard_7x7(this.sampleConnectors1));
        Tile spareTile = new Tile('┐', "diamond", "diamond");
        state = new RefereeState(mazeBoard, spareTile, initData1(), new SlideAction(), goalTiles);

        initReferee();
        initPlayer();

        Pair<List<IPlayer>, List<IPlayer>> returnList = referee.runGame(state, playerList);
        List<IPlayer> winnerPlayers = returnList.getFirst();
        List<IPlayer> misbehavingPlayers = returnList.getSecond();

        assertEquals(1, winnerPlayers.size());
        assertEquals(0, misbehavingPlayers.size());
        assertEquals("GreenPlayer", winnerPlayers.get(0).name());
    }

    @Test
    public void ONLY_PLAYER_CONNECT_GAME() {
        initPlayer();
        initReferee();
        Pair<List<IPlayer>, List<IPlayer>> returnList = referee.runGame(playerList);
        List<IPlayer> winnerPlayers = returnList.getFirst();
        List<IPlayer> misbehavingPlayers = returnList.getSecond();
        //assertEquals(1, winnerPlayers.size());
        //assertEquals(0, misbehavingPlayers.size());
        //assertEquals("GreenPlayer", winnerPlayers.get(0).name());

    }

}
