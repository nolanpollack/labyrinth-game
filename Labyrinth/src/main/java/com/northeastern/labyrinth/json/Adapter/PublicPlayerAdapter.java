package com.northeastern.labyrinth.json.Adapter;

import com.google.gson.*;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.ColorHandler;
import com.northeastern.labyrinth.Util.Coordinate;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * Custom Json Serializer and Deserializer for {@link PublicPlayerData}.
 */
public class PublicPlayerAdapter implements JsonSerializer<PublicPlayerData>,
        JsonDeserializer<PublicPlayerData> {

    @Override
    public PublicPlayerData deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonPlayerData = json.getAsJsonObject();

        JsonObject jsonPlayerHome = jsonPlayerData.get("home").getAsJsonObject();
        JsonObject jsonPlayerCurrentPosition = jsonPlayerData.get("current").getAsJsonObject();
        String jsonColor = jsonPlayerData.get("color").getAsString();

        Coordinate playerHome = context.deserialize(jsonPlayerHome, Coordinate.class);
        Coordinate playerCurrentPosition = context.deserialize(jsonPlayerCurrentPosition,
                Coordinate.class);
        Color playerColor = ColorHandler.stringToColor(jsonColor);

        return new PublicPlayerData(playerColor, playerCurrentPosition, playerHome);
    }

    @Override
    public JsonElement serialize(PublicPlayerData playerData, Type typeOfSrc,
                                 JsonSerializationContext context) {
        Color playerColor = playerData.getAvatarColor();
        String hexColor = String.format("%06X", 0xFFFFFF & playerColor.getRGB());
        Coordinate playerCurrentPosition = playerData.getCurrentPosition();
        Coordinate playerHome = playerData.getHomePosition();

        JsonObject jsonPlayerData = new JsonObject();
        jsonPlayerData.addProperty("color", hexColor);
        jsonPlayerData.add("current", context.serialize(playerCurrentPosition));
        jsonPlayerData.add("home", context.serialize(playerHome));
        return jsonPlayerData;
    }
}
