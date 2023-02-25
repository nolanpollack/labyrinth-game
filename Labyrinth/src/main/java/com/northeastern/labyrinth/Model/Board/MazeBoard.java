package com.northeastern.labyrinth.Model.Board;

import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Gem;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.util.*;

/**
 * Represents a Maze game board as a 2-D array of Tiles.
 * Coordinate grid system for representation of tile positioning. Odd index row/col are unmovable.
 * Rows and Column index start from top left and count from zero.
 */
public final class MazeBoard {

    //Represents the game board as a 2-D array of Tiles.
    private final Tile[][] board;
    //Number of rows the board has, equal to number of arrays in given board
    private final int numRows;
    //Number of columns the board has, equal to length of each array in given board
    private final int numCols;

    /**
     * Initializes the Maze with the provided array of tiles
     */
    public MazeBoard(Tile[][] board) {
        this.numRows = board.length;
        this.numCols = board[0].length;
        this.board = new Tile[numRows][numCols];
        createValidBoard(board);
    }

    /**
     * Sets the class 2D array field to the constructor input.
     * Throws an exception if the provided 2D array of tiles doesn't meet the unique treasure constraint.
     *
     * @param board 2D array of tiles.
     */
    private void createValidBoard(Tile[][] board) {
        Set<Pair<Gem, Gem>> treasureSet = new HashSet<>();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Gem[] currentTreasure = board[i][j].getTreasure();
                Pair<Gem, Gem> pairGem = new Pair<>(currentTreasure[0], currentTreasure[1]);

                if (treasureSet.contains(pairGem)) {
                    throw new IllegalArgumentException("NON UNIQUE TREASURES IN PROVIDED BOARD");
                }
                this.board[i][j] = board[i][j];
                treasureSet.add(pairGem);
            }
        }
    }

    /**
     * Slides a specified row/col towards given direction. Puts given spareTile into empty spot
     * created and returns the pushed out tile.
     *
     * @param action    slide action to be performed.
     * @param spareTile tile to be placed in empty spot created by slide.
     * @return the tile that was pushed out of the board.
     * @throws IllegalArgumentException if the slide action is invalid, according to validSlideDimension.
     */
    public Pair<Tile, MazeBoard> slide(SlideAction action, Tile spareTile)
            throws IllegalArgumentException {
        if (!validSlideDimension(action)) {
            throw new IllegalArgumentException(
                    "Check the boards dimensions! Given: " + action.getIndex());
        }
        Direction direction = action.getDirection();

        switch (direction) {
            case UP:
                return performSlideUp(action.getIndex(), spareTile);
            case DOWN:
                return performSlideDown(action.getIndex(), spareTile);
            case RIGHT:
                return performSlideRight(action.getIndex(), spareTile);
            case LEFT:
                return performSlideLeft(action.getIndex(), spareTile);
            default:
                throw new IllegalStateException("Unexpected Direction Value: " + direction);
        }
    }

    /**
     * /**
     * Calculates a possible path from two coordinates on the MazeBoard through connectors.
     *
     * @param fromTile tile to search from.
     * @param toTile   tile to search to.
     * @return true if there is a path from fromTile to toTiles.
     * @throws IllegalArgumentException if the fromTile or toTile is not on the board.
     */
    public boolean canReachTile(Coordinate fromTile, Coordinate toTile) {
        if (!validLocation(fromTile) || !validLocation(toTile)) {
            throw new IllegalArgumentException("Location doesn't exits on the board!");
        }

        if (fromTile.equals(toTile)) {
            return true;
        }

        return canReachTileHelper(fromTile, toTile, new boolean[numRows][numCols]);
    }

    /**
     * Returns whether there is a connection from a tile to a tile IGNORING explored tiles. Marks the
     * fromTile as explored.
     *
     * @param fromTile   tile to search from.
     * @param toTile     tile to search to.
     * @param isExplored 2D array of booleans representing true for each tile that already has been explored.
     * @return true if there is a path from fromTile to toTile.
     */
    private boolean canReachTileHelper(Coordinate fromTile, Coordinate toTile,
                                       boolean[][] isExplored) {
        int row = fromTile.getRow();
        int col = fromTile.getCol();

        isExplored[row][col] = true;

        Tile tile = board[row][col];
        Map<Direction, Boolean> validEdges = tile.getConnections();

        return exploreHelper(validEdges, Direction.UP, new Coordinate(row - 1, col), toTile, isExplored)
                || exploreHelper(validEdges, Direction.RIGHT, new Coordinate(row, col + 1), toTile,
                isExplored)
                || exploreHelper(validEdges, Direction.DOWN, new Coordinate(row + 1, col), toTile,
                isExplored)
                || exploreHelper(validEdges, Direction.LEFT, new Coordinate(row, col - 1), toTile,
                isExplored);

    }

    /**
     * Helper method for canReachTileHelper. Checks if the given direction is valid and if the tile
     * has been explored. If not, it calls canReachTileHelper on the new tile
     *
     * @param validEdges      map of directions from previous tile. True if there is a connection in that direction.
     * @param direction       of new tile being checked.
     * @param exploreLocation location of new tile being checked.
     * @param desiredLocation location of eventual destination tile.
     * @param isExplored      2D array of booleans representing true for each tile that already has been explored.
     * @return true if there is a path from exploreLocation to desiredLocation.
     */
    private boolean exploreHelper(Map<Direction, Boolean> validEdges, Direction direction,
                                  Coordinate exploreLocation,
                                  Coordinate desiredLocation, boolean[][] isExplored) {
        boolean validVisit = validLocation(exploreLocation)
                && !isExplored[exploreLocation.getRow()][exploreLocation.getCol()]
                && getTileAtLocation(exploreLocation).getConnections()
                .get(Direction.getOppositeDirection(direction))
                && validEdges.get(direction);

        return validVisit && (exploreLocation.equals(desiredLocation) || canReachTileHelper(
                exploreLocation, desiredLocation, isExplored));
    }

    /**
     * Returns the tile at the given location.
     *
     * @param location location of the tile to be returned.
     * @return tile at given location.
     * @throws IllegalArgumentException if the location is not on the board.
     */
    public Tile getTileAtLocation(Coordinate location) throws IllegalArgumentException {
        if (validLocation(location)) {
            return board[location.getRow()][location.getCol()];
        } else {
            throw new IllegalArgumentException("Location doesn't exits on the board!");
        }
    }

    /**
     * Returns if dimension given to row/col slide is valid. A valid column/row are even numbers/
     *
     * @param action slide action to be performed.
     * @return true if the dimension is valid.
     */
    public boolean validSlideDimension(SlideAction action) {
        int boundIndex = numCols;
        if (Direction.isHorizontal(action.getDirection())) {
            boundIndex = numRows;
        }

        int indexToCheck = action.getIndex();

        return action.getIndex() < boundIndex
                && indexToCheck >= 0
                && indexToCheck % 2 == 0;
    }


    /**
     * Returns a list of the coordinates of all accessible Tiles from given Tile coordinate (including
     * base tile).
     *
     * @param location coordinate of the tile to be used as start point for reachability.
     * @return list of coordinates of all accessible tiles from given tile.
     */
    public List<Coordinate> reachableTiles(Coordinate location) {
        if (!validLocation(location)) {
            throw new IllegalArgumentException("Location doesn't exits on the board!");
        }

        List<Coordinate> returnList = new ArrayList<>();
        reachableTilesHelper(location, returnList);
        Collections.sort(returnList);

        return returnList;
    }

    /**
     * Accumulates a list of the coordinates of all accessible Tiles from given Tile coordinate IGNORING
     * explored tiles.
     *
     * @param location      coordinate of the tile to be used as start point for reachability.
     * @param exploredTiles list of coordinates of tiles that have already been explored.
     */
    private void reachableTilesHelper(Coordinate location, List<Coordinate> exploredTiles) {
        exploredTiles.add(location);

        int row = location.getRow();
        int col = location.getCol();

        Tile tile = board[row][col];

        Map<Direction, Boolean> validEdges = tile.getConnections();

        if (validEdges.get(Direction.UP)) {
            exploreLocation(new Coordinate(row - 1, col), Direction.DOWN, exploredTiles);
        }

        if (validEdges.get(Direction.RIGHT)) {
            exploreLocation(new Coordinate(row, col + 1), Direction.LEFT, exploredTiles);
        }

        if (validEdges.get(Direction.DOWN)) {
            exploreLocation(new Coordinate(row + 1, col), Direction.UP, exploredTiles);
        }

        if (validEdges.get(Direction.LEFT)) {
            exploreLocation(new Coordinate(row, col - 1), Direction.RIGHT, exploredTiles);
        }
    }

    /**
     * For the given Coordinate: checks if it is valid, has not already been visited, and the Tile at
     * the location connects to the previous location's Tile, then recursively searches connecting Tiles
     * at that location. Otherwise, does not search and returns exploredTiles
     *
     * @param exploreLocation     location of the tile to be used as start point for exploring.
     * @param connectingDirection direction of the previous location's tile that connects to the tile at exploreLocation.
     * @param exploredTiles       list of coordinates of tiles that have already been explored.
     */
    private void exploreLocation(Coordinate exploreLocation,
                                 Direction connectingDirection, List<Coordinate> exploredTiles) {
        boolean validVisit = validLocation(exploreLocation)
                && !exploredTiles.contains(exploreLocation)
                && getTileAtLocation(exploreLocation).getConnections().get(connectingDirection);

        if (validVisit) {
            reachableTilesHelper(exploreLocation, exploredTiles);
        }
    }

    /**
     * Returns if location represented by Coordinate is valid. A Coordinate is valid if:
     * <ul>
     *     <li>It is not null</li>
     *     <li>Neither row nor column are less than 0</li>
     *     <li>Row is not greater than number of rows in board col</li>
     *     <li>Column is not greater than number of columns in board</li>
     * </ul>
     *
     * @param location location to be checked.
     * @return true if location is valid.
     */
    public boolean validLocation(Coordinate location) {
        if (location == null) {
            return false;
        }
        return location.getRow() >= 0
                && location.getRow() < numRows
                && location.getCol() >= 0
                && location.getCol() < numCols;
    }


    /**
     * Inserts the spare tile and shifts an entire row left
     *
     * @param toMove   Row index.
     * @param oldSpare The spare tile to be inserted.
     * @return displaced Tile and new board with shift.
     */
    private Pair<Tile, MazeBoard> performSlideLeft(int toMove, Tile oldSpare) {
        Tile[][] copyBoard = this.copyBoard();
        Tile newSpare = copyBoard[toMove][0];

        for (int i = 0; i < numCols - 1; i++) {
            copyBoard[toMove][i] = this.board[toMove][i + 1];
        }

        copyBoard[toMove][numCols - 1] = oldSpare;
        return new Pair<>(newSpare, new MazeBoard(copyBoard));
    }

    /**
     * Inserts the spare tile and shifts an entire row right
     *
     * @param toMove   Row index.
     * @param oldSpare The spare tile to be inserted.
     * @return displaced Tile and new board with shift.
     */
    private Pair<Tile, MazeBoard> performSlideRight(int toMove, Tile oldSpare) {
        Tile[][] copyBoard = this.copyBoard();
        Tile newSpare = copyBoard[toMove][numCols - 1];

        for (int i = 0; i < numCols - 1; i++) {
            copyBoard[toMove][i + 1] = this.board[toMove][i];
        }

        copyBoard[toMove][0] = oldSpare;
        return new Pair<>(newSpare, new MazeBoard(copyBoard));
    }

    /**
     * Inserts the spare tile and shifts an entire column up
     *
     * @param toMove   Column index.
     * @param oldSpare The spare tile to be inserted.
     * @return displaced Tile and new board with shift.
     */
    private Pair<Tile, MazeBoard> performSlideUp(int toMove, Tile oldSpare) {
        Tile[][] copyBoard = this.copyBoard();
        Tile newSpare = copyBoard[0][toMove];

        for (int i = 0; i < numRows - 1; i++) {
            copyBoard[i][toMove] = this.board[i + 1][toMove];
        }

        copyBoard[numRows - 1][toMove] = oldSpare;
        return new Pair<>(newSpare, new MazeBoard(copyBoard));
    }


    /**
     * Inserts the spare tile and shifts an entire column down
     *
     * @param toMove   Column index.
     * @param oldSpare The spare tile to be inserted.
     * @return displaced Tile and new board with shift.
     */
    private Pair<Tile, MazeBoard> performSlideDown(int toMove, Tile oldSpare) {
        Tile[][] copyBoard = this.copyBoard();
        Tile newSpare = copyBoard[numRows - 1][toMove];

        for (int i = 0; i < numRows - 1; i++) {
            copyBoard[i + 1][toMove] = this.board[i][toMove];
        }

        copyBoard[0][toMove] = oldSpare;
        return new Pair<>(newSpare, new MazeBoard(copyBoard));

    }

    /**
     * Returns a copy of the board.
     */
    public Tile[][] copyBoard() {
        Tile[][] copyOfBoard = new Tile[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                copyOfBoard[i][j] = board[i][j];
            }
        }
        return copyOfBoard;
    }

    /**
     * Returns number of rows in board.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Returns number of columns in board.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Returns a list of all slide-able row indices in the board.
     */
    public List<Integer> getSlidableRows() {
        List<Integer> slidableRows = new ArrayList<>();
        for (int i = 0; i < this.numRows; i++) {
            if (i % 2 == 0) {
                slidableRows.add(i);
            }
        }

        return slidableRows;
    }

    /**
     * Returns a list of all slide-able columns indices in the board
     */
    public List<Integer> getSlidableColumns() {
        List<Integer> slidableCols = new ArrayList<>();
        for (int i = 0; i < this.numCols; i++) {
            if (i % 2 == 0) {
                slidableCols.add(i);
            }
        }

        return slidableCols;
    }
}