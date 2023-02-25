package com.northeastern.labyrinth.Observer.GUI;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

public class GUITest {
    private final char[][] sampleConnectors = new char[][]{
            {'┼', '┼', '┌', '┌', '┌', '┌', '┌'},
            {'┌', '┌', '┼', '┌', '┌', '┌', '┤'},
            {'┌', '┴', '┼', '┌', '┌', '┌', '┌'},
            {'┼', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┼', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┼', '┌', '┌', '┌', '┌', '┌', '┌'},
            {'┼', '┌', '┌', '┌', '┌', '┌', '┌'}
    };

    private IRefereeState state;

    private void initState() {
        Map<String, Pair<Coordinate, Boolean>> goalTiles = new HashMap();

        goalTiles.put("Blue", new Pair(new Coordinate(0, 0), false));
        goalTiles.put("Red", new Pair(new Coordinate(1, 1), false));
        goalTiles.put("Purple", new Pair(new Coordinate(2, 2), false));

        MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard_7x7(sampleConnectors));
        Tile spareTile = new Tile('┐', "diamond", "diamond");
        Queue<PrivatePlayerData> playerData = new LinkedList<>();



        PrivatePlayerData player1 = new PrivatePlayerData(new Color(255),
                new Coordinate(0, 0),
                new Coordinate(0, 2),
                new Coordinate(0, 2),
                false, 0);
        PrivatePlayerData player2 = new PrivatePlayerData(new Color(255, 0, 0),
                new Coordinate(1, 0),
                new Coordinate(3, 3),
                new Coordinate(3, 2),
                false, 0);
        PrivatePlayerData player3 = new PrivatePlayerData(new Color(0, 255, 0),
                new Coordinate(2, 0),
                new Coordinate(6, 6),
                new Coordinate(6, 2),
                false, 0);

        playerData.add(player1);
        playerData.add(player2);
        playerData.add(player3);

        state = new RefereeState(mazeBoard, spareTile, playerData, new SlideAction());
    }


    @Test
    public void SAMPLE_GUI() {
        initState();
        Queue<IRefereeState> states = new LinkedList();

        Action sampleAction = new Action(new SlideAction(0, Direction.UP), 90, new Coordinate(6, 0));
        IRefereeState secondState = this.state.performMove(sampleAction);

        states.add(this.state);
        states.add(secondState);

        MazeGUI mazeGui = new MazeGUI(states);
        mazeGui.setVisible(true);

        //while(true) {

        //}
    }


}
