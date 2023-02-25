package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Util.Coordinate;

import java.awt.*;

/**
 * Represents the publicly available game data of a single player
 */
public final class PublicPlayerData implements IPlayerData<PublicPlayerData> {
    // Color of a player avatar
    private final Color color;
    // The current location of the player on the Maze.
    private final Coordinate currentPosition;
    // The location of the player's home tile.
    private final Coordinate homePosition;

    public PublicPlayerData(java.awt.Color color, Coordinate currentPosition, Coordinate homePosition) {
        this.color = color;
        this.currentPosition = currentPosition;
        this.homePosition = homePosition;
    }

    @Override
    public Color getAvatarColor() {
        return this.color;
    }

    @Override
    public PublicPlayerData setCurrentPosition(Coordinate coordinate) {
        return new PublicPlayerData(this.color, coordinate, this.homePosition);
    }

    @Override
    public Coordinate getCurrentPosition() {
        return this.currentPosition;
    }

    @Override
    public Coordinate getHomePosition() {
        return this.homePosition;
    }
}
