package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.util.Queue;

/**
 * Represents the subset knowledge available for a player.
 */
public final class PlayerState extends AbstractState<PublicPlayerData> {

    public PlayerState(MazeBoard board, Tile spareTile, Queue<PublicPlayerData> listOfPlayers,
                       SlideAction previousSlideAction) {
        super(board, spareTile, listOfPlayers, previousSlideAction);
    }

    @Override
    public IPlayerState buildState(MazeBoard board, Tile spareTile,
                                   Queue<PublicPlayerData> listOfPlayers, SlideAction previousSlideAction) {
        return new PlayerState(board, spareTile, listOfPlayers, previousSlideAction);
    }

    @Override
    protected boolean validPlayer(PublicPlayerData player) {
        Coordinate currentPosition = player.getCurrentPosition();
        Coordinate homePosition = player.getHomePosition();
        return this.board.validLocation(currentPosition) && this.board.validLocation(homePosition);
    }
}
