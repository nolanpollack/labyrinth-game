package com.northeastern.labyrinth.Strategy;

import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a riemann strategy enumeration
 */
public class RiemannStrategy extends NaiveStrategy {

    /**
     * Returns a sorted list of candidates starting from a goal, then proceeds in row-column order.
     */
    @Override
    protected List<Coordinate> enumerateCandidates(Coordinate goalTile,
                                                   IPlayerState<PublicPlayerData> playerState) {
        int boardNumRows = playerState.getBoard().getNumRows();
        int boardNumCols = playerState.getBoard().getNumCols();

        List<Coordinate> enumeratedCandidates = new ArrayList();

        for (int i = 0; i < boardNumRows; i++) {
            for (int j = 0; j < boardNumCols; j++) {
                Coordinate candidateToAdd = new Coordinate(i, j);
                if (candidateToAdd.equals(goalTile)) {
                    enumeratedCandidates.add(0, candidateToAdd);
                } else {
                    enumeratedCandidates.add(candidateToAdd);
                }
            }
        }

        return enumeratedCandidates;
    }
}
