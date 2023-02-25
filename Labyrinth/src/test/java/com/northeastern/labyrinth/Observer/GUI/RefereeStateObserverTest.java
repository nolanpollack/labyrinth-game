package com.northeastern.labyrinth.Observer.GUI;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Observer.RefereeStateObserver;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class RefereeStateObserverTest {

    private IRefereeState createState() {
        Map<Color, Pair<Coordinate, Boolean>> goalTiles = new HashMap();




        goalTiles.put(new Color(255), new Pair(new Coordinate(0, 0), false));
        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard());
        Tile spareTile = new Tile('┐', "diamond", "diamond");
        Queue<PrivatePlayerData> playerData = new LinkedList<>();
        PrivatePlayerData player = new PrivatePlayerData(new Color(255),
                new Coordinate(0, 0),
                new Coordinate(0, 2),
                new Coordinate(0, 2),
                false, 0);
        playerData.add(player);

        return new RefereeState(mazeBoard, spareTile, playerData, new SlideAction());
    }

    @Test
    public void TEST_OBSERVER_ALREADY_CLOSED() {
        RefereeStateObserver observer = new RefereeStateObserver();

        observer.closeUpdates();

        try {
            observer.closeUpdates();
            assert(false);
        } catch (IllegalStateException e) {
            assertEquals("java.lang.IllegalStateException: com.northeastern.labyrinth.Observer already closed!", e.toString());
        }
    }

    @Test
    public void TEST_OBSERVER_NO_GUI() {
        RefereeStateObserver observer = new RefereeStateObserver();

        assertFalse(observer.closeUpdates());
    }

    @Test
    public void TEST_OBSERVER_GUI() {
        RefereeStateObserver observer = new RefereeStateObserver();

        observer.update(createState());
        assert(observer.closeUpdates());
    }
}
