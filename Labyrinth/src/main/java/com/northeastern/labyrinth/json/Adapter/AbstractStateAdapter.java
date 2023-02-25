package com.northeastern.labyrinth.json.Adapter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.GameState.IPlayerData;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * Implement the default Json Serializer and Deserializer for game state.
 *
 * @param <PD> Player Data Generic can be {@link PrivatePlayerData} or {@link PublicPlayerData}.
 * @param <GS> Game State Generic can be
 *             {@link com.northeastern.labyrinth.Model.GameState.RefereeState} or
 *             {@link com.northeastern.labyrinth.Model.GameState.PlayerState}.
 */
public abstract class AbstractStateAdapter<PD extends IPlayerData<PD>, GS extends IPlayerState<PD>> implements
        JsonSerializer<GS>, JsonDeserializer<GS> {

    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(MazeBoard.class, new BoardAdapter())
            .registerTypeAdapter(PrivatePlayerData.class, new PrivatePlayerAdapter())
            .registerTypeAdapter(PublicPlayerData.class, new PublicPlayerAdapter())
            .registerTypeAdapter(Tile.class, new TileAdapter())
            .serializeNulls()
            .create();

    // Java reflection
    private final ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
    // Represents PD.class, the class for the player data
    private final Class<PD> playerDataType = (Class<PD>) parameterizedType.getActualTypeArguments()[0];

    @Override
    public GS deserialize(JsonElement json, Type typeOfT,
                          JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonState = json.getAsJsonObject();

        JsonObject jsonBoard = jsonState.get("board").getAsJsonObject();
        JsonObject jsonSpareTile = jsonState.get("spare").getAsJsonObject();
        JsonArray jsonPlayerList = jsonState.get("plmt").getAsJsonArray();
        boolean hasNoPreviousAction = jsonState.get("last").isJsonNull();

        Queue<Coordinate> goalTiles = getGoalTiles(jsonState, context);

        MazeBoard board = gson.fromJson(jsonBoard, MazeBoard.class);
        Tile spareTile = gson.fromJson(jsonSpareTile, Tile.class);
        Type type = TypeToken.getParameterized(Queue.class, playerDataType).getType();
        Queue<PD> playerList = gson.fromJson(jsonPlayerList, type);

        if (hasNoPreviousAction) {
            return buildState(board, spareTile, playerList, new SlideAction(), goalTiles);
        } else {
            JsonArray jsonAction = jsonState.get("last").getAsJsonArray();
            JsonPrimitive jsonDirection = jsonAction.get(1).getAsJsonPrimitive();

            int slideIndex = jsonAction.get(0).getAsInt();
            Direction slideDirection = gson.fromJson(jsonDirection, Direction.class);

            return buildState(board, spareTile, playerList,
                    new SlideAction(slideIndex, slideDirection), goalTiles);
        }
    }

    /**
     * Build Game State based on the given input.
     *
     * @param board          the maze board.
     * @param spareTile      the spare tile.
     * @param listPlayerData the list of player data.
     * @param slideAction    the previous slide action.
     * @return the game state.
     */
    abstract GS buildState(MazeBoard board, Tile spareTile, Queue<PD> listPlayerData,
                           SlideAction slideAction, Queue<Coordinate> goals);

    @Override
    public JsonElement serialize(GS gameState, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonState = new JsonObject();

        MazeBoard board = gameState.getBoard();
        Tile spareTile = gameState.getSpareTile();
        List<PD> playerList = gameState.getListOfPlayers();
        SlideAction slideAction = gameState.getPreviousSlide();

        jsonState.add("board", gson.toJsonTree(board));
        jsonState.add("spare", gson.toJsonTree(spareTile));
        jsonState.add("plmt", gson.toJsonTree(playerList));
        if (slideAction.getIndex() == -1) {
            jsonState.add("last", null);
        } else {
            JsonArray previousAction = new JsonArray();
            previousAction.add(slideAction.getIndex());
            previousAction.add(gson.toJsonTree(slideAction.getDirection()));
            jsonState.add("last", previousAction);
        }

        return jsonState;
    }


    /**
     * Checks if the key "goals" exist in the json object. If so, adds coordinates to queue.
     *
     * @return queue of coordinates, empty or not.
     */
    private static Queue<Coordinate> getGoalTiles(JsonObject jsonObject, JsonDeserializationContext context) {
        Queue<Coordinate> goalTiles = new ArrayDeque<>();
        if (jsonObject.has("goals")) {
            JsonArray jsonArray = jsonObject.get("goals").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                Coordinate coordinate = context.deserialize(jsonArray.get(i), Coordinate.class);
                goalTiles.add(coordinate);
            }
        }
        return goalTiles;
    }

}
