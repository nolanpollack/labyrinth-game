package com.northeastern.labyrinth.Controller.Referee;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Controller.Rulebook;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.DaemonThreadFactory;
import com.northeastern.labyrinth.Util.Generate;
import com.northeastern.labyrinth.Util.Pair;
import com.northeastern.labyrinth.Util.Turn.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Manages a game from start to finish given a list of players and a game state. The referee will
 * validate that a player move is a valid move through the Rulebook.
 */
public class Referee implements IReferee {

    protected final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    // The amount of time a player has to return a response when called. Measured in TIME_UNIT.
    protected final static int TIME_OUT = 4;
    //Maximum board size that can be generated. This constraint does not apply to a given state.
    protected final static int MAX_BOARD_SIZE = 9;
    //List of players who have been kicked out.
    private final List<IPlayer> listMisbehaved;
    //A game state representing complete knowledge.
    protected IRefereeState state;
    // number of passes in a round
    private int passes;
    // Future Java
    protected ExecutorService executorService;

    public Referee() {
        this.listMisbehaved = new ArrayList<>();
        this.passes = 0;
    }

    @Override
    public Pair<List<IPlayer>, List<IPlayer>> runGame(IRefereeState state, List<IPlayer> playerList) {
        this.executorService = Executors.newCachedThreadPool(new DaemonThreadFactory());
        this.state = state;
        setup(playerList);

        Rulebook.validGoalTiles(this.state);
        Rulebook.validHomeTiles(this.state);

        steadyState();
        Pair<List<IPlayer>, List<IPlayer>> gameResult = endGame();

        this.executorService.shutdownNow();
        return gameResult;
    }

    /**
     * Runs a game with only connected players. Generates complete game state info
     */
    public Pair<List<IPlayer>, List<IPlayer>> runGame(List<IPlayer> playerList) {
        Generate generate = new Generate();
        return runGame(generate.generateRefereeState(MAX_BOARD_SIZE, playerList), playerList);
    }


    /**
     * Assigns external player to their PrivatePlayerData internal game representation. Notifies
     * each player of the initial game state and their target tile
     */
    protected void setup(List<IPlayer> playerList) {
        this.state = this.state.assignPlayerToPlayerData(playerList);
        List<PrivatePlayerData> playerDataList = this.state.getListOfPlayers();

        for (PrivatePlayerData playerData : playerDataList) {
            Coordinate targetTile = playerData.getTargetTile();
            notifyPlayerSetup(playerData.getPlayer().get(), Optional.of(this.state.getPlayerState()),
                    targetTile);
        }
    }

    /**
     * Runs through each stage until a game termination condition is found or the rulebook limit of round is met.
     */
    private void steadyState() {
        int numberOfRounds = 0;

        while (!Rulebook.gameOver(this.state, this.passes, numberOfRounds)) {
            performRound();
            numberOfRounds++;
        }
    }

    /**
     * Perform a single round. Terminates early if a winning move is performed. Performs a player turn
     * on the game state's current player.
     */
    private void performRound() {
        this.passes = 0;
        int roundLength = this.state.numberOfPlayers();

        for (int i = 0; i < roundLength && !Rulebook.roundOver(this.state, this.passes); i++) {
            IPlayer currentPlayer = this.state.getCurrentPlayer();
            performPlayerTurn(currentPlayer);
        }
    }

    /**
     * Performs a players turn.
     * Gets a player's action and updates the game state accordingly. If the player's action is
     * invalid or the player extract turn raises an execution exception, the player is kicked out.
     */
    protected void performPlayerTurn(IPlayer player) {
        Optional<Action> turn;
        try {
            turn = extractTurn(this.state.getPlayerState(), player);

            if (Rulebook.validTurn(this.state, turn)) {
                executeCurrentActionAndTurnNextPlayer(turn, player);
            } else {
                kickCurrentPlayer();
            }

        } catch (ExecutionException e) {
            kickCurrentPlayer();
        }
    }

    /**
     * Executes the given action, then checks if that has caused a win condition. If not, sets the
     * next active player through the state.
     *
     * @param action The action to be executed.
     * @param player the current player.
     */
    private void executeCurrentActionAndTurnNextPlayer(Optional<Action> action, IPlayer player) {
        executeCurrentAction(action, player);
        if (!Rulebook.roundOver(this.state, this.passes)) {
            this.state = this.state.setNextPlayer();
        }
    }

    /**
     * Executes a players action on the current referee state or accumulates passes if the action is empty.
     * Notifies a player their next target tile if the execution of the move has given them a new target coordinate.
     *
     * @param action The action to be executed.
     * @param player the current player.
     */
    private void executeCurrentAction(Optional<Action> action, IPlayer player) {
        if (action.isPresent()) {
            Coordinate beforeTarget = this.state.currentTargetTile();
            boolean goingHomeBefore = this.state.getCurrentPlayerData().isGoingHome();

            this.state = this.state.performMove(action.get());

            if (targetTileChange(beforeTarget, goingHomeBefore)) {
                Coordinate targetTile = this.state.getCurrentPlayerData().getTargetTile();
                notifyPlayerSetup(player, Optional.empty(), targetTile);
            }

        } else {
            this.passes++;
        }
    }

