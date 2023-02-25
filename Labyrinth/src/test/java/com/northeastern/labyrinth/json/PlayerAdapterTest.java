package com.northeastern.labyrinth.json;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.northeastern.labyrinth.Controller.Player.AIPlayer;
import com.northeastern.labyrinth.json.Adapter.PlayerAdapter;
import org.junit.jupiter.api.Test;

class PlayerAdapterTest {

  private final static Gson gson = new GsonBuilder().registerTypeAdapter(AIPlayer.class,
      new PlayerAdapter()).create();

  @Test
  void testDeserialize() {
    String jsonPlayer = "[\"PlayerA\", \"Euclid\"]";
    AIPlayer player = gson.fromJson(jsonPlayer, AIPlayer.class);
    assertEquals("PlayerA", player.name());
  }
}