package com.northeastern.labyrinth.json;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;
import com.northeastern.labyrinth.json.Adapter.ActionAdapter;
import org.junit.jupiter.api.Test;

class ActionAdapterTest {

  private final static Gson gson = new GsonBuilder().registerTypeAdapter(Action.class,
      new ActionAdapter()).create();

  @Test
  void testSerialize() {
    SlideAction slide = new SlideAction(5, Direction.UP);
    Coordinate coordinate = new Coordinate(0, 0);
    Action action = new Action(slide, 0, coordinate);
    String jsonAction = gson.toJson(action);
  }

  @Test
  void testDeserialize() {
    String jsonAction = "[5,\"UP\",0,{\"row#\":0,\"column#\":0}]\n";
    SlideAction slide = new SlideAction(5, Direction.UP);
    Coordinate coordinate = new Coordinate(0, 0);
    Action expectAction = new Action(slide, 0, coordinate);
    Action actualAction = gson.fromJson(jsonAction, Action.class);
    assertEquals(expectAction, actualAction);
  }

}