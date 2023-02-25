package com.northeastern.labyrinth.Controller;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import java.util.Optional;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

public class MockPlayer implements IPlayer {
    @Override
    public String name() {
        return "Hello";
    }

    @Override
    public MazeBoard proposeBoard0(int rows, int columns) {
        Tile[][] proposedBoard = new Tile[rows][columns];
        for (int i = 0; i < rows; i++) {
            Tile[] row = new Tile[columns];
            for (int j = 0; j < columns; j++) {
                row[j] = new Tile('┼', "TEMP1", "TEMP2");
            }
            proposedBoard[i] = row;
        }

        MazeBoard mazeBoard = new MazeBoard(proposedBoard);

        return mazeBoard;
    }

    @Override
    public void setup(Optional<IPlayerState> state0, Coordinate targetTile) {

    }

    /**
     * Gives an invalid action
     */
    @Override
    public Optional<Action> takeTurn(IPlayerState publicState) {
        return Optional.of(new Action(
                new SlideAction(10, Direction.UP),
                10,
                new Coordinate(-1, -1)));
    }

    @Override
    public void win(Boolean w) {
    }
}
