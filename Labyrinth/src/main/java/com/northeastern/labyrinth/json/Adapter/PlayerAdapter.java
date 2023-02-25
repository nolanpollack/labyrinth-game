package com.northeastern.labyrinth.json.Adapter;

import com.google.gson.*;
import com.northeastern.labyrinth.Controller.Player.AIPlayer;
import com.northeastern.labyrinth.Strategy.IStrategy;
import com.northeastern.labyrinth.Strategy.PlayerStrategyFactory;

import java.lang.reflect.Type;

/**
 * Custom Json Serializer and Deserializer for {@link AIPlayer}.
 */
public class PlayerAdapter implements JsonSerializer<AIPlayer>, JsonDeserializer<AIPlayer> {

    @Override
    public AIPlayer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonArray jsonPlayer = json.getAsJsonArray();
        String playerName = jsonPlayer.get(0).getAsString();
        String strategyName = jsonPlayer.get(1).getAsString();
        IStrategy strategy = PlayerStrategyFactory.getStrategyByName(strategyName);
        return new AIPlayer(playerName, strategy);
    }

    @Override
    public JsonElement serialize(AIPlayer player, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
