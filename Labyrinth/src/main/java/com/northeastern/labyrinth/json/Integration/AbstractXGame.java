package com.northeastern.labyrinth.json.Integration;

import com.google.gson.*;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Controller.Referee.IReferee;
import com.northeastern.labyrinth.Controller.Referee.ObservableReferee;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Observer.IObserver;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.json.Adapter.RefereeStateAdapter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractXGame {

    // Json Parser
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(RefereeState.class, new RefereeStateAdapter())
            .create();
    private IReferee referee;
    private List<IPlayer> playerList;
    private IRefereeState refereeState;

    /**
     * Parse the two Json values from input to a list of {@link IPlayer} and a {@link RefereeState}.
     * Then create a referee and runs the game. Output the list of winners and list of misbehaved
     * players name sorted alphabetically in Json format.
     *
     * @param reader    contains two Json values. The first one represents a list of {@link IPlayer}.
     *                  The second on represents a {@link RefereeState}.
     * @param writer    the list of winners and list of misbehaved players name sorted alphabetically
     *                  in Json Array format.
     * @param observers the observer that used to observe the game.
     */
    public void xGame(Reader reader, Writer writer, List<IObserver> observers) {
        try {
            parseInput(reader);
            Pair<List<IPlayer>, List<IPlayer>> gameResult = runGame(observers);
            parseOutput(writer, gameResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse the input to a list of {@link IPlayer} and a {@link RefereeState}.
     */
    private void parseInput(Reader reader) {
        JsonStreamParser parser = new JsonStreamParser(reader);

        JsonArray jsonPlayerList = parser.next().getAsJsonArray();
        this.playerList = parsePlayerList(jsonPlayerList);

        JsonElement jsonRefereeState = parser.next();
        this.refereeState = gson.fromJson(jsonRefereeState, RefereeState.class);
    }

    /**
     * Runs the game based on the parsed list of {@link IPlayer} and a {@link RefereeState}.
     *
     * @param observers the observer that used to observer the game.
     * @return the game result where the first element in pair is the list of winners and the second
     * element in pair is the list of misbehaved players.
     */
    private Pair<List<IPlayer>, List<IPlayer>> runGame(List<IObserver> observers) {
        this.referee = new ObservableReferee();
        for (IObserver observer : observers) {
            ((ObservableReferee) this.referee).addObserver(observer);
        }
        Pair<List<IPlayer>, List<IPlayer>> gameResult = this.referee.runGame(refereeState,
                playerList);
        return gameResult;
    }

    /**
     * Parse the JsonArray to a list of {@link IPlayer}.
     */
    private List<IPlayer> parsePlayerList(JsonArray jsonPlayerList) {
        List<IPlayer> playerList = new ArrayList<>();

        for (int i = 0; i < jsonPlayerList.size(); i++) {
            JsonArray jsonPlayer = jsonPlayerList.get(i).getAsJsonArray();
            IPlayer player = parsePlayer(jsonPlayer);
            playerList.add(player);
        }

        return playerList;
    }

    /**
     * Parse the JsonArray to a single {@link IPlayer}.
     */
    protected abstract IPlayer parsePlayer(JsonArray jsonPlayer);

    /**
     * Parse the gameResult {@code Pair<List<IPlayer>, List<IPlayer>>} (the first list is list of
     * winners and the second list is list of misbehaved players) to Json Array format and write to
     * the given writer.
     */
    private void parseOutput(Writer writer, Pair<List<IPlayer>, List<IPlayer>> gameResult)
            throws IOException {
        List<List<String>> output = new ArrayList<>();

        List<String> winnerNames = gameResult.getFirst().stream().map(IPlayer::name).sorted()
                .collect(Collectors.toList());
        List<String> misbehavedNames = gameResult.getSecond().stream().map(IPlayer::name).sorted()
                .collect(Collectors.toList());

        output.add(winnerNames);
        output.add(misbehavedNames);

        writer.write(gson.toJson(output));
        writer.flush();
    }
}
