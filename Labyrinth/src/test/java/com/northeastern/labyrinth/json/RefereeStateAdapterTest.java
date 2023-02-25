package com.northeastern.labyrinth.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.json.Adapter.RefereeStateAdapter;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RefereeStateAdapterTest {

  private final static Gson gson = new GsonBuilder().registerTypeAdapter(RefereeState.class,
      new RefereeStateAdapter()).create();

  @Test
  void testDeserialize() {
    String jsonRefereeState = "{\"board\":{\"connectors\":[[\"┌\",\"┌\",\"┐\",\"│\",\"─\",\"┐\",\"└\"],[\"└\",\"│\",\"┘\",\"│\",\"┌\",\"┘\",\"┬\"],[\"─\",\"─\",\"─\",\"│\",\"├\",\"┴\",\"┤\"],[\"┼\",\"│\",\"─\",\"┐\",\"└\",\"┌\",\"┘\"],[\"┬\",\"├\",\"┴\",\"┤\",\"┼\",\"│\",\"─\"],[\"┐\",\"└\",\"┌\",\"┘\",\"┬\",\"├\",\"┴\"],[\"┤\",\"┼\",\"│\",\"─\",\"┐\",\"└\",\"┌\"]],\"treasures\":[[[\"zircon\",\"pink-opal\"],[\"zircon\",\"pink-emerald-cut\"],[\"zircon\",\"peridot\"],[\"yellow-heart\",\"chrysolite\"],[\"yellow-heart\",\"carnelian\"],[\"yellow-heart\",\"bulls-eye\"],[\"yellow-heart\",\"blue-spinel-heart\"]],[[\"zircon\",\"padparadscha-sapphire\"],[\"zircon\",\"padparadscha-oval\"],[\"zircon\",\"orange-radiant\"],[\"yellow-heart\",\"chrysoberyl-cushion\"],[\"yellow-heart\",\"blue-pear-shape\"],[\"yellow-heart\",\"blue-cushion\"],[\"yellow-heart\",\"blue-ceylon-sapphire\"]],[[\"yellow-heart\",\"clinohumite\"],[\"yellow-heart\",\"citrine\"],[\"yellow-heart\",\"citrine-checkerboard\"],[\"yellow-heart\",\"chrome-diopside\"],[\"yellow-heart\",\"black-spinel-cushion\"],[\"yellow-heart\",\"black-onyx\"],[\"yellow-heart\",\"black-obsidian\"]],[[\"yellow-heart\",\"beryl\"],[\"yellow-heart\",\"azurite\"],[\"yellow-heart\",\"aventurine\"],[\"yellow-heart\",\"australian-marquise\"],[\"yellow-heart\",\"aquamarine\"],[\"yellow-heart\",\"apricot-square-radiant\"],[\"yellow-heart\",\"aplite\"]],[[\"yellow-heart\",\"apatite\"],[\"yellow-heart\",\"ammolite\"],[\"yellow-heart\",\"ametrine\"],[\"yellow-heart\",\"amethyst\"],[\"yellow-heart\",\"almandine-garnet\"],[\"yellow-heart\",\"alexandrite\"],[\"yellow-heart\",\"alexandrite-pear-shape\"]],[[\"yellow-beryl-oval\",\"zoisite\"],[\"yellow-beryl-oval\",\"zircon\"],[\"yellow-beryl-oval\",\"yellow-jasper\"],[\"yellow-beryl-oval\",\"yellow-heart\"],[\"yellow-beryl-oval\",\"yellow-beryl-oval\"],[\"yellow-beryl-oval\",\"yellow-baguette\"],[\"yellow-beryl-oval\",\"white-square\"]],[[\"yellow-beryl-oval\",\"unakite\"],[\"yellow-beryl-oval\",\"tourmaline\"],[\"yellow-beryl-oval\",\"tourmaline-laser-cut\"],[\"yellow-beryl-oval\",\"tigers-eye\"],[\"yellow-beryl-oval\",\"tanzanite-trillion\"],[\"yellow-beryl-oval\",\"super-seven\"],[\"yellow-beryl-oval\",\"sunstone\"]]]},\"last\":null,\"plmt\":[{\"color\":\"white\",\"current\":{\"column#\":3,\"row#\":3},\"goto\":{\"column#\":3,\"row#\":5},\"home\":{\"column#\":3,\"row#\":3}},{\"color\":\"black\",\"current\":{\"column#\":3,\"row#\":3},\"goto\":{\"column#\":1,\"row#\":5},\"home\":{\"column#\":1,\"row#\":1}},{\"color\":\"purple\",\"current\":{\"column#\":2,\"row#\":0},\"goto\":{\"column#\":3,\"row#\":1},\"home\":{\"column#\":5,\"row#\":3}},{\"color\":\"yellow\",\"current\":{\"column#\":3,\"row#\":0},\"goto\":{\"column#\":1,\"row#\":5},\"home\":{\"column#\":3,\"row#\":5}}],\"spare\":{\"1-image\":\"yellow-beryl-oval\",\"2-image\":\"ammolite\",\"tilekey\":\"┤\"}}";
    RefereeState refereeState = gson.fromJson(jsonRefereeState, RefereeState.class);
    System.out.println(refereeState);
  }

  @Test
  void TEST_DESERIALIZE_GOAL_TILES() {
    String jsonRefereeState = "{\"board\":{\"connectors\":[[\"┌\",\"┌\",\"┐\",\"│\",\"─\",\"┐\",\"└\"],[\"└\",\"│\",\"┘\",\"│\",\"┌\",\"┘\",\"┬\"],[\"─\",\"─\",\"─\",\"│\",\"├\",\"┴\",\"┤\"],[\"┼\",\"│\",\"─\",\"┐\",\"└\",\"┌\",\"┘\"],[\"┬\",\"├\",\"┴\",\"┤\",\"┼\",\"│\",\"─\"],[\"┐\",\"└\",\"┌\",\"┘\",\"┬\",\"├\",\"┴\"],[\"┤\",\"┼\",\"│\",\"─\",\"┐\",\"└\",\"┌\"]],\"treasures\":[[[\"zircon\",\"pink-opal\"],[\"zircon\",\"pink-emerald-cut\"],[\"zircon\",\"peridot\"],[\"yellow-heart\",\"chrysolite\"],[\"yellow-heart\",\"carnelian\"],[\"yellow-heart\",\"bulls-eye\"],[\"yellow-heart\",\"blue-spinel-heart\"]],[[\"zircon\",\"padparadscha-sapphire\"],[\"zircon\",\"padparadscha-oval\"],[\"zircon\",\"orange-radiant\"],[\"yellow-heart\",\"chrysoberyl-cushion\"],[\"yellow-heart\",\"blue-pear-shape\"],[\"yellow-heart\",\"blue-cushion\"],[\"yellow-heart\",\"blue-ceylon-sapphire\"]],[[\"yellow-heart\",\"clinohumite\"],[\"yellow-heart\",\"citrine\"],[\"yellow-heart\",\"citrine-checkerboard\"],[\"yellow-heart\",\"chrome-diopside\"],[\"yellow-heart\",\"black-spinel-cushion\"],[\"yellow-heart\",\"black-onyx\"],[\"yellow-heart\",\"black-obsidian\"]],[[\"yellow-heart\",\"beryl\"],[\"yellow-heart\",\"azurite\"],[\"yellow-heart\",\"aventurine\"],[\"yellow-heart\",\"australian-marquise\"],[\"yellow-heart\",\"aquamarine\"],[\"yellow-heart\",\"apricot-square-radiant\"],[\"yellow-heart\",\"aplite\"]],[[\"yellow-heart\",\"apatite\"],[\"yellow-heart\",\"ammolite\"],[\"yellow-heart\",\"ametrine\"],[\"yellow-heart\",\"amethyst\"],[\"yellow-heart\",\"almandine-garnet\"],[\"yellow-heart\",\"alexandrite\"],[\"yellow-heart\",\"alexandrite-pear-shape\"]],[[\"yellow-beryl-oval\",\"zoisite\"],[\"yellow-beryl-oval\",\"zircon\"],[\"yellow-beryl-oval\",\"yellow-jasper\"],[\"yellow-beryl-oval\",\"yellow-heart\"],[\"yellow-beryl-oval\",\"yellow-beryl-oval\"],[\"yellow-beryl-oval\",\"yellow-baguette\"],[\"yellow-beryl-oval\",\"white-square\"]],[[\"yellow-beryl-oval\",\"unakite\"],[\"yellow-beryl-oval\",\"tourmaline\"],[\"yellow-beryl-oval\",\"tourmaline-laser-cut\"],[\"yellow-beryl-oval\",\"tigers-eye\"],[\"yellow-beryl-oval\",\"tanzanite-trillion\"],[\"yellow-beryl-oval\",\"super-seven\"],[\"yellow-beryl-oval\",\"sunstone\"]]]},\"goals\": [{\"column#\":3,\"row#\":3}, {\"column#\":1,\"row#\":3}, {\"column#\":3,\"row#\":1}], \"last\":null,\"plmt\":[{\"color\":\"white\",\"current\":{\"column#\":3,\"row#\":3},\"goto\":{\"column#\":3,\"row#\":5},\"home\":{\"column#\":3,\"row#\":3}},{\"color\":\"black\",\"current\":{\"column#\":3,\"row#\":3},\"goto\":{\"column#\":1,\"row#\":5},\"home\":{\"column#\":1,\"row#\":1}},{\"color\":\"purple\",\"current\":{\"column#\":2,\"row#\":0},\"goto\":{\"column#\":3,\"row#\":1},\"home\":{\"column#\":5,\"row#\":3}},{\"color\":\"yellow\",\"current\":{\"column#\":3,\"row#\":0},\"goto\":{\"column#\":1,\"row#\":5},\"home\":{\"column#\":3,\"row#\":5}}],\"spare\":{\"1-image\":\"yellow-beryl-oval\",\"2-image\":\"ammolite\",\"tilekey\":\"┤\"}}";
    RefereeState refereeState = gson.fromJson(jsonRefereeState, RefereeState.class);
    Queue<Coordinate> get = refereeState.getGoalTiles();
    assertEquals(3, get.size());

    for (Coordinate coordinate : get) {
        System.out.println(coordinate.toString());
    }

    //System.out.println(refereeState);
  }
}