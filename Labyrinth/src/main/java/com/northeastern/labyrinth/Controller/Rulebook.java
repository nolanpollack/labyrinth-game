package com.northeastern.labyrinth.Controller;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.util.*;

/**
 * Represents the Rules and Constraint for this version of Labyrinth.
 */
public class Rulebook {
    public final static int ROUND_LIMIT = 1000;

    /**
     * Ensures the uniqueness and correctness of home tiles for each player in the referee state.
     * A home tile is "correct" if it exists on an unslidable column/row of the maze board in state.
     */
    public static void validHomeTiles(IRefereeState state) {
        List<PrivatePlayerData> listPlayers = state.getListOfPlayers();
        Set<Coordinate> homes = new HashSet<>();

        for (PrivatePlayerData playerData : listPlayers) {
            Coordinate homeTile = playerData.getHomePosition();
            if (homes.contains(homeTile) || !isCoordinateFixed(state, homeTile)) {
                throw new IllegalArgumentException("Player Home Tile(s) is not correct in this state");
            }
            homes.add(homeTile);
        }
    }


    /**
     * Ensures the correctness of goal tiles for each player in a referee state.
     * A goal tile is correct if it exists on an unslidable column/row to the maze board in state.
     */
    public static void validGoalTiles(IRefereeState state) {
        List<PrivatePlayerData> listPlayers = state.getListOfPlayers();

        Queue<Coordinate> goalTiles = state.getGoalTiles();

        for (Coordinate coordinate : goalTiles) {
            if (!isCoordinateFixed(state, coordinate)) {
                throw new IllegalArgumentException("Goal Tile in queue of candidates not correct in this state");
            }
        }

        for (PrivatePlayerData playerData : listPlayers) {
            Coordinate goalTile = playerData.getTargetTile();
            if (!isCoordinateFixed(state, goalTile)) {
                throw new IllegalArgumentException("Player Goal Tile(s) is not correct in this state");
            }
        }
    }


    /**
     * Check if the given coordinate is a fixed coordinate (unslidable).
     *
     * @return true if the given coordinate is a fixed coordinate
     */
    public static boolean isCoordinateFixed(IRefereeState state, Coordinate coordinate) {
        List<Integer> slidableRows = state.getBoard().getSlidableRows();
        List<Integer> slidableCols = state.getBoard().getSlidableColumns();
        return !slidableRows.contains(coordinate.getRow()) && !slidableCols.contains(coordinate.getCol());
    }

    /**
     * Checks if the coordinates row and column index are even.
     */
    public static boolean isCoordinateFixed(Coordinate coordinate) {
        return coordinate.getCol() % 2 == 1 && coordinate.getRow() % 2 == 1;
    }


    /**
     * Termination of a game is one of: - a player reaches its home after visiting its designated goal
     * tile - all players that survive a round opt to pass - the referee has run 1000 rounds - No
     * players are left in the game (kicked)
     */
    public static boolean gameOver(IRefereeState state, int passes, int numberOfRounds) {
        return numberOfRounds >= ROUND_LIMIT || roundOver(state, passes);
    }

    /**
     * A Round is over if the following:
     * - a player reaches its home after visiting its designated goal
     * - Player List is empty after kicking out the last list.
     * - All active players have opted to pass.
     */
    public static boolean roundOver(IRefereeState state, int passes) {
        return state.getListOfPlayers().isEmpty() ||
                passes >= state.getListOfPlayers().size() ||
                state.getCurrentPlayerData().isTerminateCondition();
    }

    /**
     * Returns whether a turn is valid. A turn is valid if it is:
     * Empty
     * A valid action
     */
    public static boolean validTurn(IRefereeState playerState, Optional<Action> turnToValidate) {
        if (turnToValidate.isEmpty()) {
            return true;
        }
        return validAction(playerState, turnToValidate.get());
    }

    /**
     * A valid action must have:
     * - valid slide
     * - valid rotation
     * - valid coordinate to move
     * - Coordinate exist on the board.
     * - Reachable coordinate from the current position.
     */
    public static boolean validAction(IPlayerState playerState, Action actionToValidate) {
        SlideAction slide = actionToValidate.getSlideAction();
        int rotation = actionToValidate.getRotation();
        Coordinate toMove = actionToValidate.getToMove();

        boolean validRotation = validRotation(rotation);
        boolean validSlideIndex = validSlide(playerState, slide);
        boolean validBoardCoordinate = validBoardCoordinate(playerState, toMove);

        if (validRotation && validSlideIndex && validBoardCoordinate) {
            playerState = playerState.rotateSpare(rotation).insertSpare(slide);

            return validToMove(playerState, toMove);
        }

        return false;
    }

