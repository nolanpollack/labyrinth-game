package com.northeastern.labyrinth.Remote;

import com.google.gson.*;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.PlayerState;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.json.Adapter.ActionAdapter;
import com.northeastern.labyrinth.json.Adapter.PlayerStateAdapter;

import java.io.*;
import java.util.Optional;

/**
 * Represents a proxy referee that handles the incoming method call in JSON format from the socket
 * input stream and calls the corresponding IPlayer method.
 */
public class ProxyReferee {

    //MName protocols
    private final static String SET_UP_MNAME = "setup";
    private final static String TAKE_TURN_MNAME = "take-turn";
    private final static String WIN_MNAME = "win";

    // Json parser
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(PlayerState.class, new PlayerStateAdapter())
            .registerTypeAdapter(Action.class, new ActionAdapter()).serializeNulls()
            .create();

    private final JsonStreamParser input;
    private final Writer output;

    private final IPlayer player;

    /**
     * Create a ProxyReferee based on the given input stream, output stream and player. Send the
     * player's name to output stream.
     */
    public ProxyReferee(InputStream inputStream, OutputStream outputStream, IPlayer player)
            throws IOException {
        this.player = player;
        this.input = new JsonStreamParser(new BufferedReader(new InputStreamReader(inputStream)));
        this.output = new BufferedWriter(new OutputStreamWriter(outputStream));

        sendInfoToServer(new JsonPrimitive(this.player.name()).toString());
    }

    /**
     * Runs the Proxy Referee game loop.
     */
    public void runGame() {
        try {
            dispatchRefereeCall();
        } catch (Exception e) {
        }
    }

    /**
     * Receives JSON array that represents the method call from the input stream. Separates the method
     * name from the arguments and call {@link ProxyReferee#dispatch(String, JsonArray)}.
     */
    private void dispatchRefereeCall() throws IOException {
        while (this.input.hasNext()) {
            JsonArray currentCall = this.input.next().getAsJsonArray();

            String mName = currentCall.get(0).getAsString();
            JsonArray arguments = currentCall.get(1).getAsJsonArray();

            dispatch(mName, arguments);
        }
    }


    /**
     * Calls the appropriate deserialize method for the MName message key. Writes the message response
     * through output stream.
     *
     * @param mName     Method Key
     * @param arguments Payload of method.
     */
    private void dispatch(String mName, JsonArray arguments) throws IOException {
        JsonElement playerResponse;

        switch (mName) {
            case SET_UP_MNAME:
                playerResponse = callSetupPlayer(arguments);
                break;
            case TAKE_TURN_MNAME:
                playerResponse = callPlayerTakeTurn(arguments);
                break;
            case WIN_MNAME:
                playerResponse = callPlayerWon(arguments);
                break;
            default:
                throw new IllegalArgumentException("Not Following the Protocol");
        }

        sendInfoToServer(playerResponse.toString());
    }

    /**
     * Deserializes arguments of Setup message and calls the method on player. Serializes the
     * response.
     *
     * @param arguments the arguments of the {@link IPlayer#setup(Optional, Coordinate)} method in
     *                  JSON format.
     * @return the JSON "void".
     */
    private JsonElement callSetupPlayer(JsonArray arguments) {
        Optional<IPlayerState> state0;

        if (arguments.get(0).isJsonPrimitive() && !arguments.get(0).getAsBoolean()) {
            state0 = Optional.empty();
        } else {
            state0 = Optional.of(gson.fromJson(arguments.get(0), PlayerState.class));
        }

        Coordinate target = gson.fromJson(arguments.get(1), Coordinate.class);
        this.player.setup(state0, target);

        return new JsonPrimitive("void");
    }

    /**
     * Deserializes arguments of Take Turn message and calls the method on player. Serializes the
     * response.
     *
     * @param arguments the arguments of the {@link IPlayer#takeTurn(IPlayerState)} method in JSON
     *                  format.
     * @return the {@link Action} returned by {@link IPlayer#takeTurn(IPlayerState)} in JSON format,
     * or JSON "PASS" if it is {@code Optional.empty()}.
     */
    private JsonElement callPlayerTakeTurn(JsonArray arguments) {
        IPlayerState state = gson.fromJson(arguments.get(0), PlayerState.class);

        Optional<Action> choice = this.player.takeTurn(state);

        if (choice.isPresent()) {
            return gson.toJsonTree(choice.get());
        } else {
            return new JsonPrimitive("PASS");
        }
    }

    /**
     * Deserializes arguments of won message and calls the method on player. Serializes the response.
     *
     * @param arguments the arguments of the {@link IPlayer#win(Boolean)} method in JSON format.
     * @return the JSON "void".
     */
    private JsonElement callPlayerWon(JsonArray arguments) {
        boolean won = arguments.get(0).getAsBoolean();
        this.player.win(won);
        return new JsonPrimitive("void");
    }

    /**
     * Writes the given info to the output stream and flush the stream.
     *
     * @param info the information to be written.
     * @throws IOException if an I/O error occurs.
     */
    private void sendInfoToServer(String info) throws IOException {
        this.output.write(info);
        this.output.flush();
    }
}
