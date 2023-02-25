package com.northeastern.labyrinth.json;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.BoardTestUtil;
import com.northeastern.labyrinth.json.Adapter.BoardAdapter;
import org.junit.jupiter.api.Test;

class BoardAdapterTest {

  private final static Gson gson = new GsonBuilder().registerTypeAdapter(MazeBoard.class,
      new BoardAdapter()).create();

  @Test
  void testSerialize() {
    MazeBoard mazeBoard = new MazeBoard(BoardTestUtil.create_SampleBoard());
    String jsonBoard = gson.toJson(mazeBoard);
    System.out.println(jsonBoard);
  }

  @Test
  void testDeserialize() {
    String jsonBoard = "{\"connectors\":[[\"┌\",\"┐\",\"─\"],[\"┌\",\"┼\",\"─\"],[\"┤\",\"┴\",\"┘\"]],\"treasures\":[[[\"alexandrite\",\"alexandrite\"],[\"alexandrite\",\"amethyst\"],[\"alexandrite\",\"ametrine\"]],[[\"alexandrite\",\"ammolite\"],[\"alexandrite\",\"apatite\"],[\"alexandrite\",\"aplite\"]],[[\"alexandrite\",\"azurite\"],[\"alexandrite\",\"beryl\"],[\"alexandrite\",\"carnelian\"]]]}\n";
    MazeBoard expectBoard = new MazeBoard(BoardTestUtil.create_SampleBoard());
    MazeBoard actualBoard = gson.fromJson(jsonBoard, MazeBoard.class);

    assertArrayEquals(expectBoard.copyBoard(), actualBoard.copyBoard());
  }

}