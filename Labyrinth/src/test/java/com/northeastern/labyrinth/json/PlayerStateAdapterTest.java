package com.northeastern.labyrinth.json;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.northeastern.labyrinth.Model.GameState.PlayerState;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.json.Adapter.PlayerStateAdapter;
import org.junit.jupiter.api.Test;

class PlayerStateAdapterTest {

  @Test
  void testDeserialize() {
    String connectors = "[[\"┌\",\"─\",\"─\",\"┬\",\"─\",\"─\",\"┐\"],\n"
        + "  [\"│\",\"┘\",\"│\",\"┘\",\"┬\",\"├\",\"│\"],\n"
        + "  [\"│\",\"─\",\"│\",\"┤\",\"┼\",\"│\",\"│\"],\n"
        + "  [\"│\",\"└\",\"┌\",\"┘\",\"┬\",\"├\",\"│\"],\n"
        + "  [\"│\",\"┼\",\"│\",\"─\",\"┐\",\"└\",\"│\"],\n"
        + "  [\"│\",\"┬\",\"├\",\"┴\",\"┤\",\"┼\",\"│\"],\n"
        + "  [\"└\",\"┐\",\"└\",\"─\",\"─\",\"┬\",\"┘\"]]";
    String treasures =
        "[[[\"alexandrite-pear-shape\",\"ruby-diamond-profile\"],[\"zircon\",\"rose-quartz\"],[\"clinohumite\",\"yellow-jasper\"],[\"garnet\",\"yellow-baguette\"],[\"rhodonite\",\"white-square\"],[\"zoisite\",\"unakite\"],[\"zoisite\",\"tourmaline\"]],\n"
            + "[[\"zircon\",\"rock-quartz\"],[\"beryl\",\"rhodonite\"],[\"chrysolite\",\"yellow-heart\"],[\"diamond\",\"tourmaline-laser-cut\"],[\"zoisite\",\"tigers-eye\"],[\"zoisite\",\"tanzanite-trillion\"],[\"zoisite\",\"super-seven\"]],\n"
            + "[[\"alexandrite\",\"zoisite\"],[\"ammolite\",\"zircon\"],[\"carnelian\",\"yellow-beryl-oval\"],[\"cordierite\",\"sunstone\"],[\"zoisite\",\"stilbite\"],[\"zoisite\",\"star-cabochon\"],[\"zoisite\",\"spinel\"]],\n"
            + "[[\"almandine-garnet\",\"sphalerite\"],[\"zoisite\",\"ruby\"],[\"aventurine\",\"ruby-diamond-profile\"],[\"zoisite\",\"rose-quartz\"],[\"zoisite\",\"rock-quartz\"],[\"zoisite\",\"rhodonite\"],[\"zoisite\",\"red-spinel-square-emerald-cut\"]],\n"
            + "[[\"zoisite\",\"red-diamond\"],[\"black-onyx\",\"raw-citrine\"],[\"zoisite\",\"raw-beryl\"],[\"diamond\",\"purple-square-cushion\"],[\"zoisite\",\"purple-spinel-trillion\"],[\"zoisite\",\"purple-oval\"],[\"zoisite\",\"purple-cabochon\"]],\n"
            + "[[\"amethyst\",\"prehnite\"],[\"ametrine\",\"prasiolite\"],[\"citrine\",\"pink-spinel-cushion\"],[\"emerald\",\"pink-round\"],[\"zoisite\",\"pink-opal\"],[\"zoisite\",\"pink-emerald-cut\"],[\"zoisite\",\"peridot\"]],\n"
            + "[[\"zoisite\",\"padparadscha-sapphire\"],[\"zoisite\",\"padparadscha-oval\"],[\"zoisite\",\"orange-radiant\"],[\"zoisite\",\"moss-agate\"],[\"zoisite\",\"morganite-oval\"],[\"zoisite\",\"moonstone\"],[\"zoisite\",\"mexican-opal\"]]]";
    String boardJson = "{ \"connectors\":" + connectors + ", \"treasures\":" + treasures + "}";
    String tile = "{ \"tilekey\" : \"┤\", \"1-image\" : \"orange-radiant\",\"2-image\" : \"prasiolite\" }";
    String player1 = "{ \"current\" : {\"row#\": 0, \"column#\":0}, \"home\" : {\"row#\": 0, \"column#\":0}, \"color\" : \"pink\" }";
    String player2 = "{ \"current\" : {\"row#\": 1, \"column#\":1}, \"home\" : {\"row#\": 1, \"column#\":1}, \"color\" : \"white\" }";
    String playerList = "[" + player1 + "," + player2 + "]";
    String action = "[" + 1 + ", \"RIGHT\"]";
    String json =
        "{ \"board\":" + boardJson + ", \"spare\":" + tile + ", \"plmt\":" + playerList
            + ", \"last\":"
            + action + "}";

    Gson gson = new GsonBuilder().registerTypeAdapter(PlayerState.class, new PlayerStateAdapter()).create();

    PlayerState playerGameState = gson.fromJson(json, PlayerState.class);

    assertEquals(7, playerGameState.getBoard().getNumRows());
    assertEquals(7, playerGameState.getBoard().getNumCols());

    assertEquals(new Coordinate(0, 0), playerGameState.getCurrentPlayerPosition());

    assertFalse(playerGameState.canReachGivenLocation(new Coordinate(1, 1)));
    assertTrue(playerGameState.canReachGivenLocation(new Coordinate(6, 1)));
    assertTrue(playerGameState.canReachGivenLocation(new Coordinate(6, 6)));
  }
}