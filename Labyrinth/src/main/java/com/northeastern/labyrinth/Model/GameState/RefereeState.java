package com.northeastern.labyrinth.Model.GameState;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.Action;
import com.northeastern.labyrinth.Util.Turn.SlideAction;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Implementation of methods regarding referee's knowledge of the maze game. The referee state
 * extends the knowledge built in a player state by adding methods on private information.
 */
public final class RefereeState extends AbstractState<PrivatePlayerData> implements IRefereeState {

    Queue<Coordinate> goalTiles;

    public RefereeState(MazeBoard board, Tile spareTile, Queue<PrivatePlayerData> playerData) {
        this(board, spareTile, playerData, new SlideAction());
    }

    public RefereeState(MazeBoard board, Tile spareTile, Queue<PrivatePlayerData> playerData, SlideAction sl) {
        this(board, spareTile, playerData, sl, new ArrayDeque<>());
    }

    public RefereeState(MazeBoard board, Tile spareTile, Queue<PrivatePlayerData> playerData, Queue<Coordinate> goalTiles) {
        this(board, spareTile, playerData, new SlideAction(), goalTiles);
    }

    public RefereeState(MazeBoard board, Tile spareTile, Queue<PrivatePlayerData> playerData,
                        SlideAction previousSlideAction, Queue<Coordinate> goalTiles) {
        super(board, spareTile, playerData, previousSlideAction);
        this.goalTiles = goalTiles;
    }

    @Override
    protected boolean validPlayer(PrivatePlayerData player) {
        Coordinate currentPosition = player.getCurrentPosition();
        Coordinate homePosition = player.getHomePosition();
        Coordinate goalPosition = player.getTargetTile();
        return this.board.validLocation(currentPosition) && this.board.validLocation(homePosition)
                && this.board.validLocation(goalPosition);
    }

    @Override
    public IRefereeState rotateSpare(int degree) {
        return (IRefereeState) super.rotateSpare(degree);
    }

    @Override
    public IRefereeState insertSpare(SlideAction action) {
        return (IRefereeState) super.insertSpare(action);
    }

    @Override
    public IRefereeState performMove(Action action) {
        return this.rotateSpare(action.getRotation()).insertSpare(action.getSlideAction())
                .moveCurrentPlayer(action.getToMove());
    }

    @Override
    public Coordinate currentTargetTile() {
        PrivatePlayerData current = this.playersQueue.peek();

        return current.getTargetTile();
    }

    @Override
    public IRefereeState kickOutCurrentPlayer() {
        Queue<PrivatePlayerData> newPlayersQueue = new ArrayDeque<>(this.playersQueue);
        newPlayersQueue.poll();
        return new RefereeState(this.board, this.spareTile, newPlayersQueue, this.previousSlideAction, this.goalTiles);
    }

    @Override
    public IRefereeState kickOutPlayer(IPlayer player) {
        Queue<PrivatePlayerData> newPlayersQueue = new ArrayDeque<>(this.playersQueue);

        for (PrivatePlayerData data : this.getListOfPlayers()) {
            IPlayer currentPlayer = data.getPlayer().get();
            if (player == currentPlayer) {
                newPlayersQueue.remove(data);
            }
        }
        return new RefereeState(this.board, this.spareTile, newPlayersQueue, this.previousSlideAction, this.goalTiles);
    }

    @Override
    public IRefereeState setNextPlayer() {
        Queue<PrivatePlayerData> newPlayersQueue = new ArrayDeque<>(this.playersQueue);
        newPlayersQueue.offer(newPlayersQueue.poll());

        return new RefereeState(this.board, this.spareTile, newPlayersQueue, this.previousSlideAction, this.goalTiles);
    }

    @Override
    public int numberOfPlayers() {
        return this.playersQueue.size();
    }

