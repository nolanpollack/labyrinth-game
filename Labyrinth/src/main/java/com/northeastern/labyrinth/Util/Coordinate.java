package com.northeastern.labyrinth.Util;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Represents a comparable, immutable position of a tile on the board with a row and a column
 * row = 0, column = 0 would be located in the top left corner of the board
 */
public class Coordinate implements Comparable<Coordinate> {
    //Row on board
    @SerializedName("row#")
    private final int row;
    //Column on board
    @SerializedName("column#")
    private final int col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * returns deep copy of board location
     */
    public Coordinate createCopy() {
        return new Coordinate(this.row, this.col);
    }

    /**
     * returns if given object is a BoardLocation with equal row and col values
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinate that = (Coordinate) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * returns 0 if locations are equal
     * returns < 0 if this location row is less than given location
     * returns > 0 if this location row is greater than given location
     * If row is the same, compares column instead
     */
    @Override
    public int compareTo(Coordinate testLocation) {
        int rowDif = row - testLocation.getRow();
        if (rowDif == 0) {
            int colDif = col - testLocation.getCol();

            return colDif;
        }

        return rowDif;
    }

    public static int getEuclideanDistanceSquare(Coordinate coordinate1, Coordinate coordinate2) {
        int rowDiff = coordinate1.getRow() - coordinate2.getRow();
        int colDiff = coordinate1.getCol() - coordinate2.getCol();

        return rowDiff * rowDiff + colDiff * colDiff;
    }
}
