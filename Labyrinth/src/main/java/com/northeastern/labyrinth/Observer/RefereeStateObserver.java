package com.northeastern.labyrinth.Observer;

import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Observer.GUI.MazeGUI;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A com.northeastern.labyrinth.Observer for the referee state. Does NOT track other referee knowledge such as misbehaved players.
 */
public class RefereeStateObserver implements IObserver {
    private final Queue<IRefereeState> states;
    private boolean finishedCollecting;

    public RefereeStateObserver() {
        this.finishedCollecting = false;
        this.states = new LinkedList();
    }

    /**
     * Feeds a new referee state to observer
     */
    public void update(IRefereeState state) {
        if (finishedCollecting) {
            throw new IllegalStateException("com.northeastern.labyrinth.Observer no longer accepting referee states!");
        }

        this.states.add(state);
    }

    /**
     * Prevents observer from accepting further referee states. Returns if a GUI is generated
     */
    public boolean closeUpdates() {
        if (finishedCollecting) {
            throw new IllegalStateException("com.northeastern.labyrinth.Observer already closed!");
        }

        finishedCollecting = true;

        return startGUI();
    }

    /**
     * Starts the GUI if there are states to display. Returns if a GUI is generated
     */
    private boolean startGUI() {
        if (states.size() == 0) {
            return false;
        }

        MazeGUI mazeGui = new MazeGUI(states);
        mazeGui.setVisible(true);
        return true;
    }
}