    @Override
    public IRefereeState moveCurrentPlayer(Coordinate coordinate) {

        ArrayDeque<PrivatePlayerData> newPlayersQueue = new ArrayDeque<>(this.playersQueue);
        PrivatePlayerData current = newPlayersQueue.poll();

        //TODO Redundant Checking ?
        if (!current.getCurrentPosition().equals(coordinate)) {
            current = current.setCurrentPosition(coordinate);
            current = handlePlayerOutcome(current);
        }

        newPlayersQueue.addFirst(current);
        return new RefereeState(this.board, this.spareTile, newPlayersQueue, this.previousSlideAction, this.goalTiles);
    }

    /**
     * Handles the player data outcome after a move.
     * Cases:
     * A player has either landed on their target tile or home tile.
     * Player reaching target tile are either assigned a new one or assigned their home tile.
     */
    private PrivatePlayerData handlePlayerOutcome(PrivatePlayerData current) {

        if (currentPlayerReturnedHome(current)) {
            current = current.completedGame();
        } else if (currentPlayerMadeToTarget(current)) {
            if (this.goalTiles.size() != 1 || !current.getHomePosition().equals(this.goalTiles.peek())) {
                current = current.incrementTreasure();
            }
            if (this.goalTiles.isEmpty()) {
                current = current.setGoingHome();
            } else {
                Coordinate newTarget = this.goalTiles.poll();
                current = current.setTargetTile(newTarget);
            }
        }
        return current;
    }

    /**
     * Checks if the player's current position is on its home tile. Also checks if the player's target
     * is the home tile and tracking home.
     *
     * @param current the current player's data.
     */
    private boolean currentPlayerReturnedHome(PrivatePlayerData current) {
        Coordinate homeTile = current.getHomePosition();
        Coordinate currentPosition = current.getCurrentPosition();

        return homeTile.equals(currentPosition) && current.isGoingHome();
    }

    /**
     * Is the players current position also its target tile.
     *
     * @param current the current player's data.
     */
    private boolean currentPlayerMadeToTarget(PrivatePlayerData current) {
        return current.getCurrentPosition().equals(current.getTargetTile());
    }


    @Override
    public IPlayerState<PrivatePlayerData> buildState(MazeBoard board, Tile spareTile,
                                                      Queue<PrivatePlayerData> listOfPlayers, SlideAction previousSlideAction) {
        return new RefereeState(board, spareTile, listOfPlayers, previousSlideAction, this.goalTiles);
    }

    @Override
    public PrivatePlayerData getCurrentPlayerData() {
        return this.playersQueue.peek();
    }

    @Override
    public IPlayerState<PublicPlayerData> getPlayerState() {
        Queue<PublicPlayerData> publicPlayerDataQueue = new ArrayDeque<>();

        for (PrivatePlayerData privatePlayerData : this.playersQueue) {
            PublicPlayerData curPlayerData = new PublicPlayerData(privatePlayerData.getAvatarColor(),
                    privatePlayerData.getCurrentPosition(), privatePlayerData.getHomePosition());
            publicPlayerDataQueue.add(curPlayerData);
        }

        return new PlayerState(this.board, this.spareTile, publicPlayerDataQueue,
                this.previousSlideAction);
    }

    @Override
    public IRefereeState assignPlayerToPlayerData(List<IPlayer> playerList) {
        if (this.numberOfPlayers() != playerList.size()) {
            throw new IllegalArgumentException(
                    "Number of players in Game state not equal to number of connected players!");
        }

        Queue<PrivatePlayerData> playerDataQueue = new ArrayDeque<>();
        int i = 0;
        for (PrivatePlayerData playerData : this.playersQueue) {
            PrivatePlayerData newPlayerData = playerData.setPlayer(playerList.get(i++));
            playerDataQueue.add(newPlayerData);
        }
        return new RefereeState(this.board, this.spareTile, playerDataQueue, this.previousSlideAction, this.goalTiles);
    }

    @Override
    public IPlayer getCurrentPlayer() {
        if (this.getCurrentPlayerData().getPlayer().isPresent()) {
            return this.getCurrentPlayerData().getPlayer().get();
        }
        throw new IllegalArgumentException("No player has been assigned to this player data");
    }

    public Queue<Coordinate> getGoalTiles() {
        return new LinkedList<>(this.goalTiles);
    }


}
