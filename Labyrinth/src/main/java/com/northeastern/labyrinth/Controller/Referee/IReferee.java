package com.northeastern.labyrinth.Controller.Referee;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Util.Pair;

import java.util.List;

/**
 * Represents the referee of the game: Maze Labyrinth.
 */
public interface IReferee {

    /**
     * Computes a terminal game state given an initial state and list of players.
     *
     * @param state      the initial state of the game.
     * @param playerList the list of players.
     * @return two lists of players.
     * First is the list of winners
     * Second is the list of all players who were removed from the game.
     */
    Pair<List<IPlayer>, List<IPlayer>> runGame(IRefereeState state, List<IPlayer> playerList);

    /**
     * Generates a game state given a list of players, then runs the game and returns the results.
     *
     * @param playerList list of players to play the game.
     * @return two lists of players.
     * First is the list of winners
     * Second is the list of all players who were removed from the game.
     */
    Pair<List<IPlayer>, List<IPlayer>> runGame(List<IPlayer> playerList);
}
