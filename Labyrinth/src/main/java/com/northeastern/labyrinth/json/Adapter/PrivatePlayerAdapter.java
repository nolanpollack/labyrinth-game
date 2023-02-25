package com.northeastern.labyrinth.json.Adapter;

import com.google.gson.*;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Util.ColorHandler;
import com.northeastern.labyrinth.Util.Coordinate;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * Custom Json Serializer and Deserializer for {@link PrivatePlayerData}.
 */
public class PrivatePlayerAdapter implements JsonSerializer<PrivatePlayerData>,
        JsonDeserializer<PrivatePlayerData> {

    @Override
    public PrivatePlayerData deserialize(JsonElement json, Type typeOfT,
                                         JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonPlayerData = json.getAsJsonObject();

        JsonObject jsonPlayerHome = jsonPlayerData.get("home").getAsJsonObject();
        JsonObject jsonPlayerCurrentPosition = jsonPlayerData.get("current").getAsJsonObject();
        JsonObject jsonPlayerGoal = jsonPlayerData.get("goto").getAsJsonObject();
        String jsonColor = jsonPlayerData.get("color").getAsString();

        Coordinate playerHome = context.deserialize(jsonPlayerHome, Coordinate.class);
        Coordinate playerCurrentPosition = context.deserialize(jsonPlayerCurrentPosition,
                Coordinate.class);
        Coordinate playerGoal = context.deserialize(jsonPlayerGoal, Coordinate.class);
        Color playerColor = ColorHandler.stringToColor(jsonColor);

        return new PrivatePlayerData(playerColor, playerCurrentPosition, playerHome, playerGoal, false, 0);
    }

    @Override
    public JsonElement serialize(PrivatePlayerData playerData, Type typeOfSrc,
                                 JsonSerializationContext context) {
        Color playerColor = playerData.getAvatarColor();
        String hexColor = String.format("%06X", 0xFFFFFF & playerColor.getRGB());
        Coordinate playerCurrentPosition = playerData.getCurrentPosition();
        Coordinate playerHome = playerData.getHomePosition();
        Coordinate playerGoal = playerData.getTargetTile();

        JsonObject jsonPlayerData = new JsonObject();
        jsonPlayerData.addProperty("color", hexColor);
        jsonPlayerData.add("current", context.serialize(playerCurrentPosition));
        jsonPlayerData.add("home", context.serialize(playerHome));
        jsonPlayerData.add("goto", context.serialize(playerGoal));

        return jsonPlayerData;
    }
}
