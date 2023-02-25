package com.northeastern.labyrinth.Server;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Remote.ProxyReferee;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import static java.util.Objects.nonNull;

public class Client {

    /**
     * If the Client fails to connect to the Server, it will wait {@link Client#WAITING_PERIOD}
     * seconds and try to reconnect.
     */
    private static final int WAITING_PERIOD = 2;
    /**
     * The Client will only try to connect to Server {@link  Client#MAX_ATTEMPT} times before it
     * shut down.
     */
    private static final int MAX_ATTEMPT = 10;
    private final String host;
    private final int serverPortNumber;

    /**
     * Create a Client with the given hostIP and server port number.
     */
    public Client(String host, int serverPortNumber) {
        this.host = host;
        this.serverPortNumber = serverPortNumber;
    }

    public void connectAndExecute(IPlayer player) {
        Socket socket = null;
        try {
            socket = connectToServer();
            runGame(socket, player);
        } finally {
            closeSocket(socket);
        }
    }


    /**
     * Creates a socket of communication with server. Creates a Proxy Referee for the socket and
     * IPlayer (Client). On termination calls the close socket method.
     */
    public Socket connectToServer() {
        boolean connected = false;
        int attemptNums = 0;
        Socket socket = null;
        while (!connected && attemptNums++ < MAX_ATTEMPT) {
            try {
                socket = new Socket(this.host, this.serverPortNumber);
                connected = true;
            } catch (ConnectException e) {
                waitForServerUp();
            } catch (Exception e) {
            }
        }
        return socket;
    }

    /**
     * Create a ProxyReferee based on the socket input stream and socket output stream. Call {@link ProxyReferee#runGame()}
     */
    private void runGame(Socket socket, IPlayer player) {
        try {
            ProxyReferee referee = new ProxyReferee(socket.getInputStream(), socket.getOutputStream(),
                    player);
            referee.runGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Wait {@link Client#WAITING_PERIOD} seconds.
     */
    private void waitForServerUp() {
        try {
            Thread.sleep(WAITING_PERIOD);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the socket channel.
     */
    private void closeSocket(Socket socket) {
        try {
            if (nonNull(socket)) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }
}
