package com.northeastern.labyrinth.Controller.Player;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Server.Client;
import com.northeastern.labyrinth.Strategy.IStrategy;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.Action;

import java.util.Optional;

/**
 * A player has a unique game and a strategy.
 * The Referee informs the player where to target its strategy.
 */
public class AIPlayer extends Client implements IPlayer {

    protected final String name;
    protected IStrategy strategy;
    protected Coordinate targetTile;

    public AIPlayer(String name, IStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    @Override
    public String name() {
        return this.name;
    }

    /**
     * Proposes a board where all tiles have all directions and two diamonds.
     * (Technically not a valid board, unused legacy code).
     */
    public MazeBoard proposeBoard0(int rows, int columns) {
        Tile[][] proposedBoard = new Tile[rows][columns];
        for (int i = 0; i < rows; i++) {
            Tile[] row = new Tile[columns];
            for (int j = 0; j < columns; j++) {
                row[j] = new Tile('┼', "diamond", "diamond");
            }
            proposedBoard[i] = row;
        }

        return new MazeBoard(proposedBoard);
    }

    public void setup(Optional<IPlayerState> state0, Coordinate targetTile) {
        this.targetTile = targetTile;
    }


    public Optional<Action> takeTurn(IPlayerState<PublicPlayerData> publicState) {
        return this.strategy.getMove(this.targetTile, publicState);
    }

    public void win(Boolean w) {
        //Is empty because an AI player does not 'care' if it has won or not.
    }

    public Coordinate getTargetTile() {
        return this.targetTile;
    }
}
