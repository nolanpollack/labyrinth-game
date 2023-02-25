package com.northeastern.labyrinth.Controller.Player;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.Action;

import java.util.Optional;

/**
 * Represents the player programming interface used by the referee to communicate with a player.
 */
public interface IPlayer {

    /**
     * Gets the player's name. '
     */
    String name();

    /**
     * Prompts the player to propose a board with a min. # of rows and columns
     *
     * @return the proposed maze board.
     */
    MazeBoard proposeBoard0(int rows, int columns);

    /**
     * Calls setup for a player according to the spec.
     *
     * @param state0     the state of the game. Will be empty to tell player to go home.
     * @param targetTile the target tile for the player to try to reach.
     */
    void setup(Optional<IPlayerState> state0, Coordinate targetTile);

    /**
     * Prompts the player to execute a turn based on the given IPlayerState.
     *
     * @param publicState the public state of the game.
     * @return the action the player wants to take, or an empty optional if the player wants to pass.
     */
    Optional<Action> takeTurn(IPlayerState<PublicPlayerData> publicState);

    /**
     * The player is informed whether it won or not.
     */
    void win(Boolean w);
}
