package com.northeastern.labyrinth.Strategy;

import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a euclid strategy enumeration
 */
public class EuclidStrategy extends NaiveStrategy {

    /**
     * Returns a sorted list of candidates starting from a goal, then proceeds by smallest euclidean
     * distance to goal.
     */
    protected List<Coordinate> enumerateCandidates(Coordinate goalTile,
                                                   IPlayerState<PublicPlayerData> playerState) {
        List<Pair<Coordinate, Integer>> euclideanDistances = getEuclideanDistances(goalTile,
                playerState);
        List<Coordinate> enumeratedCandidates = sortEuclideanDistances(euclideanDistances);
        return enumeratedCandidates;
    }

    /**
     * Sorts the list of coordinates based on euclidean distances. Ties are broken by row column
     * order.
     */
    private List<Coordinate> sortEuclideanDistances(List<Pair<Coordinate, Integer>> list) {
        list.sort(new Comparator<Pair<Coordinate, Integer>>() {
            public int compare(Pair<Coordinate, Integer> firstObject,
                               Pair<Coordinate, Integer> secondObject) {
                if (firstObject.getSecond() > secondObject.getSecond()) {
                    return 1;
                } else if (firstObject.getSecond() < secondObject.getSecond()) {
                    return -1;
                } else {
                    return firstObject.getFirst().compareTo(secondObject.getFirst());
                }
            }
        });

        List<Coordinate> enumeratedCandidates = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            enumeratedCandidates.add(list.get(i).getFirst());
        }

        return enumeratedCandidates;
    }

    /**
     * Gathers the euclidean distance of every tile to goal tile
     */
    private List<Pair<Coordinate, Integer>> getEuclideanDistances(Coordinate goalTile,
                                                                  IPlayerState<PublicPlayerData> playerState) {

        int boardNumRows = playerState.getBoard().getNumRows();
        int boardNumCols = playerState.getBoard().getNumCols();

        List<Pair<Coordinate, Integer>> euclideanDistances = new ArrayList();

        for (int row = 0; row < boardNumRows; row++) {
            for (int col = 0; col < boardNumCols; col++) {
                int distance = Coordinate.getEuclideanDistanceSquare(goalTile, new Coordinate(row, col));
                Pair<Coordinate, Integer> distanceAtCoordinate = new Pair(new Coordinate(row, col),
                        distance);

                euclideanDistances.add(distanceAtCoordinate);
            }
        }

        return euclideanDistances;
    }

}