package com.northeastern.labyrinth.json;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.json.Adapter.TileAdapter;
import org.junit.jupiter.api.Test;

class TileAdapterTest {

  private final static Gson gson = new GsonBuilder().registerTypeAdapter(Tile.class,
      new TileAdapter()).create();

  @Test
  void testSerialize() {
    Tile tile = new Tile('├', "alexandrite", "stilbite");
    String json = gson.toJson(tile);
    System.out.println(json);
  }

  @Test
  void testDeserialize() {
    String jsonString = "{\"tilekey\":\"├\",\"1-image\":\"alexandrite\",\"2-image\":\"stilbite\"}";
    Tile expectedTile = new Tile('├', "alexandrite", "stilbite");
    Tile actualTile = gson.fromJson(jsonString, Tile.class);
    assertEquals(expectedTile, actualTile);
  }

}