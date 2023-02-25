package com.northeastern.labyrinth.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Controller.Referee.Referee;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.json.Adapter.RefereeStateAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class XServer {

    // Json Parser
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(RefereeState.class, new RefereeStateAdapter())
            .create();


    public static void main(String[] args) {

        try (Reader input = new InputStreamReader(System.in);
             Writer output = new OutputStreamWriter(System.out)) {

            if (args.length == 0) {
                throw new RuntimeException("Missing port number argument");
            }

            int port = Integer.parseInt(args[0]);
            Server server = new Server(port);

            Optional<IRefereeState> state = Optional.empty();
            if (args.length > 1 && "test".equals(args[1])) {
                state = Optional.of(parseInput(input));
            }
            Pair<List<IPlayer>, List<IPlayer>> outcome = server.runServer(new Referee(), state);

            parseOutput(output, outcome);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse the input to a list of {@link IPlayer} and a {@link RefereeState}.
     */
    private static IRefereeState parseInput(Reader reader) {
        JsonStreamParser parser = new JsonStreamParser(reader);

        JsonElement jsonRefereeState = parser.next();
        return gson.fromJson(jsonRefereeState, RefereeState.class);
    }

    /**
     * Parse the gameResult {@code Pair<List<IPlayer>, List<IPlayer>>} (the first list is list of
     * winners and the second list is list of misbehaved players) to Json Array format and write to
     * the given writer.
     */
    private static void parseOutput(Writer writer, Pair<List<IPlayer>, List<IPlayer>> gameResult)
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
