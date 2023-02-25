package com.northeastern.labyrinth.Strategy;

import com.northeastern.labyrinth.Controller.Rulebook;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;
import java.util.List;
import java.util.Optional;

/**
 * Represents a Naive strategy that checks all slide combinations for a given list of candidates
 */
public abstract class NaiveStrategy implements IStrategy {

    private final static int[] degrees = new int[]{0, 90, 180, 270};
    private final static Direction[] rowDirections = new Direction[]{Direction.LEFT, Direction.RIGHT};
    private final static Direction[] colDirections = new Direction[]{Direction.UP, Direction.DOWN};

    /**
     * Returns a list of enumerated candidates as determined by a specific strategy
     */
    protected abstract List<Coordinate> enumerateCandidates(Coordinate candidate,
                                                            IPlayerState<PublicPlayerData> playerState);

    @Override
    public Optional<Action> getMove(Coordinate goalTile, IPlayerState<PublicPlayerData> playerState) {
        List<Coordinate> candidates = enumerateCandidates(goalTile, playerState);
        return traverseAllCandidates(playerState, candidates);
    }

    /**
     * Attempt to find a valid move for the player to reach a candidate tile in the order given.
     * @param candidates the list of candidates to try.
     * @return first valid action found, or an empty optional if no valid action is found.
     */
    private Optional<Action> traverseAllCandidates(IPlayerState<PublicPlayerData> playerState,
                                                   List<Coordinate> candidates) {
        for (Coordinate candidate : candidates) {
            Optional<Action> action = getAction(playerState, candidate);
            if (action.isPresent()) {
                return action;
            }
        }

        return Optional.empty();
    }

    /**
     * Get the {@link Action} if the current player can reach the given position. If the current
     * player cannot reach the given position then returns {@code Optional.empty()}.
     *
     * @param playerState the current game state.
     * @param candidate   the position that current player wants to reach.
     * @return the {@link Action} if the current player can reach the given position, otherwise
     * {@code Optional.empty()}.
     */
    private Optional<Action> getAction(IPlayerState<PublicPlayerData> playerState,
                                       Coordinate candidate) {

        List<Integer> slidableRows = playerState.getBoard().getSlidableRows();
        Optional<Action> action = traverseAllActionCombinations(playerState, candidate, slidableRows,
                rowDirections, degrees);

        if (action.isPresent()) {
            return action;
        }

        List<Integer> slidableCols = playerState.getBoard().getSlidableColumns();
        action = traverseAllActionCombinations(playerState, candidate, slidableCols, colDirections,
                degrees);

        return action;
    }

    /**
     * Traverse all action combinations based on the given list of slidable indices, directions and
     * degrees and checks if any of the action combinations can make the current player reach the
     * given position. If there is no such action then return {@code Optional.empty()}
     */
    private Optional<Action> traverseAllActionCombinations(IPlayerState<PublicPlayerData> playerState,
                                                           Coordinate candidate, List<Integer> slidableIndices, Direction[] directions, int[] degrees) {

        for (int curIndex : slidableIndices) {
            for (Direction curDirection : directions) {
                SlideAction slideAction = new SlideAction(curIndex, curDirection);
                for (int curDegree : degrees) {
                    Action action = new Action(slideAction, curDegree, candidate);
                    if (Rulebook.validAction(playerState, action)) {
                        return Optional.of(new Action(slideAction, curDegree, candidate));
                    }
                }
            }
        }
        return Optional.empty();
    }
}