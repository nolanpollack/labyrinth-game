package com.northeastern.labyrinth.Util;

import com.northeastern.labyrinth.Controller.Player.AIPlayer;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Controller.Rulebook;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Strategy.EuclidStrategy;
import com.northeastern.labyrinth.Strategy.RiemannStrategy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GenerateTest extends Generate {
    Generate gTest = new Generate(1);

    @Test
    void generateRefereeState() {
        for (int seed = 0; seed < 100; seed++) {
            gTest = new Generate(seed);

            List<IPlayer> players = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                players.add(new AIPlayer(("Riemann " + i), new RiemannStrategy()));
                players.add(new AIPlayer(("Euclid " + i), new EuclidStrategy()));
            }

            RefereeState testState = gTest.generateRefereeState(10, players);

            assertEquals(6, testState.getListOfPlayers().size());

            Set<Coordinate> distinctHomes = new HashSet<>();

            for (PrivatePlayerData player : testState.getListOfPlayers()) {
                Coordinate playerHome = player.getHomePosition();

                assertFalse(distinctHomes.contains(playerHome));
                distinctHomes.add(playerHome);
                assertTrue(Rulebook.isCoordinateFixed(playerHome));
            }

            Set<Gem[]> gemPairs = new HashSet<>();
//            for (Tile tile: testState.getPlayerState().getBoard()) {
//
//            }

            assertEquals(6, distinctHomes.size());
        }
    }
}