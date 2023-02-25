package com.northeastern.labyrinth.json.Adapter;

import com.google.gson.*;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Gem;
import com.northeastern.labyrinth.Util.TileSymbolConverter;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Custom Json Serializer and Deserializer for {@link Tile}.
 */
public class TileAdapter implements JsonSerializer<Tile>, JsonDeserializer<Tile> {

    @Override
    public Tile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonTile = json.getAsJsonObject();
        String jsonTileConnections = jsonTile.get("tilekey").getAsString();
        String jsonGem1 = jsonTile.get("1-image").getAsString();
        String jsonGem2 = jsonTile.get("2-image").getAsString();

        return new Tile(jsonTileConnections.charAt(0), jsonGem1, jsonGem2);
    }

    @Override
    public JsonElement serialize(Tile tile, Type typeOfSrc, JsonSerializationContext context) {

        Map<Direction, Boolean> connectors = tile.getConnections();
        char tilekey = TileSymbolConverter.connectorsToSymbol(connectors);

        Gem[] gems = tile.getTreasure();
        String gem1 = Gem.enumToString(gems[0]);
        String gem2 = Gem.enumToString(gems[1]);

        JsonObject jsonTile = new JsonObject();
        jsonTile.addProperty("tilekey", tilekey);
        jsonTile.addProperty("1-image", gem1);
        jsonTile.addProperty("2-image", gem2);

        return jsonTile;
    }
}
