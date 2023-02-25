package com.northeastern.labyrinth.json.Adapter;

import com.northeastern.labyrinth.Controller.Player.AIPlayer;
import com.northeastern.labyrinth.Controller.Player.IPlayer;
import com.northeastern.labyrinth.Model.GameState.IPlayerState;
import com.northeastern.labyrinth.Model.GameState.PublicPlayerData;
import com.northeastern.labyrinth.Strategy.IStrategy;
import com.northeastern.labyrinth.Util.Coordinate;
import com.northeastern.labyrinth.Util.Turn.Action;

import java.util.Optional;

public class BadPlayerFactory {

    /**
     * Create a bad player that raise the Arithmetic Exception on either the
     * {@link IPlayer#setup(Optional, Coordinate)}, {@link IPlayer#takeTurn(IPlayerState)}, or
     * {@link IPlayer#win(Boolean)} method.
     *
     * @param name     the name of the player.
     * @param strategy the player's strategy.
     * @param bad      one of "setUp", "takeTurn", "win".
     */
    public static IPlayer createExceptionPlayer(String name, IStrategy strategy, String bad) {
        switch (bad) {
            case "setUp":
                return new AIPlayer(name, strategy) {
                    @Override
                    public void setup(Optional<IPlayerState> state0, Coordinate targetTile) {
                        int i = 1 / 0;
                    }
                };
            case "takeTurn":
                return new AIPlayer(name, strategy) {
                    @Override
                    public Optional<Action> takeTurn(IPlayerState<PublicPlayerData> publicState) {
                        int i = 1 / 0;
                        return null;
                    }
                };
            case "win":
                return new AIPlayer(name, strategy) {
                    @Override
                    public void win(Boolean w) {
                        int i = 1 / 0;
                    }
                };
            default:
                throw new IllegalArgumentException("I don't understand this bad");
        }
    }

    /**
     * Create a bad player that goes into an infinite loop when call on the method specific by bad on
     * and after count times.
     *
     * @param name     the name of the bad player.
     * @param strategy the player's strategy.
     * @param bad      one of "setUp", "takeTurn", "win".
     * @param count    the method specific by bad will go into an infinite loop on and after count
     *                 times.
     */
    public static IPlayer createLoopPlayer(String name, IStrategy strategy, String bad,
                                           int count) {
        switch (bad) {
            case "setUp":
                return new AIPlayer(name, strategy) {
                    private int counter = 0;

                    @Override
                    public void setup(Optional<IPlayerState> state0, Coordinate targetTile) {
                        if (++counter >= count) {
                            while (true) {
                            }
                        } else {
                            super.setup(state0, targetTile);
                        }
                    }
                };
            case "takeTurn":
                return new AIPlayer(name, strategy) {
                    private int counter = 0;

                    @Override
                    public Optional<Action> takeTurn(IPlayerState<PublicPlayerData> publicState) {
                        if (++counter >= count) {
                            while (true) {
                            }
                        } else {
                            return super.takeTurn(publicState);
                        }
                    }
                };
            case "win":
                return new AIPlayer(name, strategy) {
                    private int counter = 0;

                    @Override
                    public void win(Boolean w) {
                        if (++counter >= count) {
                            while (true) {
                            }
                        } else {
                            super.win(w);
                        }
                    }
                };
            default:
                throw new IllegalArgumentException("I don't understand this bad");
        }
    }
}
