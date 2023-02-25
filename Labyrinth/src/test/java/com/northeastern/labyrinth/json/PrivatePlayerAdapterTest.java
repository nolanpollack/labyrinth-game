package com.northeastern.labyrinth.json;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.json.Adapter.PrivatePlayerAdapter;
import java.awt.Color;
import org.junit.jupiter.api.Test;

class PrivatePlayerAdapterTest {

  private final static Gson gson = new GsonBuilder().registerTypeAdapter(PrivatePlayerData.class,
      new PrivatePlayerAdapter()).create();

  @Test
  void testSerialize() {
    PrivatePlayerData privatePlayer = new PrivatePlayerData(Color.BLACK, new Coordinate(1, 1),
        new Coordinate(3, 6), new Coordinate(5, 5), false, 0);
    String actualJsonPlayer = gson.toJson(privatePlayer);
    System.out.println(actualJsonPlayer);
  }

  @Test
  void testDeserialize() {
    String jsonPlayer = "{\"color\":\"000000\",\"current\":{\"row#\":1,\"column#\":1},\"home\":{\"row#\":3,\"column#\":6},\"goto\":{\"row#\":5,\"column#\":5}}";
    PrivatePlayerData expectPlayerData = new PrivatePlayerData(Color.BLACK, new Coordinate(1, 1),
        new Coordinate(3, 6), new Coordinate(5, 5), false, 0);
    PrivatePlayerData actualPlayerData = gson.fromJson(jsonPlayer, PrivatePlayerData.class);

    assertEquals(expectPlayerData.getAvatarColor(), actualPlayerData.getAvatarColor());
    assertEquals(expectPlayerData.getCurrentPosition(), actualPlayerData.getCurrentPosition());
    assertEquals(expectPlayerData.getHomePosition(), actualPlayerData.getHomePosition());
    assertEquals(expectPlayerData.getTargetTile(), actualPlayerData.getTargetTile());
  }

}