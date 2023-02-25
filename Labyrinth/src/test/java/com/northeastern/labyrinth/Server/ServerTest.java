package com.northeastern.labyrinth.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.northeastern.labyrinth.Controller.MockPlayer;
import com.northeastern.labyrinth.Controller.Player.AIPlayer;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Controller.Referee.Referee;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Strategy.EuclidStrategy;
import com.northeastern.labyrinth.Strategy.RiemannStrategy;
import com.northeastern.labyrinth.Util.DaemonThreadFactory;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.json.Adapter.BadPlayerFactory;
import com.northeastern.labyrinth.json.Adapter.RefereeStateAdapter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerTest {

  private final int PORT_NUM = 42430;

  private final Gson gson = new GsonBuilder().registerTypeAdapter(RefereeState.class,
          new RefereeStateAdapter())
      .create();
  private ExecutorService executorService;
  private Server server;
  private Client client1;
  private Client client2;
  private Client client3;
  private Client client4;
  private Client client5;
  private Client client6;
  private Client client7;

  private IPlayer player1;
  private IPlayer player2;
  private IPlayer player3;
  private IPlayer player4;
  private IPlayer player5;
  private IPlayer player6;
  private IPlayer player7;

  private Pair<List<IPlayer>, List<IPlayer>> actualOutcome;


  @BeforeEach
  void initThreadPool() {
    this.executorService = Executors.newCachedThreadPool(new DaemonThreadFactory());
    this.server = new Server(PORT_NUM);
  }

  @AfterEach
  void shutDownThreadPool() {
    this.executorService.shutdown();
  }

  @Test
  void testNoClientConnect() {
    Pair<List<IPlayer>, List<IPlayer>> expectOutcome = new Pair<>(new ArrayList<>(),
        new ArrayList<>());
    // Pass in 2Player state but since only one player connected the game shouldn't start.
    Pair<List<IPlayer>, List<IPlayer>> actualOutcome = this.server.runServer(new Referee(),
        Optional.of(get_2Player_7x7_State()));
    assertEquals(expectOutcome.getFirst(), actualOutcome.getFirst());
    assertEquals(expectOutcome.getSecond(), actualOutcome.getSecond());
  }

  void initOneClient() {
    this.player1 = new AIPlayer("QISHEN", new EuclidStrategy());
    this.client1 = new Client("127.0.0.1", PORT_NUM);
  }

  @Test
  void testOneClientConnection() throws InterruptedException, ExecutionException {
    initOneClient();
    runClient(this.client1, this.player1, 1000);
    Pair<List<IPlayer>, List<IPlayer>> expectOutcome = new Pair<>(new ArrayList<>(),
        new ArrayList<>());
    // Pass in 2Player state but since only one player connected the game shouldn't start.
    Pair<List<IPlayer>, List<IPlayer>> actualOutcome = this.server.runServer(new Referee(),
            Optional.of(get_2Player_7x7_State()));
    assertEquals(expectOutcome.getFirst(), actualOutcome.getFirst());
    assertEquals(expectOutcome.getSecond(), actualOutcome.getSecond());
  }

  void initTwoClient() {
    this.server = new Server(PORT_NUM);
    this.player1 = new AIPlayer("QISHEN", new RiemannStrategy());
    this.player2 = new AIPlayer("DIEGO", new EuclidStrategy());
    this.client1 = new Client("127.0.0.1", PORT_NUM);
    this.client2 = new Client("127.0.0.1", PORT_NUM);
  }

  @Test
  void testTwoClientConnection() {
    initTwoClient();
    runClient(this.client1, this.player1, 1000);
    runClient(this.client2, this.player2, 2000);
    Pair<List<IPlayer>, List<IPlayer>> expectOutcome = new Pair<>(new ArrayList<>(Arrays.asList(this.player2)),
        new ArrayList<>());
    Pair<List<IPlayer>, List<IPlayer>> actualOutcome = this.server.runServer(new Referee(),
            Optional.of(get_2Player_7x7_State()));
    assertEquals(expectOutcome.getFirst().stream().map(IPlayer::name).collect(Collectors.toList()),
        actualOutcome.getFirst().stream().map(IPlayer::name).collect(Collectors.toList()));
    assertEquals(expectOutcome.getSecond().isEmpty(), actualOutcome.getSecond().isEmpty());
  }

  @Test
  void testTwoClient_SecondWaitPeriod_Connection() {
    initTwoClient();
    runClient(this.client1, this.player1, 1000);
    runClient(this.client2, this.player2, 24000);
    Pair<List<IPlayer>, List<IPlayer>> expectOutcome = new Pair<>(new ArrayList<>(Arrays.asList(this.player2)),
            new ArrayList<>());
    Pair<List<IPlayer>, List<IPlayer>> actualOutcome = this.server.runServer(new Referee(),
            Optional.of(get_2Player_7x7_State()));
    assertEquals(expectOutcome.getFirst().stream().map(IPlayer::name).collect(Collectors.toList()),
            actualOutcome.getFirst().stream().map(IPlayer::name).collect(Collectors.toList()));
    assertEquals(expectOutcome.getSecond().isEmpty(), actualOutcome.getSecond().isEmpty());
  }

  void initThreeClient() {
    this.server = new Server(PORT_NUM);
    this.player1 = new AIPlayer("QISHEN", new RiemannStrategy());
    this.player2 = new AIPlayer("DIEGO", new EuclidStrategy());
    this.player3 = new MockPlayer();
    this.client1 = new Client("127.0.0.1", PORT_NUM);
    this.client2 = new Client("127.0.0.1", PORT_NUM);
    this.client3 = new Client("127.0.0.1", PORT_NUM);
  }

  @Test
  void testThreeClientConnection() {
    initThreeClient();
    runClient(this.client1, this.player1, 1000);
    runClient(this.client2, this.player2, 4000);
    runClient(this.client3, this.player3, 7000);

    Pair<List<IPlayer>, List<IPlayer>> expectOutcome = new Pair<>(new ArrayList<>(Arrays.asList(this.player1)),
        new ArrayList<>(Arrays.asList(this.player3)));
    Pair<List<IPlayer>, List<IPlayer>> actualOutcome = this.server.runServer(new Referee(),
        Optional.of(get_3Player_7x7_State()));

    assertEquals(expectOutcome.getFirst().stream().map(IPlayer::name).collect(Collectors.toList()),
        actualOutcome.getFirst().stream().map(IPlayer::name).collect(Collectors.toList()));
    assertEquals(expectOutcome.getSecond().stream().map(IPlayer::name).collect(Collectors.toList()),
        actualOutcome.getSecond().stream().map(IPlayer::name).collect(Collectors.toList()));
  }

  void initSixClient() {
    this.server = new Server(PORT_NUM);
    this.player1 = new AIPlayer("QISHEN", new RiemannStrategy());
    this.player2 = new AIPlayer("DIEGO", new EuclidStrategy());
    this.player3 = new AIPlayer("AI1", new EuclidStrategy());
    this.player4 = BadPlayerFactory.createLoopPlayer("Loop", new RiemannStrategy(), "setUp", 1);
    this.player5 = BadPlayerFactory.createExceptionPlayer("Exception", new EuclidStrategy(), "takeTurn");
    this.player6 = new AIPlayer("AI2",  new RiemannStrategy());
    this.client1 = new Client("127.0.0.1", PORT_NUM);
    this.client2 = new Client("127.0.0.1", PORT_NUM);
    this.client3 = new Client("127.0.0.1", PORT_NUM);
    this.client4 = new Client("127.0.0.1", PORT_NUM);
    this.client5 = new Client("127.0.0.1", PORT_NUM);
    this.client6 = new Client("127.0.0.1", PORT_NUM);
  }

  @Test
  void testSixClientConnection() {
    initSixClient();
    runClient(this.client1, this.player1, 1000);
    runClient(this.client2, this.player2, 4000);
    runClient(this.client3, this.player3, 7000);
    runClient(this.client4, this.player4, 10000);
    runClient(this.client5, this.player5, 13000);
    runClient(this.client6, this.player6, 16000);

    Pair<List<IPlayer>, List<IPlayer>> expectOutcome = new Pair<>(new ArrayList<>(Arrays.asList(this.player6)),
        new ArrayList<>(Arrays.asList(this.player4, this.player5)));
    Pair<List<IPlayer>, List<IPlayer>> actualOutcome = this.server.runServer(new Referee(),
        Optional.of(get_6Player_7x7_State()));
    assertEquals(expectOutcome.getFirst().stream().map(IPlayer::name).collect(Collectors.toList()),
        actualOutcome.getFirst().stream().map(IPlayer::name).collect(Collectors.toList()));
    assertEquals(expectOutcome.getSecond().stream().map(IPlayer::name).collect(Collectors.toList()),
        actualOutcome.getSecond().stream().map(IPlayer::name).collect(Collectors.toList()));
  }

  void initSevenClient() {
    this.server = new Server(PORT_NUM);
    this.player1 = new AIPlayer("QISHEN", new RiemannStrategy());
    this.player2 = new AIPlayer("DIEGO", new EuclidStrategy());
    this.player3 = new AIPlayer("AI1", new EuclidStrategy());
    this.player4 = BadPlayerFactory.createLoopPlayer("Loop", new RiemannStrategy(), "setUp", 1);
    this.player5 = BadPlayerFactory.createExceptionPlayer("Exception", new EuclidStrategy(), "takeTurn");
    this.player6 = new AIPlayer("AI2",  new RiemannStrategy());
    this.player7 = new AIPlayer("Seven", new RiemannStrategy());
    this.client1 = new Client("127.0.0.1", PORT_NUM);
    this.client2 = new Client("127.0.0.1", PORT_NUM);
    this.client3 = new Client("127.0.0.1", PORT_NUM);
    this.client4 = new Client("127.0.0.1", PORT_NUM);
    this.client5 = new Client("127.0.0.1", PORT_NUM);
    this.client6 = new Client("127.0.0.1", PORT_NUM);
    this.client7 = new Client("127.0.0.1", PORT_NUM);
  }

  @Test
  void testSevenClientConnection() {
    initSevenClient();
    runClient(this.client1, this.player1, 1000);
    runClient(this.client2, this.player2, 4000);
    runClient(this.client3, this.player3, 7000);
    runClient(this.client4, this.player4, 10000);
    runClient(this.client5, this.player5, 13000);
    runClient(this.client6, this.player6, 16000);
    runClient(this.client7, this.player7, 19000);

    Pair<List<IPlayer>, List<IPlayer>> expectOutcome = new Pair<>(new ArrayList<>(Arrays.asList(this.player6)),
        new ArrayList<>(Arrays.asList(this.player4, this.player5)));
    Pair<List<IPlayer>, List<IPlayer>> actualOutcome = this.server.runServer(new Referee(),
        Optional.of(get_6Player_7x7_State()));
    assertEquals(expectOutcome.getFirst().stream().map(IPlayer::name).collect(Collectors.toList()),
        actualOutcome.getFirst().stream().map(IPlayer::name).collect(Collectors.toList()));
    assertEquals(expectOutcome.getSecond().stream().map(IPlayer::name).collect(Collectors.toList()),
        actualOutcome.getSecond().stream().map(IPlayer::name).collect(Collectors.toList()));
  }

  private void runClient(Client client, IPlayer player, int wait) {
    this.executorService.execute(() -> {
      try {
        Thread.sleep(wait);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      client.connectAndExecute(player);
    });
  }

  private IRefereeState get_2Player_7x7_State() {
    String state = "{\n"
        + "  \"board\": {\"connectors\":[\n"
        + "    [\"┼\",\"┼\",\"┼\",\"│\",\"─\",\"┐\",\"└\"],\n"
        + "    [\"┼\",\"┼\",\"┼\",\"│\",\"┌\",\"┘\",\"┬\"],\n"
        + "    [\"─\",\"┼\",\"├\",\"│\",\"├\",\"┴\",\"┤\"],\n"
        + "    [\"┼\",\"│\",\"├\",\"┐\",\"└\",\"┌\",\"┘\"],\n"
        + "    [\"┬\",\"├\",\"┴\",\"┤\",\"┼\",\"│\",\"─\"],\n"
        + "    [\"┐\",\"└\",\"┌\",\"┘\",\"┬\",\"├\",\"┴\"],\n"
        + "    [\"┤\",\"┼\",\"│\",\"─\",\"┐\",\"└\",\"┌\"]],"
        + "    \"treasures\":\n"
        + "    [[[\"green-aventurine\",\"orange-radiant\"],[\"hackmanite\",\"azurite\"],[\"ruby\",\"azurite\"],[\"ruby-diamond-profile\",\"alexandrite-pear-shape\"],[\"carnelian\",\"red-spinel-square-emerald-cut\"],[\"chrysoberyl-cushion\",\"pink-spinel-cushion\"],[\"sunstone\",\"emerald\"]],\n"
        + "      [[\"garnet\",\"pink-emerald-cut\"],[\"black-onyx\",\"peridot\"],[\"rose-quartz\",\"apricot-square-radiant\"],[\"raw-beryl\",\"stilbite\"],[\"purple-spinel-trillion\",\"yellow-heart\"],[\"blue-spinel-heart\",\"super-seven\"],[\"zircon\",\"blue-cushion\"]],\n"
        + "      [[\"grossular-garnet\",\"emerald\"],[\"chrysoberyl-cushion\",\"green-beryl-antique\"],[\"raw-citrine\",\"diamond\"],[\"tourmaline\",\"pink-round\"],[\"ruby-diamond-profile\",\"sunstone\"],[\"grossular-garnet\",\"blue-pear-shape\"],[\"zircon\",\"green-aventurine\"]],\n"
        + "      [[\"black-obsidian\",\"raw-beryl\"],[\"purple-oval\",\"aventurine\"],[\"morganite-oval\",\"grossular-garnet\"],[\"green-princess-cut\",\"purple-spinel-trillion\"],[\"rhodonite\",\"bulls-eye\"],[\"raw-beryl\",\"prehnite\"],[\"star-cabochon\",\"magnesite\"]],\n"
        + "      [[\"hackmanite\",\"blue-pear-shape\"],[\"golden-diamond-cut\",\"jasper\"],[\"almandine-garnet\",\"rock-quartz\"],[\"chrysolite\",\"bulls-eye\"],[\"tanzanite-trillion\",\"carnelian\"],[\"peridot\",\"mexican-opal\"],[\"gray-agate\",\"grossular-garnet\"]],\n"
        + "      [[\"peridot\",\"purple-spinel-trillion\"],[\"australian-marquise\",\"jaspilite\"],[\"morganite-oval\",\"pink-emerald-cut\"],[\"amethyst\",\"red-spinel-square-emerald-cut\"],[\"kunzite\",\"australian-marquise\"],[\"zircon\",\"iolite-emerald-cut\"],[\"green-princess-cut\",\"white-square\"]],\n"
        + "      [[\"carnelian\",\"green-beryl-antique\"],[\"kunzite-oval\",\"black-spinel-cushion\"],[\"lemon-quartz-briolette\",\"diamond\"],[\"red-diamond\",\"peridot\"],[\"moonstone\",\"yellow-heart\"],[\"prasiolite\",\"ruby\"],[\"pink-emerald-cut\",\"aquamarine\"]]]\n"
        + "  },\n"
        + "  \"last\": [4, \"UP\"],\n"
        + "  \"spare\": {\n"
        + "    \"tilekey\": \"─\",\n"
        + "    \"1-image\": \"apatite\",\n"
        + "    \"2-image\": \"carnelian\"\n"
        + "  },\n"
        + "  \"plmt\": [\n"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 5,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"color\": \"pink\"\n"
        + "    },"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 5\n"
        + "      },\n"
        + "      \"color\": \"blue\"\n"
        + "    }"
        + "  ]\n"
        + "}";

    return gson.fromJson(state, RefereeState.class);
  }

  private IRefereeState get_3Player_7x7_State() {
    String state = "{\n"
        + "  \"board\": {\"connectors\":[\n"
        + "    [\"│\",\"─\",\"─\",\"─\",\"─\",\"─\",\"─\"],\n"
        + "    [\"─\",\"─\",\"─\",\"─\",\"─\",\"─\",\"─\"],\n"
        + "    [\"─\",\"─\",\"─\",\"─\",\"─\",\"─\",\"─\"],\n"
        + "    [\"│\",\"│\",\"┤\",\"┤\",\"├\",\"├\",\"│\"],\n"
        + "    [\"─\",\"│\",\"┐\",\"┼\",\"┤\",\"┘\",\"┌\"],\n"
        + "    [\"│\",\"│\",\"│\",\"┬\",\"│\",\"┼\",\"┌\"],\n"
        + "    [\"│\",\"│\",\"│\",\"─\",\"─\",\"─\",\"─\"]],\n"
        + "    \"treasures\":\n"
        + "    [[[\"green-aventurine\",\"orange-radiant\"],[\"hackmanite\",\"azurite\"],[\"ruby\",\"azurite\"],[\"ruby-diamond-profile\",\"alexandrite-pear-shape\"],[\"carnelian\",\"red-spinel-square-emerald-cut\"],[\"chrysoberyl-cushion\",\"pink-spinel-cushion\"],[\"sunstone\",\"emerald\"]],\n"
        + "      [[\"garnet\",\"pink-emerald-cut\"],[\"black-onyx\",\"peridot\"],[\"rose-quartz\",\"apricot-square-radiant\"],[\"raw-beryl\",\"stilbite\"],[\"purple-spinel-trillion\",\"yellow-heart\"],[\"blue-spinel-heart\",\"super-seven\"],[\"zircon\",\"blue-cushion\"]],\n"
        + "      [[\"grossular-garnet\",\"emerald\"],[\"chrysoberyl-cushion\",\"green-beryl-antique\"],[\"raw-citrine\",\"diamond\"],[\"tourmaline\",\"pink-round\"],[\"ruby-diamond-profile\",\"sunstone\"],[\"grossular-garnet\",\"blue-pear-shape\"],[\"zircon\",\"green-aventurine\"]],\n"
        + "      [[\"black-obsidian\",\"raw-beryl\"],[\"purple-oval\",\"aventurine\"],[\"morganite-oval\",\"grossular-garnet\"],[\"green-princess-cut\",\"purple-spinel-trillion\"],[\"rhodonite\",\"bulls-eye\"],[\"raw-beryl\",\"prehnite\"],[\"star-cabochon\",\"magnesite\"]],\n"
        + "      [[\"hackmanite\",\"blue-pear-shape\"],[\"golden-diamond-cut\",\"jasper\"],[\"almandine-garnet\",\"rock-quartz\"],[\"chrysolite\",\"bulls-eye\"],[\"tanzanite-trillion\",\"carnelian\"],[\"peridot\",\"mexican-opal\"],[\"gray-agate\",\"grossular-garnet\"]],\n"
        + "      [[\"peridot\",\"purple-spinel-trillion\"],[\"australian-marquise\",\"jaspilite\"],[\"morganite-oval\",\"pink-emerald-cut\"],[\"amethyst\",\"red-spinel-square-emerald-cut\"],[\"kunzite\",\"australian-marquise\"],[\"zircon\",\"iolite-emerald-cut\"],[\"green-princess-cut\",\"white-square\"]],\n"
        + "      [[\"carnelian\",\"green-beryl-antique\"],[\"kunzite-oval\",\"black-spinel-cushion\"],[\"lemon-quartz-briolette\",\"diamond\"],[\"red-diamond\",\"peridot\"],[\"moonstone\",\"yellow-heart\"],[\"prasiolite\",\"ruby\"],[\"pink-emerald-cut\",\"aquamarine\"]]]\n"
        + "  },\n"
        + "  \"last\": [4, \"UP\"],\n"
        + "  \"spare\": {\n"
        + "    \"tilekey\": \"─\",\n"
        + "    \"1-image\": \"apatite\",\n"
        + "    \"2-image\": \"carnelian\"\n"
        + "  },\n"
        + "  \"plmt\": [\n"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 5,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"color\": \"pink\"\n"
        + "    },"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 3\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 5\n"
        + "      },\n"
        + "      \"color\": \"blue\"\n"
        + "    },"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 1,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"color\": \"red\"\n"
        + "    }"
        + "  ]\n"
        + "}";

    return gson.fromJson(state, RefereeState.class);
  }

  private IRefereeState get_6Player_7x7_State() {
    String state = "{\n"
        + "  \"board\": {\"connectors\":[\n"
        + "    [\"┼\",\"┼\",\"┼\",\"│\",\"─\",\"┐\",\"└\"],\n"
        + "    [\"┼\",\"┼\",\"┼\",\"│\",\"┌\",\"┘\",\"┬\"],\n"
        + "    [\"─\",\"┼\",\"├\",\"│\",\"├\",\"┴\",\"┤\"],\n"
        + "    [\"┼\",\"│\",\"├\",\"┐\",\"└\",\"┌\",\"┘\"],\n"
        + "    [\"┬\",\"├\",\"┴\",\"┤\",\"┼\",\"│\",\"─\"],\n"
        + "    [\"┐\",\"└\",\"┌\",\"┘\",\"┬\",\"├\",\"┴\"],\n"
        + "    [\"┤\",\"┼\",\"│\",\"─\",\"┐\",\"└\",\"┌\"]],"
        + "    \"treasures\":\n"
        + "    [[[\"green-aventurine\",\"orange-radiant\"],[\"hackmanite\",\"azurite\"],[\"ruby\",\"azurite\"],[\"ruby-diamond-profile\",\"alexandrite-pear-shape\"],[\"carnelian\",\"red-spinel-square-emerald-cut\"],[\"chrysoberyl-cushion\",\"pink-spinel-cushion\"],[\"sunstone\",\"emerald\"]],\n"
        + "      [[\"garnet\",\"pink-emerald-cut\"],[\"black-onyx\",\"peridot\"],[\"rose-quartz\",\"apricot-square-radiant\"],[\"raw-beryl\",\"stilbite\"],[\"purple-spinel-trillion\",\"yellow-heart\"],[\"blue-spinel-heart\",\"super-seven\"],[\"zircon\",\"blue-cushion\"]],\n"
        + "      [[\"grossular-garnet\",\"emerald\"],[\"chrysoberyl-cushion\",\"green-beryl-antique\"],[\"raw-citrine\",\"diamond\"],[\"tourmaline\",\"pink-round\"],[\"ruby-diamond-profile\",\"sunstone\"],[\"grossular-garnet\",\"blue-pear-shape\"],[\"zircon\",\"green-aventurine\"]],\n"
        + "      [[\"black-obsidian\",\"raw-beryl\"],[\"purple-oval\",\"aventurine\"],[\"morganite-oval\",\"grossular-garnet\"],[\"green-princess-cut\",\"purple-spinel-trillion\"],[\"rhodonite\",\"bulls-eye\"],[\"raw-beryl\",\"prehnite\"],[\"star-cabochon\",\"magnesite\"]],\n"
        + "      [[\"hackmanite\",\"blue-pear-shape\"],[\"golden-diamond-cut\",\"jasper\"],[\"almandine-garnet\",\"rock-quartz\"],[\"chrysolite\",\"bulls-eye\"],[\"tanzanite-trillion\",\"carnelian\"],[\"peridot\",\"mexican-opal\"],[\"gray-agate\",\"grossular-garnet\"]],\n"
        + "      [[\"peridot\",\"purple-spinel-trillion\"],[\"australian-marquise\",\"jaspilite\"],[\"morganite-oval\",\"pink-emerald-cut\"],[\"amethyst\",\"red-spinel-square-emerald-cut\"],[\"kunzite\",\"australian-marquise\"],[\"zircon\",\"iolite-emerald-cut\"],[\"green-princess-cut\",\"white-square\"]],\n"
        + "      [[\"carnelian\",\"green-beryl-antique\"],[\"kunzite-oval\",\"black-spinel-cushion\"],[\"lemon-quartz-briolette\",\"diamond\"],[\"red-diamond\",\"peridot\"],[\"moonstone\",\"yellow-heart\"],[\"prasiolite\",\"ruby\"],[\"pink-emerald-cut\",\"aquamarine\"]]]\n"
        + "  },\n"
        + "  \"last\": [4, \"UP\"],\n"
        + "  \"spare\": {\n"
        + "    \"tilekey\": \"─\",\n"
        + "    \"1-image\": \"apatite\",\n"
        + "    \"2-image\": \"carnelian\"\n"
        + "  },\n"
        + "  \"plmt\": [\n"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 5,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"color\": \"pink\"\n"
        + "    },"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 3\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 5\n"
        + "      },\n"
        + "      \"color\": \"blue\"\n"
        + "    },"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 5\n"
        + "      },\n"
        + "      \"color\": \"red\"\n"
        + "    },"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 1,\n"
        + "        \"column#\": 3\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 5\n"
        + "      },\n"
        + "      \"color\": \"green\"\n"
        + "    },"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 1,\n"
        + "        \"column#\": 5\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 3,\n"
        + "        \"column#\": 5\n"
        + "      },\n"
        + "      \"color\": \"black\"\n"
        + "    },"
        + "    {\n"
        + "      \"current\": {\n"
        + "        \"row#\": 0,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"home\": {\n"
        + "        \"row#\": 5,\n"
        + "        \"column#\": 5\n"
        + "      },\n"
        + "      \"goto\": {\n"
        + "        \"row#\": 1,\n"
        + "        \"column#\": 1\n"
        + "      },\n"
        + "      \"color\": \"yellow\"\n"
        + "    }"
        + "  ]\n"
        + "}";

    return gson.fromJson(state, RefereeState.class);
  }
}