    /**
     * A rotation is valid if it is a multiple of 90, including negative integers
     */
    private static boolean validRotation(int rotation) {
        return rotation % 90 == 0;
    }

    /**
     * A slide is valid if it slides a movable column or row and would not reverse the last slide.
     */
    private static boolean validSlide(IPlayerState playerState, SlideAction slideToValidate) {
        boolean wouldNotReverse = !playerState.getPreviousSlide().equals(slideToValidate.getReverseSlide());
        boolean validSlideDimension = playerState.validSlideDimension(slideToValidate);

        return wouldNotReverse && validSlideDimension;
    }

    /**
     * A coordinate is a valid board coordinate if it exists within the board
     */
    private static boolean validBoardCoordinate(IPlayerState playerState, Coordinate toMove) {
        return playerState.getBoard().validLocation(toMove);
    }

    /**
     * A toMove is valid if it is different from the current player's current position and they can reach it.
     */
    private static boolean validToMove(IPlayerState playerState, Coordinate toMove) {
        boolean notSameLocation = !playerState.getCurrentPlayerPosition().equals(toMove);
        boolean canReachLocation = playerState.getBoard().canReachTile(playerState.getCurrentPlayerPosition(), toMove);

        return notSameLocation && canReachLocation;
    }

    /**
     * Calculates the list of winning players. A winning player(s) is one that has the highest
     * tally of treasures accumulated. If there is a tie of treasures, the euclidean distance
     * to the next target tile.
     */
    public static List<IPlayer> getWinningCandidates(IRefereeState finalState) {

        List<PrivatePlayerData> mostTreasures = checkTreasureWinner(finalState);

        if (mostTreasures.isEmpty()) {
            return new ArrayList<>();
        } else if (mostTreasures.size() < 2) {
            IPlayer winningPlayer = mostTreasures.get(0).getPlayer().get();
            return new ArrayList<>(List.of(winningPlayer));

        } else {
            return getEuclideanWinners(mostTreasures);
        }
    }

    /**
     * Counts the treasures of every player in the final state given. Returns a list of players
     * with the highest tally of treasure accumulated.
     */
    private static List<PrivatePlayerData> checkTreasureWinner(IRefereeState finalState) {
        int mostTreasures = 0;
        List<PrivatePlayerData> players = new ArrayList<>();

        for (PrivatePlayerData playerData : finalState.getListOfPlayers()) {
            int currentTreasure = playerData.getTreasuresCount();
            if (currentTreasure > mostTreasures) {
                players = new ArrayList<>(List.of(playerData));
                mostTreasures = currentTreasure;
            } else if (currentTreasure == mostTreasures) {
                players.add(playerData);
            }
        }

        return players;
    }

    /**
     * Calculates winner(s) based on minimal euclidean distance to a players target tile.
     */
    private static List<IPlayer> getEuclideanWinners(List<PrivatePlayerData> treasureTieList) {
        List<Pair<IPlayer, Integer>> playersWithMaxTreasures = new ArrayList<>();

        for (PrivatePlayerData currentPlayerData : treasureTieList) {
            IPlayer currentPlayer = currentPlayerData.getPlayer().get();
            Coordinate currentPosition = currentPlayerData.getCurrentPosition();
            Coordinate target = currentPlayerData.getTargetTile();

            int euclideanDistance = Coordinate.getEuclideanDistanceSquare(currentPosition, target);
            playersWithMaxTreasures.add(new Pair(currentPlayer, euclideanDistance));
        }
        return calculateSmallestDistance(playersWithMaxTreasures);

    }

    /**
     * Returns the player(s) with the smallest distance to a target
     */
    private static List<IPlayer> calculateSmallestDistance(List<Pair<IPlayer, Integer>> listToSort) {
        listToSort.sort(new Comparator<Pair<IPlayer, Integer>>() {
            public int compare(Pair<IPlayer, Integer> firstObject, Pair<IPlayer, Integer> secondObject) {
                if (firstObject.getSecond() > secondObject.getSecond()) {
                    return 1;
                } else if (firstObject.getSecond() < secondObject.getSecond()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        List<IPlayer> winners = new ArrayList();
        winners.add(listToSort.get(0).getFirst());

        for (int i = 1; i < listToSort.size(); i++) {
            if (listToSort.get(i).getSecond() == listToSort.get(0).getSecond()) {
                winners.add(listToSort.get(i).getFirst());
            }
        }

        return winners;
    }
}