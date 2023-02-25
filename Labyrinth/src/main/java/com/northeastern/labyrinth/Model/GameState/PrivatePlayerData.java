package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Util.Coordinate;

import java.awt.*;
import java.util.Optional;

/**
 * Represents the private game data of a single player.
 */
public final class PrivatePlayerData implements IPlayerData<PrivatePlayerData> {

    // Color of a player avatar
    private final Color color;
    // The current location of the player on the Maze.
    private final Coordinate currentPosition;
    // The location of the player's home tile.
    private final Coordinate homeTile;
    // the location of the player's current target tile.
    private final Coordinate targetTile;
    // does the player's target tile represent its home tile.
    private final boolean goingHome;
    // has a player won
    private final boolean terminateCondition;
    // IPlayer equivalent of the private player data. This is optional because PlayerData can be created
    // before a player is associated with the data.
    private final Optional<IPlayer> player;
    // number of treasures/targets this player has collected.
    private final int treasures;

    public PrivatePlayerData(Color color, Coordinate currentPosition,
                             Coordinate homePosition, Coordinate goalPosition, boolean goingHome, int treasures) {
        this(color, currentPosition, homePosition, goalPosition, goingHome, false, Optional.empty(), treasures);
    }

    private PrivatePlayerData(Color color, Coordinate currentPosition, Coordinate homeTile,
                              Coordinate targetTile, boolean goingHome, boolean terminateCondition, Optional<IPlayer> player, int treasures) {
        this.color = color;
        this.currentPosition = currentPosition;
        this.homeTile = homeTile;
        this.targetTile = targetTile;
        this.goingHome = goingHome;
        this.terminateCondition = terminateCondition;
        this.player = player;
        this.treasures = treasures;
    }

    @Override
    public Color getAvatarColor() {
        return this.color;
    }

    @Override
    public PrivatePlayerData setCurrentPosition(Coordinate coordinate) {
        return new PrivatePlayerData(this.color, coordinate, this.homeTile, this.targetTile,
                this.goingHome, this.terminateCondition, this.player, this.treasures);
    }

    public PrivatePlayerData setTargetTile(Coordinate newTarget) {
        return new PrivatePlayerData(this.color, this.currentPosition, this.homeTile, newTarget,
                this.goingHome, this.terminateCondition, this.player, this.treasures);
    }

    public PrivatePlayerData setPlayer(IPlayer player) {
        return new PrivatePlayerData(this.color, this.currentPosition, this.homeTile,
                this.targetTile, this.goingHome, this.terminateCondition, Optional.of(player), this.treasures);
    }

    /**
     * Returns the player with their boolean goal flag set to true.
     */
    public PrivatePlayerData setGoingHome() {
        return new PrivatePlayerData(this.color, this.currentPosition, this.homeTile,
                this.homeTile, true, false, this.player, this.treasures);
    }

    /**
     * Is the players target tile its home tile and is tracking home?
     */
    public boolean isGoingHome() {
        return this.goingHome;
    }

    /**
     * Returns the player boolean goal flag.
     */
    public PrivatePlayerData completedGame() {
        return new PrivatePlayerData(this.color, this.currentPosition, this.homeTile,
                this.targetTile, true, true, this.player, this.treasures);
    }

    @Override
    public Coordinate getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public Coordinate getHomePosition() {
        return homeTile;
    }

    /**
     * Returns the player's goal tile location.
     */
    public Coordinate getTargetTile() {
        return targetTile;
    }

    /**
     * Returns the actual player.
     */
    public Optional<IPlayer> getPlayer() {
        return this.player;
    }

    /**
     * Has the player made it back home?
     */
    public boolean isTerminateCondition() {
        return this.terminateCondition;
    }


    /**
     * Getter for the player data treasure count.
     */
    public int getTreasuresCount() {
        return this.treasures;
    }

    /**
     * Increments the treasure count of the player data by one.
     */
    public PrivatePlayerData incrementTreasure() {
        return new PrivatePlayerData(this.color, this.currentPosition, this.homeTile, this.targetTile,
                this.goingHome, this.terminateCondition, this.player, this.treasures + 1);
    }


}
