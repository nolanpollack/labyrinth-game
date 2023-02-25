package com.northeastern.labyrinth.json.Adapter;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.util.Queue;

/**
 * Custom Json Serializer and Deserializer for {@link RefereeState}.
 */
public class RefereeStateAdapter extends AbstractStateAdapter<PrivatePlayerData, RefereeState> {

    @Override
    RefereeState buildState(MazeBoard board, Tile spareTile, Queue<PrivatePlayerData> listPlayerData,
                            SlideAction slideAction, Queue<Coordinate> goals) {
        return new RefereeState(board, spareTile, listPlayerData, slideAction, goals);
    }
}
