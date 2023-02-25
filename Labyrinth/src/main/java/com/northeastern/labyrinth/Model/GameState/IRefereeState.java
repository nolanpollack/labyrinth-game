package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.util.List;
import java.util.Queue;

/**
 * Total knowledge the Referee knows of the game state.
 * The referee state extends the knowledge built in a IPlayerState by adding methods on private information.
 */
public interface IRefereeState extends IPlayerState<PrivatePlayerData> {

    /**
     * Performs a rotation, slide and movement of the current player. If the player is now on their
     * goal tile AND has performed an action, updates that they have reached their goal tile.
     *
     * @param action the action to perform.
     * @return a new copy reflecting the updated referee state.
     */
    IRefereeState performMove(Action action);

    /**
     * Removes from the PrivatePlayerData queue the first player entry.
     *
     * @return a new copy reflecting the updated referee state.
     */
    IRefereeState kickOutCurrentPlayer();

    /**
     * Removes from the PrivatePlayerData queue the given player and his corresponding player entry.
     *
     * @return a new copy reflecting the updated referee state
     */
    IRefereeState kickOutPlayer(IPlayer player);

    /**
     * Checks if the player's current position is on its goal tile.
     */
    Coordinate currentTargetTile();

    /**
     * Moves the active player to a new desired location on the board. Updates the player´s data
     * after the move to represent the target/home tile status.
     *
     * @param coordinate the new location of the player.
     * @return a new copy reflecting the updated referee state
     */
    IRefereeState moveCurrentPlayer(Coordinate coordinate);

    /**
     * Sets the active player to the next player in the list
     *
     * @return a new copy reflecting the updated referee state
     */
    IRefereeState setNextPlayer();

    /**
     * Retrieves the number of players the game state is holding information for
     */
    int numberOfPlayers();

    /**
     * Extracts the player data class of the current player.
     */
    PrivatePlayerData getCurrentPlayerData();

    /**
     * @return An IPlayerState interface version of the current referee state with PublicPlayerData,
     * to be provided to the players.
     */
    IPlayerState<PublicPlayerData> getPlayerState();

    /**
     * Assigned the participant players to playerData.
     *
     * @param playerList the size of the list should be same as the size of the list player data.
     * @return the new referee state.
     * @throws IllegalArgumentException if the size of the list is not the same as the size of the
     *                                  player data list.
     */
    IRefereeState assignPlayerToPlayerData(List<IPlayer> playerList);

    /**
     * Gets the current IPlayer, or an empty optional if an IPlayer hasn't been assigned to the current PlayerData.
     *
     * @return the participant player assigned to current player data if it exists, or the empty optional if it doesn't.
     */
    IPlayer getCurrentPlayer();

    /**
     * Returns a GameState with the spare tile rotated counterclockwise by a degree
     *
     * @param degree the degree to rotate the spare tile. Degrees must be multiple of 90.
     *               Negative values will rotate clockwise.
     * @return a new copy reflecting the updated referee state
     */
    IRefereeState rotateSpare(int degree);

    /**
     * Returns a GameState with a slided row/col by inserting the spare tile, shifting all players on it.
     * Returned GameState will have given action as its new previousAction
     * If the player is outplaced from the board, reassigns its location to the newly inserted Tile.
     * If the slide undoes the last slide action, throws an error
     *
     * @param action the slide action to perform.
     * @return a new copy reflecting the updated referee state
     */
    IRefereeState insertSpare(SlideAction action);

    /**
     * Getter for the queue of goal candidates.
     */
    Queue<Coordinate> getGoalTiles();
}
