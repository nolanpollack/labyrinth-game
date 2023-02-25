package com.northeastern.labyrinth.json.Adapter;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.GameState.PlayerState;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.util.Queue;

/**
 * Custom Json Serializer and Deserializer for {@link PlayerState}.
 */
public class PlayerStateAdapter extends AbstractStateAdapter<PublicPlayerData, PlayerState> {

    @Override
    PlayerState buildState(MazeBoard board, Tile spareTile, Queue<PublicPlayerData> listPlayerData,
                           SlideAction slideAction, Queue<Coordinate> goals) {
        return new PlayerState(board, spareTile, listPlayerData, slideAction);
    }
}
