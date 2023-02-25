package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Util.Coordinate;

import java.awt.*;

/**
 * Represents the available game data of a single player.
 * This is a self-referential generic type, T is the type of the player data.
 * This is used to allow the player data to be immutable and to enforce using ones own class as a
 * parameter when implementing an interface. Binds the return copy type to the generic type.
 */
public interface IPlayerData<T extends IPlayerData<T>> {

    /**
     * Returns the players color representation.
     */
    Color getAvatarColor();

    /**
     * Returns a player with updated current position.
     *
     * @param coordinate the new current position.
     * @return a new player with updated current position.
     */
    T setCurrentPosition(Coordinate coordinate);

    /**
     * Returns the player's current tile.
     */
    Coordinate getCurrentPosition();

    /**
     * Returns the player's home tile location.
     */
    Coordinate getHomePosition();
}