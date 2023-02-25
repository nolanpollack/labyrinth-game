package com.northeastern.labyrinth.Util;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Controller.Rulebook;
import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Model.GameState.RefereeState;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class Generate {
    private final Random r;
    private final Set<Pair<Gem, Gem>> gemsUsed;

    private final List<Color> AVAILABLE_COLORS = new ArrayList<>(Arrays.asList(
            new Color(255, 0, 255),
            Color.ORANGE,
            Color.PINK,
            Color.RED,
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.WHITE,
            Color.BLACK));


    public Generate() {
        r = new Random();
        gemsUsed = new HashSet<>();
    }

    public Generate(int seed) {
        r = new Random(seed);
        gemsUsed = new HashSet<>();
    }

    // static methods that return generated structures
    public RefereeState generateRefereeState(int maxBoardSize, List<IPlayer> players) {

        Coordinate validBoardSize = generateValidBoardSize(players.size(), maxBoardSize);
        int numRows = validBoardSize.getRow();
        int numColumns = validBoardSize.getCol();

        Queue<Coordinate> goals = generateValidGoals(numRows, numColumns);

        return new RefereeState(generateBoard(numRows, numColumns),
                generateTile(),
                //Note that this mutates goals.
                generatePlayerData(players, numRows, numColumns, goals),
                goals);
    }

    private Coordinate generateValidBoardSize(int numPlayers, int maxBoardSize) {
        int MIN_NUM_ROWS = 2;

        int numRows = r.nextInt(maxBoardSize - MIN_NUM_ROWS) + MIN_NUM_ROWS;

        int minNumColumns = (numPlayers * 2) / (numRows / 2);

        if (minNumColumns < 2) {
            minNumColumns = 2;
        }

        int numColumns = r.nextInt(maxBoardSize - minNumColumns) + minNumColumns;

        return new Coordinate(numRows, numColumns);
    }

    private MazeBoard generateBoard(int numRows, int numColumns) {
        Tile[][] board = new Tile[numRows][numColumns];

        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                board[row][column] = generateTile();
            }
        }

        return new MazeBoard(board);
    }

    /**
     * Generates a tile with random values. Gems is a random Gem that hasn't been used (in gemsUsed). Tile will have at least two directions,
     * as specified by the spec.
     *
     * @return Tile with random valid constraints.
     */
    private Tile generateTile() {
        Gem[] gems = generateGems();

        return new Tile(generateDirections(), gems[0], gems[1]);
    }

    private Gem[] generateGems() {
        Gem gem1 = Gem.values()[r.nextInt(Gem.values().length)];
        Gem gem2 = Gem.values()[r.nextInt(Gem.values().length)];

        while (gemsUsed.contains(new Pair<>(gem1, gem2))) {
            gem1 = Gem.values()[r.nextInt(Gem.values().length)];
            gem2 = Gem.values()[r.nextInt(Gem.values().length)];
        }
        gemsUsed.add(new Pair<>(gem1, gem2));

        return new Gem[]{gem1, gem2};
    }

    /**
     * First, randomly decides how many directions to return with true (between 2-4). Then, randomly chooses which directions
     * to be true.
     *
     * @return a map containing all four possible directions, and a boolean true value for 2-4 of them, and false for the others.
     */
    private Map<Direction, Boolean> generateDirections() {
        int numDirections = r.nextInt(3);

        List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
        for (int i = 0; i < numDirections; i++) {
            int removeIndex = r.nextInt(4 - i);
            directions.remove(removeIndex);
        }

        Map<Direction, Boolean> toReturn = new HashMap<>();

        for (Direction d : Direction.values()) {
            if (directions.contains(d)) {
                toReturn.put(d, true);
            } else {
                toReturn.put(d, false);
            }
        }

        return toReturn;
    }

    /**
     * @param players
     * @param numRows
     * @param numColumns
     * @param goals
     * @return
     */
    private Queue<PrivatePlayerData> generatePlayerData(List<IPlayer> players, int numRows, int numColumns, Queue<Coordinate> goals) {
        Queue<PrivatePlayerData> playerData = new ArrayDeque<>();
        Set<Coordinate> usedHomes = new HashSet<>();

        for (IPlayer player : players) {
            Color playerColor = AVAILABLE_COLORS.get(r.nextInt(AVAILABLE_COLORS.size()));
            AVAILABLE_COLORS.remove(playerColor);


            Coordinate homePosition = generateValidHome(numRows, numColumns);
            while (usedHomes.contains(homePosition)) {
                homePosition = generateValidHome(numRows, numColumns);
            }
            usedHomes.add(homePosition);

            Coordinate currentPosition = homePosition;

            //Note: goals is mutable so this will change the Queue before (currently desired behavior).
            Coordinate goalPosition = goals.poll();

            PrivatePlayerData newPlayer = new PrivatePlayerData(playerColor, currentPosition, homePosition, goalPosition, false, 0);
            //Sets the playerData to have this player and adds them.
            playerData.add(newPlayer.setPlayer(player));
        }

        return playerData;
    }

    /**
     * Generates a coordinate within a board with the given constraints, on a "fixed" tile (odd indices).
     *
     * @param numRows    number of rows coordinate can be on.
     * @param numColumns number of columns coordinate can be on.
     * @return valid home according to the spec.
     */
    private Coordinate generateValidHome(int numRows, int numColumns) {
        //TODO: Change to use rulebook instead of doing manually (done this way to avoid redundant loop).
        int row = r.nextInt(numRows / 2) * 2 + 1;
        int column = r.nextInt(numColumns / 2) * 2 + 1;

        Coordinate homePosition = new Coordinate(row, column);

        if (!Rulebook.isCoordinateFixed(homePosition)) {
            throw new IllegalArgumentException("Illegal row/column");
        }

        return homePosition;
    }

    /**
     * Generates a queue of all valid coordinates within a board in row/column order. Coordinates are valid if they are
     * on a nonmoveable tile, defined as a tile with odd indices.
     *
     * @param numRows
     * @param numColumns
     * @return
     */
    private static Queue<Coordinate> generateValidGoals(int numRows, int numColumns) {
        Queue<Coordinate> returnQueue = new ArrayDeque<>();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                Coordinate toCheck = new Coordinate(i, j);
                if (Rulebook.isCoordinateFixed(toCheck)) {
                    returnQueue.add(toCheck);
                }
            }
        }
        return returnQueue;
    }
}