    /**
     * Checks if the current player has a new target tile.
     *
     * @param beforeTarget    the target tile before the move.
     * @param goingHomeBefore whether the player was going home before the move.
     * @return true if the player has a new target tile.
     */
    private boolean targetTileChange(Coordinate beforeTarget, boolean goingHomeBefore) {
        boolean goingHomeAfter = this.state.getCurrentPlayerData().isGoingHome();
        Coordinate afterTarget = this.state.currentTargetTile();

        return !afterTarget.equals(beforeTarget) || goingHomeBefore != goingHomeAfter;
    }

    /**
     * Informs all players whether they've won or lost according to the Rulebook
     * and returns a list of the winning and losing players.
     * Players who misbehave when informed of a win will not be included in the returned winner list.
     *
     * @return a pair of lists of players, the first list contains the winners, the second list
     * contains the kicked players.
     */
    protected Pair<List<IPlayer>, List<IPlayer>> endGame() {
        List<IPlayer> winningCandidates = Rulebook.getWinningCandidates(this.state);
        notifyAllPlayerEnding(winningCandidates);
        List<IPlayer> actualWinners = new ArrayList<>();

        for (IPlayer currentPlayer : winningCandidates) {
            if (!this.listMisbehaved.contains(currentPlayer)) {
                actualWinners.add(currentPlayer);
            }
        }
        return new Pair<>(actualWinners, this.listMisbehaved);
    }

    /**
     * Wraps the method of a player taking a turn into a Callable object for Future execution task.
     */
    private Optional<Action> extractTurn(IPlayerState<PublicPlayerData> state, IPlayer player)
            throws ExecutionException {
        Callable<Optional<Action>> getAction = () -> player.takeTurn(state);
        return queryFromPlayer(getAction);
    }

    /**
     * Wraps the method of player setup into a Runnable Object for Future execution task.
     */
    private void notifyPlayerSetup(IPlayer player, Optional<IPlayerState> state,
                                   Coordinate goalTile) {
        Runnable notifyPlayerSetUp = () -> player.setup(state, goalTile);

        notifyPlayer(notifyPlayerSetUp, player);
    }

    /**
     * Notifies non-kicked players in game state if they belong in the winning list.
     *
     * @param winningCandidates the list of players who are winning candidates.
     */
    private void notifyAllPlayerEnding(List<IPlayer> winningCandidates) {
        List<PrivatePlayerData> playerDataList = this.state.getListOfPlayers();

        for (PrivatePlayerData playerData : playerDataList) {
            IPlayer currentPlayer = playerData.getPlayer().get();
            notifyPlayerEnding(currentPlayer, winningCandidates.contains(currentPlayer));
        }
    }

    /**
     * Wraps the method of player won into a Runnable Object for Future execution task.
     */
    private void notifyPlayerEnding(IPlayer player, boolean win) {
        Runnable notifyPlayerEnding = () -> player.win(win);

        notifyPlayer(notifyPlayerEnding, player);
    }

    /**
     * Performs asynchronous action on IPlayer. Failure of execution results in kicking out the
     * current player.
     */
    private void notifyPlayer(Runnable info, IPlayer player) {
        Future future = executorService.submit(info);

        try {
            future.get(TIME_OUT, TIME_UNIT);
        } catch (Exception e) {
            kickPlayer(player);
        } finally {
            future.cancel(true);
        }
    }

    /**
     * Performs asynchronous action on IPlayer, Failure of execution results in kicking out the
     * current player.
     *
     * @return response from the function call
     */
    private <T> T queryFromPlayer(Callable<T> query) throws ExecutionException {
        T result;
        Future<T> future = executorService.submit(query);

        try {
            result = future.get(TIME_OUT, TIME_UNIT);
        } catch (Exception e) {
            throw new ExecutionException(e);
        } finally {
            future.cancel(true);
        }

        return result;
    }

    /**
     * Kicks out the CURRENT player from the game state and adds them to list of misbehaving players.
     */
    private void kickCurrentPlayer() {
        IPlayer currentPlayer = this.state.getCurrentPlayer();
        this.state = this.state.kickOutCurrentPlayer();
        this.listMisbehaved.add(currentPlayer);
    }

    /**
     * Kicks out the GIVEN player from the game state and adds them to list of misbehaving players.
     */
    private void kickPlayer(IPlayer player) {
        this.state = this.state.kickOutPlayer(player);
        this.listMisbehaved.add(player);
    }
}
