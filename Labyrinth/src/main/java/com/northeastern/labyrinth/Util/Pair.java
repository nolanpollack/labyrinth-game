package com.northeastern.labyrinth.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an immutable generic pair of two objects
 */
public class Pair<T, V> {
    private final T first;
    private final V second;

    public Pair(T first, V second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }


    @Override
    public final int hashCode() {
        return 13 * first.hashCode() + second.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair that = (Pair) o;
        return first == that.first && second == that.second;
    }

    /**
     * Takes in a list of pairs and returns an arraylist of only the first item.
     */
    public static <T, V> List<T> getListOfFirst(List<Pair<T, V>> list) {
        List<T> returnFirstList = new ArrayList();
        for (Pair<T, V> item : list) {
            returnFirstList.add(item.getFirst());
        }
        return returnFirstList;
    }

    /**
     * Takes in a list of pairs and returns an arraylist of only the second item.
     */
    public static <T, V> List<V> getListOfSecond(List<Pair<T, V>> list) {
        List<V> returnSecondList = new ArrayList();
        for (Pair<T, V> item : list) {
            returnSecondList.add(item.getSecond());
        }
        return returnSecondList;
    }
}
