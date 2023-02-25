package com.northeastern.labyrinth.json.Adapter;

import com.google.gson.*;
import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Util.Gem;
import com.northeastern.labyrinth.Util.TileSymbolConverter;

import java.lang.reflect.Type;

/**
 * Custom Json Serializer and Deserializer for {@link MazeBoard}.
 */
public class BoardAdapter implements JsonSerializer<MazeBoard>, JsonDeserializer<MazeBoard> {

    @Override
    public MazeBoard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Tile[][] tiles = getTiles(json.getAsJsonObject());

        return new MazeBoard(tiles);
    }

    /**
     * Creates a board representation from the json value.
     */
    private static Tile[][] getTiles(JsonObject json) {

        JsonArray connectors = json.getAsJsonArray("connectors");
        JsonArray treasures = json.getAsJsonArray("treasures");

        int numRow = connectors.size();
        int numCol = connectors.get(0).getAsJsonArray().size();

        Tile[][] tiles = new Tile[numRow][numCol];

        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {

                char connector = connectors.get(i).getAsJsonArray().get(j).getAsString().charAt(0);
                String treasure1 = treasures.get(i).getAsJsonArray().get(j).getAsJsonArray().get(0)
                        .getAsString();
                String treasure2 = treasures.get(i).getAsJsonArray().get(j).getAsJsonArray().get(1)
                        .getAsString();

                tiles[i][j] = new Tile(connector, treasure1, treasure2);
            }
        }

        return tiles;
    }

    @Override
    public JsonElement serialize(MazeBoard board, Type typeOfSrc, JsonSerializationContext context) {
        Tile[][] tiles = board.copyBoard();
        JsonObject jsonBoard = new JsonObject();
        JsonElement connectorsJson = context.serialize(getBoardConnectors(tiles));
        JsonElement treasuresJson = context.serialize(getBoardGems(tiles));
        jsonBoard.add("connectors", connectorsJson);
        jsonBoard.add("treasures", treasuresJson);
        return jsonBoard;
    }

    /**
     * Get the connector symbol of the tiles in the board.
     *
     * @param board the maze board.
     * @return the connector symbol of the tiles in the board.
     */
    private Character[][] getBoardConnectors(Tile[][] board) {
        Character[][] connectors = new Character[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                connectors[i][j] = TileSymbolConverter.connectorsToSymbol(board[i][j].getConnections());
            }
        }

        return connectors;
    }

    /**
     * Get the name of gems in the tiles in the board.
     *
     * @param board the maze board.
     * @return the name of gems in the tiles in the board.
     */
    private String[][][] getBoardGems(Tile[][] board) {
        String[][][] treasures = new String[board.length][board[0].length][2];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Gem[] gems = board[i][j].getTreasure();
                treasures[i][j][0] = Gem.enumToString(gems[0]);
                treasures[i][j][1] = Gem.enumToString(gems[1]);
            }
        }

        return treasures;
    }
}
