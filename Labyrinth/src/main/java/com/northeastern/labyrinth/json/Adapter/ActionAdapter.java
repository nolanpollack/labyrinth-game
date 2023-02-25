package com.northeastern.labyrinth.json.Adapter;

import com.google.gson.*;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.lang.reflect.Type;

/**
 * Custom Json Serializer and Deserializer for {@link Action}.
 */
public class ActionAdapter implements JsonSerializer<Action>, JsonDeserializer<Action> {

    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonArray jsonAction = json.getAsJsonArray();
        int shiftIndex = jsonAction.get(0).getAsInt();
        Direction direction = context.deserialize(jsonAction.get(1), Direction.class);
        int degree = jsonAction.get(2).getAsInt();
        Coordinate movePosition = context.deserialize(jsonAction.get(3), Coordinate.class);

        SlideAction slideAction = new SlideAction(shiftIndex, direction);
        return new Action(slideAction, degree, movePosition);
    }

    @Override
    public JsonElement serialize(Action action, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonAction = new JsonArray();
        jsonAction.add(action.getIndex());
        jsonAction.add(context.serialize(action.getDirection()));
        jsonAction.add(action.getRotation());
        jsonAction.add(context.serialize(action.getToMove()));

        return jsonAction;
    }
}
