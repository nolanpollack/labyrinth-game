package com.northeastern.labyrinth.Strategy;

import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.Action;

import java.util.Optional;

/**
 * Represents a strategy that returns an action based off a goal tile and player state.
 */
public interface IStrategy {

    /**
     * Returns an empty Optional if the strategy cannot determine a move (pass) For example, if a
     * strategy cannot move off its position no matter how it moves, it will pass.
     * Tries to find an action to get CURRENT PLAYER to the target tile.
     *
     * @param targetTile the player's current target tile.
     * @param playerState the current public state.
     * @return any action or an empty optional representing a pass.
     */
    Optional<Action> getMove(Coordinate targetTile, IPlayerState<PublicPlayerData> playerState);
}
