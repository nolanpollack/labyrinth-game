package com.northeastern.labyrinth.Remote;

import com.google.gson.*;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.PlayerState;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.json.Adapter.ActionAdapter;
import com.northeastern.labyrinth.json.Adapter.PlayerStateAdapter;

import java.io.*;
import java.util.Optional;

/**
 * Represents a proxy player who is connected remotely for the referee to use. A proxy has its own
 * socket communication for input and output duty. Adheres to the logical protocol found in REMOTE
 * INTERACTIONS.
 */
public class ProxyPlayer extends ProxyReferee implements IPlayer {

    //MName protocols
    private final static String SET_UP_MNAME = "setup";
    private final static String TAKE_TURN_MNAME = "take-turn";
    private final static String WIN_MNAME = "win";

    // Json parser
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(PlayerState.class, new PlayerStateAdapter())
            .registerTypeAdapter(Action.class, new ActionAdapter()).serializeNulls().create();

    private final JsonStreamParser parser;
    private final Writer output;

    // Player name
    private final String name;

    /**
     * Create a ProxyPlayer based on the given input stream and output stream.
     */
    public ProxyPlayer(InputStream inputStream, OutputStream outputStream) {
        this.parser = new JsonStreamParser(
                new BufferedReader(new InputStreamReader(inputStream)));
        this.output = new BufferedWriter(new OutputStreamWriter(outputStream));

        this.name = this.parser.next().getAsString();
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public MazeBoard proposeBoard0(int rows, int columns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setup(Optional<IPlayerState> state, Coordinate targetTile) {
        JsonArray call = new JsonArray();
        call.add(SET_UP_MNAME);

        JsonArray arguments = new JsonArray();
        if (state.isPresent()) {
            arguments.add(gson.toJsonTree(state.get()));
        } else {
            arguments.add(false);
        }

        arguments.add(gson.toJsonTree(targetTile));
        call.add(arguments);
        sendCall(call);
        // Should expect a response "void".
        String response = this.parser.next().getAsString();
        if (!"void".equals(response)) {
            throw new IllegalArgumentException("Invalid Response Type");
        }
    }

    @Override
    public Optional<Action> takeTurn(IPlayerState<PublicPlayerData> publicState) {
        JsonArray call = new JsonArray();
        call.add(TAKE_TURN_MNAME);

        JsonArray arguments = new JsonArray();
        arguments.add(gson.toJsonTree(publicState));
        call.add(arguments);

        sendCall(call);

        JsonElement responseAction = this.parser.next();

        if (responseAction.isJsonPrimitive() && "PASS".equals(responseAction.getAsString())) {
            return Optional.empty();
        } else {
            return Optional.of(gson.fromJson(responseAction, Action.class));
        }
    }

    @Override
    public void win(Boolean w) {
        JsonArray call = new JsonArray();
        call.add(WIN_MNAME);

        JsonArray arguments = new JsonArray();
        arguments.add(w);
        call.add(arguments);

        sendCall(call);
        String response = this.parser.next().getAsString();

        if (!"void".equals(response)) {
            throw new IllegalArgumentException("Invalid Response Type");
        }
    }

    /**
     * Sends the method call in Json format to the output stream.
     */
    private void sendCall(JsonElement call) {
        try {
            this.output.write(gson.toJson(call));
            this.output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
