package com.northeastern.labyrinth.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.json.Adapter.PublicPlayerAdapter;
import java.awt.Color;
import org.junit.jupiter.api.Test;

class PublicPlayerAdapterTest {

  private final static Gson gson = new GsonBuilder().registerTypeAdapter(PublicPlayerData.class,
      new PublicPlayerAdapter()).create();

  @Test
  void testSerialize() {
    PublicPlayerData playerData = new PublicPlayerData(Color.BLUE, new Coordinate(0, 5),
        new Coordinate(6, 6));
    String jsonPlayerData = gson.toJson(playerData);
    System.out.println(jsonPlayerData);
  }

  @Test
  void testDeserialize() {
    String jsonPlayerData = "{\"color\":\"0000FF\",\"current\":{\"row#\":0,\"column#\":5},\"home\":{\"row#\":6,\"column#\":6}}";
    PublicPlayerData expectPlayerData = new PublicPlayerData(Color.BLUE, new Coordinate(0, 5),
        new Coordinate(6, 6));
    PublicPlayerData actualPlayerData = gson.fromJson(jsonPlayerData, PublicPlayerData.class);
    assertEquals(expectPlayerData.getAvatarColor(), actualPlayerData.getAvatarColor());
    assertEquals(expectPlayerData.getHomePosition(), actualPlayerData.getHomePosition());
    assertEquals(expectPlayerData.getCurrentPosition(), actualPlayerData.getCurrentPosition());
  }

  @Test
  void testDeserialize_StringColor() {
    String jsonPlayerData = "{\"color\":\"red\",\"current\":{\"row#\":0,\"column#\":5},\"home\":{\"row#\":6,\"column#\":6}}";
    PublicPlayerData expectPlayerData = new PublicPlayerData(new Color(255, 0, 0), new Coordinate(0, 5),
        new Coordinate(6, 6));
    PublicPlayerData actualPlayerData = gson.fromJson(jsonPlayerData, PublicPlayerData.class);
    assertEquals(expectPlayerData.getAvatarColor(), actualPlayerData.getAvatarColor());
    assertEquals(expectPlayerData.getHomePosition(), actualPlayerData.getHomePosition());
    assertEquals(expectPlayerData.getCurrentPosition(), actualPlayerData.getCurrentPosition());
  }
}