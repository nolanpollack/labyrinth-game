package com.northeastern.labyrinth.Controller.Referee;

import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Observer.IObserver;
import com.northeastern.labyrinth.Util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static java.util.Objects.isNull;

/**
 * Represents an Observable referee with a list of observers.
 */
public class ObservableReferee extends Referee {

    private final List<IObserver> observers;

    public ObservableReferee() {
        super();
        this.observers = new ArrayList<>();
    }

    public void addObserver(IObserver observer) {
        if (isNull(observer)) {
            throw new IllegalArgumentException("Observer cannot be null");
        }

        this.observers.add(observer);
    }

    @Override
    protected void setup(List<IPlayer> playerList) {
        super.setup(playerList);

        for (IObserver observer : this.observers) {
            addStateToObserver(observer);
        }
    }

    @Override
    protected void performPlayerTurn(IPlayer player) {
        super.performPlayerTurn(player);

        for (IObserver observer : this.observers) {
            addStateToObserver(observer);
        }
    }

    @Override
    protected Pair<List<IPlayer>, List<IPlayer>> endGame() {
        for (IObserver observer : this.observers) {
            notifyEndGame(observer);
        }

        return super.endGame();
    }

    private void addStateToObserver(IObserver observer) {
        Callable<Void> updateObserver = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                observer.update(state);
                return null;
            }
        };

        updateObserver(observer, updateObserver);
    }

    private void notifyEndGame(IObserver observer) {
        Callable<Void> notifyEndGame = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                observer.closeUpdates();
                return null;
            }
        };

        updateObserver(observer, notifyEndGame);
    }

    private void updateObserver(IObserver observer, Callable<Void> info) {
        Future<Void> future = executorService.submit(info);

        try {
            future.get(TIME_OUT, TIME_UNIT);
        } catch (Exception e) {
            this.observers.remove(observer);
        } finally {
            future.cancel(true);
        }
    }
}
