package com.northeastern.labyrinth.Server;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Controller.Referee.IReferee;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Remote.ProxyPlayer;
import com.northeastern.labyrinth.Util.DaemonThreadFactory;
import com.northeastern.labyrinth.Util.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import static java.util.Objects.nonNull;

public class Server {

    /**
     * Time the Server will wait for Client to send their name after connection.
     * Unit: Second
     */
    private final static int NAME_WAIT_TIME = 2;
    /**
     * Time the Server will wait for TCP sign-ups in one round.
     * Unit: Second
     */
    private final static int WAITING_PERIOD = 20;
    private final static int MAX_PLAYER_SIZE = 6;
    private final static int MIN_PLAYER_SIZE = 2;

    private final int portNumber;

    private ExecutorService executorService;
    private ServerSocket server;
    private List<IPlayer> playerList;
    private Set<Socket> clientConnections;

    /**
     * Construct a Server with given port number.
     *
     * @param port the given port number.
     */
    public Server(int port) {
        this.portNumber = port;
    }

    /**
     * Connect the clients and then let referee run games.
     *
     * @param referee      the game referee.
     * @param initialState the initial state that will be passed to the referee to start the game.
     * @return the game result where the first list in pair is the list of winners and the second list
     * in pair is the list of misbehaved players.
     */
    public Pair<List<IPlayer>, List<IPlayer>> runServer(IReferee referee,
                                                        Optional<IRefereeState> initialState) {
        try {
            startServer();
            return connectPlayersAndRunGame(referee, initialState);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            shutDownServer();
        }
    }

    /**
     * Initialize the server and set the server accept time out.
     */
    private void startServer() {
        try {
            this.server = new ServerSocket(this.portNumber);
            this.server.setSoTimeout(1000);
            this.playerList = new ArrayList<>();
            this.clientConnections = new HashSet<>();
            this.executorService = Executors.newCachedThreadPool(new DaemonThreadFactory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Shutdown the server and close all the client connections.
     */
    private void shutDownServer() {
        closeAllClientsConnection();
        this.executorService.shutdownNow();
        try {
            this.server.close();
        } catch (IOException e) {
            // Close silently
        }
    }

    /**
     * Connects players and lets the referee run the game to completion. Checks Optional field of a IRefereeState
     * for given starting state. Returns empty list if player connection requirements are not met.
     *
     * @return Pair of winner and misbehaved list from game task.
     */
    private Pair<List<IPlayer>, List<IPlayer>> connectPlayersAndRunGame(IReferee referee,
                                                                        Optional<IRefereeState> initialState) {
        if (handlePlayerConnection()) {

            if (initialState.isPresent()) {
                return referee.runGame(initialState.get(), this.playerList);
            } else {
                return referee.runGame(this.playerList);
            }
        } else {
            return new Pair<>(new ArrayList<>(), new ArrayList<>());
        }
    }

    /**
     * Handles multiple waiting periods for client connection.
     * Reverses final player list, Last player connected is first to take turn in game.
     *
     * @return boolean flag to alert that client connections have met the game size requirements
     */
    private boolean handlePlayerConnection() {
        waitingPeriod();
        if (this.playerList.size() < MIN_PLAYER_SIZE) {
            waitingPeriod();
        }
        if (this.playerList.size() < MIN_PLAYER_SIZE) {
            return false;
        }
        Collections.reverse(this.playerList);
        return true;
    }

    /**
     * The waiting sequence where the server is accepting clients Validates a player response time and
     * string name.
     */
    private void waitingPeriod() {
        long end = System.currentTimeMillis() + WAITING_PERIOD * 1000;

        while (System.currentTimeMillis() < end && this.playerList.size() < MAX_PLAYER_SIZE) {
            Socket socket = null;
            try {
                socket = this.server.accept();
                ProxyPlayer player = getProxyPlayer(socket);
                this.playerList.add(player);
                this.clientConnections.add(socket);
            } catch (Exception e) {
                closeClientConnection(socket);
            }
        }
    }

    /**
     * Bundles the creation of a proxy player into an executable call, in order to safely handle
     * timeout.
     */
    private ProxyPlayer getProxyPlayer(Socket socket) throws ExecutionException {
        Callable<ProxyPlayer> createProxyPlayer = new Callable<ProxyPlayer>() {
            @Override
            public ProxyPlayer call() throws Exception {
                ProxyPlayer player = new ProxyPlayer(socket.getInputStream(), socket.getOutputStream());
                validPlayerName(player);
                return player;
            }
        };

        return runSafely(createProxyPlayer, NAME_WAIT_TIME);
    }

    /**
     * Must be string of at least one and at most 20 alpha-numeric characters, i.e., it also matches
     * the regular expression "^[a-zA-Z0-9]+$".
     */
    private void validPlayerName(ProxyPlayer player) {
        String name = player.name();
        if (name == null || name.length() == 0 || name.length() > 20 || !name.matches(
                "^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Invalid Player Name");
        }
    }

    /**
     * Close all the client connections.
     */
    private void closeAllClientsConnection() {
        for (Socket socket : this.clientConnections) {
            closeClientConnection(socket);
        }
    }

    /**
     * Close the given client connection.
     */
    private void closeClientConnection(Socket socket) {
        try {
            if (nonNull(socket)) {
                socket.close();
            }
        } catch (IOException e) {
            // Close silently
        }
    }

    /**
     * Performs asynchronous task, Catches time out response.
     *
     * @return response from the function call
     */
    private <T> T runSafely(Callable<T> query, int timeout) throws ExecutionException {
        T result;
        Future<T> future = executorService.submit(query);
        try {
            result = future.get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new ExecutionException(e);
        } finally {
            future.cancel(true);
        }
        return result;
    }
}




