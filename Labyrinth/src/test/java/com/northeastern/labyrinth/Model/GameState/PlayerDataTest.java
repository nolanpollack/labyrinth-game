package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Util.Coordinate;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class PlayerDataTest {

    PrivatePlayerData pd;

    /*
    _ _ g _
    _ _ _ p
    _ _ _ _
    _ h _ _
    */

    private void init() {
        pd = new PrivatePlayerData(new Color(255),
                new Coordinate(0, 0),
                new Coordinate(1, 1),
                new Coordinate(1, 1),
                false, 0);
    }

    @Test
    public void SET_CURRENT_TILE() {
        init();
        Coordinate expectedCoordinate = new Coordinate(2, 2);

        pd.setCurrentPosition(expectedCoordinate);
        assertEquals(pd.getCurrentPosition(), new Coordinate(0, 0));

        pd = pd.setCurrentPosition(expectedCoordinate);
        assertEquals(pd.getCurrentPosition(), new Coordinate(2, 2));
    }

    @Test
    public void GET_CURRENT_POSITION() {
        init();
        Coordinate currentCoordinate = pd.getCurrentPosition();
        assertEquals(new Coordinate(0, 0), currentCoordinate);
    }

    @Test
    public void GET_HOME_COORDINATE() {
        init();
        Coordinate homeCoordinate = pd.getHomePosition();
        assertEquals(new Coordinate(1, 1), homeCoordinate);
    }

    @Test
    public void TREASURE_INCREMENT() {
        init();
        assertEquals(0, pd.getTreasuresCount());
        pd = pd.incrementTreasure();
        assertEquals(1, pd.getTreasuresCount());
    }

}
