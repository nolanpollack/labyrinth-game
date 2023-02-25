package com.northeastern.labyrinth.Server;

import com.google.gson.JsonArray;
import com.google.gson.JsonStreamParser;
import com.northeastern.labyrinth.Controller.Player.AIPlayer;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Strategy.IStrategy;
import com.northeastern.labyrinth.Strategy.PlayerStrategyFactory;
import com.northeastern.labyrinth.json.Adapter.BadPlayerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class XClients {

    private static int port;
    private static String host;
    private static int DELAY_CLIENTS = 3;

    public static void main(String[] args) {

        try (Reader input = new InputStreamReader(System.in)) {

            if (args.length == 0) {
                throw new RuntimeException("Missing port number argument");
            }

            port = Integer.parseInt(args[0]);
            host = args.length > 1 ? args[1] : "127.0.0.1";

            List<IPlayer> players = parseInput(input);
            runClients(players);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static void runClients(List<IPlayer> playerList) throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<>();
        for (IPlayer player : playerList) {
            Thread clientThread = new Thread() {
                public void run() {
                    Client client = new Client(host, port);
                    client.connectAndExecute(player);
                }
            };

            clientThread.start();

            threads.add(clientThread);
            Thread.sleep(DELAY_CLIENTS);
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    /**
     * Parse the input to a list of {@link IPlayer} and a {@link RefereeState}.
     */
    private static List<IPlayer> parseInput(Reader reader) {
        JsonStreamParser parser = new JsonStreamParser(reader);
        JsonArray jsonPlayerList = parser.next().getAsJsonArray();
        return parsePlayerList(jsonPlayerList);
    }

    /**
     * Parse the JsonArray to a list of {@link IPlayer}.
     */
    private static List<IPlayer> parsePlayerList(JsonArray jsonPlayerList) {
        List<IPlayer> playerList = new ArrayList<>();

        for (int i = 0; i < jsonPlayerList.size(); i++) {
            JsonArray jsonPlayer = jsonPlayerList.get(i).getAsJsonArray();
            IPlayer player = parsePlayer(jsonPlayer);
            playerList.add(player);
        }

        return playerList;
    }

    private static IPlayer parsePlayer(JsonArray jsonPlayer) {
        String name = jsonPlayer.get(0).getAsString();
        String strategyName = jsonPlayer.get(1).getAsString();
        IStrategy strategy = PlayerStrategyFactory.getStrategyByName(strategyName);

        if (jsonPlayer.size() > 2) {
            String bad = jsonPlayer.get(2).getAsString();

            if (jsonPlayer.size() > 3) {
                int count = jsonPlayer.get(3).getAsInt();
                return BadPlayerFactory.createLoopPlayer(name, strategy, bad, count);
            }

            return BadPlayerFactory.createExceptionPlayer(name, strategy, bad);
        }

        return new AIPlayer(name, strategy);
    }
}
