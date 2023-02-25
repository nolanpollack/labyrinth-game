package com.northeastern.labyrinth.json.Integration;

import com.google.gson.JsonArray;
import com.northeastern.labyrinth.Controller.Player.AIPlayer;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Observer.IObserver;
import com.northeastern.labyrinth.Observer.RefereeStateObserver;
import com.northeastern.labyrinth.Strategy.IStrategy;
import com.northeastern.labyrinth.Strategy.PlayerStrategyFactory;
import com.northeastern.labyrinth.json.Adapter.BadPlayerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * XBad reads two Json values (the first Json value represents List of {@link IPlayer} and the
 * second Json value represents a {@link RefereeState}), then runs the game and output the list of
 * winners and list of misbehaved players name sorted alphabetically in Json Array format.
 */
public class XBad extends AbstractXGame {

    /**
     * Main method of {@link XBad} that reads the Json value from Stdin and print the result to
     * Stdout. It will also set up a gui observer if {@code "gui".equals(args[0])}.
     */
    public static void main(String[] args) {

        try (Reader input = new InputStreamReader(System.in);
             Writer output = new OutputStreamWriter(System.out)) {
            XBad xBad = new XBad();

            List<IObserver> observers = new ArrayList<>();

            if (args.length >= 1 && "gui".equals(args[0])) {
                observers.add(new RefereeStateObserver());
            }

            xBad.xGame(input, output, observers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse the JsonArray to a single {@link IPlayer}.
     */
    protected IPlayer parsePlayer(JsonArray jsonPlayer) {
        String name = jsonPlayer.get(0).getAsString();
        String strategyName = jsonPlayer.get(1).getAsString();
        IStrategy strategy = PlayerStrategyFactory.getStrategyByName(strategyName);

        if (jsonPlayer.size() > 2) {
            String bad = jsonPlayer.get(2).getAsString();
            return BadPlayerFactory.createExceptionPlayer(name, strategy, bad);
        }

        return new AIPlayer(name, strategy);
    }
}
