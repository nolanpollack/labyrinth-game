package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.util.*;

/**
 * An abstract class implements common functionality for a referee and player state found in the IPublicState interface.
 * The generic type abstracts the type of IPlayer data for the list of player data, specific to the class extending.
 *
 * @param <T> IPlayerData can be private or public.
 */
public abstract class AbstractState<T extends IPlayerData<T>> implements IPlayerState<T> {
    //Represents maze board data
    protected final MazeBoard board;
    //Represents spare tile that is generated when a Tile is pushed out from a slide. Initializes with given tile
    protected final Tile spareTile;
    //Represents a list of players currently in the game, ordered by turn order. Index 0 is current player. Assumes non-empty
    protected final Queue<T> playersQueue;
    //Represents the slide move made by the previous player. Initializes to empty SlideAction before a move is made
    protected final SlideAction previousSlideAction;

    public AbstractState(MazeBoard board, Tile spareTile, Collection<T> listOfPlayers,
                         SlideAction previousSlideAction) {
        this.board = board;
        this.spareTile = spareTile;
        // validate coordinates of players
        validListPlayer(listOfPlayers);
        this.playersQueue = new ArrayDeque<>(listOfPlayers);
        this.previousSlideAction = previousSlideAction;
    }

    /**
     * Check if the given list players' current location, home and goal is a valid location on the board.
     */
    private void validListPlayer(Collection<T> listOfPlayers) {
        for (T player : listOfPlayers) {
            if (!validPlayer(player)) {
                throw new IllegalArgumentException("Invalid Player");
            }
        }
    }

    /**
     * Checks valid player data (private or public) tile locations compared to the board.
     */
    protected abstract boolean validPlayer(T player);

    @Override
    public IPlayerState<T> rotateSpare(int degree) {
        Tile newSpare = this.spareTile.rotateClockwise(degree * -1);

        return buildState(this.board, newSpare, this.playersQueue, this.previousSlideAction);
    }

    @Override
    public boolean canReachGivenLocation(Coordinate toSearch) {
        Coordinate playerLocation = playersQueue.peek().getCurrentPosition();

        return this.board.canReachTile(playerLocation, toSearch);
    }

    @Override
    public IPlayerState<T> insertSpare(SlideAction action) {

        if (action.equals(previousSlideAction.getReverseSlide())) {
            throw new IllegalStateException("Given move would undo previous slide!");
        }

        Pair<Tile, MazeBoard> newBoardSpare = this.board.slide(action, this.spareTile);
        Tile newSpareTile = newBoardSpare.getFirst();
        MazeBoard newBoard = newBoardSpare.getSecond();

        Queue<T> newPlayersQueue = new ArrayDeque<>();

        for (T playerData : this.playersQueue) {

            if (willPlayerMove(playerData, action)) {
                playerData = shiftPlayer(playerData, action.getDirection());
            }

            newPlayersQueue.offer(playerData);
        }

        SlideAction newPreviousSlide = action;
        return buildState(newBoard, newSpareTile, newPlayersQueue, newPreviousSlide);
    }

    /**
     * Shifts player one tile in given direction. If this pushes them off the board, wraps location
     */
    private T shiftPlayer(T player, Direction direction) {
        int playerRow = player.getCurrentPosition().getRow();
        int playerCol = player.getCurrentPosition().getCol();

        switch (direction) {
            case UP:
                playerRow = (playerRow == 0) ? this.board.getNumRows() - 1 : playerRow - 1;
                break;
            case DOWN:
                playerRow = (playerRow == this.board.getNumRows() - 1) ? 0 : playerRow + 1;
                break;
            case LEFT:
                playerCol = (playerCol == 0) ? this.board.getNumCols() - 1 : playerCol - 1;
                break;
            case RIGHT:
                playerCol = (playerCol == this.board.getNumCols() - 1) ? 0 : playerCol + 1;
                break;
        }

        return player.setCurrentPosition(new Coordinate(playerRow, playerCol));
    }

    /**
     * checks if player will move on given row or column after a shift
     */
    private boolean willPlayerMove(T player, SlideAction action) {

        if (Direction.isHorizontal(action.getDirection())) {
            return player.getCurrentPosition().getRow() == action.getIndex();
        } else {
            return player.getCurrentPosition().getCol() == action.getIndex();
        }
    }

    @Override
    public boolean validSlideDimension(SlideAction slideAction) {
        return this.board.validSlideDimension(slideAction);
    }

    @Override
    public MazeBoard getBoard() {
        return this.board;
    }

    @Override
    public Coordinate getCurrentPlayerPosition() {
        return playersQueue.peek().getCurrentPosition();
    }

    @Override
    public SlideAction getPreviousSlide() {
        return this.previousSlideAction;
    }

    @Override
    public Tile getSpareTile() {
        return this.spareTile;
    }

    @Override
    public List<T> getListOfPlayers() {
        return new ArrayList<>(this.playersQueue);
    }
}
