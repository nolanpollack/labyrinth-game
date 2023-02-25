package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.util.List;
import java.util.Queue;

/**
 * Represents the public knowledge and functionality of the game state.
 *
 * @param <T> private or public type of IPlayerData.
 */
public interface IPlayerState<T extends IPlayerData<T>> {

    /**
     * Returns a GameState with the spare tile rotated counterclockwise by a degree
     *
     * @param degree the degree to rotate the spare tile. Degrees must be multiple of 90.
     *               Negative values implicitly will rotate clockwise.
     * @return a new copy reflecting the updated player state with rotated spare tile.
     */
    IPlayerState<T> rotateSpare(int degree);

    /**
     * Returns a GameState with a slided row/col by inserting the spare tile, shifting all players on it.
     * Returned GameState will have given action as its new previousAction
     * If the player is outplaced from the board, reassigns its location to the newly inserted Tile.
     * If the slide undoes the last slide action, throws an error
     *
     * @param action the slide action to be performed.
     * @return a new copy reflecting the updated player state
     */
    IPlayerState<T> insertSpare(SlideAction action);


    /**
     * Checks if the current active player has a possible path to the provided Tile at given location.
     *
     * @param toSearch the location of the tile to check.
     * @return true if the player can reach the tile, false otherwise.
     */
    boolean canReachGivenLocation(Coordinate toSearch);

    /**
     * Checks if the given slide action is reversing the previous move's slide.
     */
    boolean validSlideDimension(SlideAction slideAction);

    /**
     * Returns the board
     */
    MazeBoard getBoard();

    /**
     * Gets active player's current position.
     */
    Coordinate getCurrentPlayerPosition();

    /**
     * Gets previous slide action.
     */
    SlideAction getPreviousSlide();

    /**
     * Returns current spare tile.
     */
    Tile getSpareTile();

    /**
     * @return list of players with their T type information
     */
    List<T> getListOfPlayers();

    /**
     * Builds a state of type IPlayerState to preserve the common interface.
     */
    IPlayerState<T> buildState(MazeBoard board, Tile spareTile, Queue<T> listOfPlayers,
                               SlideAction previousSlideAction);
}
