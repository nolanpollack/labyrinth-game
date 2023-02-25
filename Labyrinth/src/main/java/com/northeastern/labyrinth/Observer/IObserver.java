package com.northeastern.labyrinth.Observer;

import com.northeastern.labyrinth.Model.GameState.IRefereeState;

public interface IObserver {

    void update(IRefereeState state);

    boolean closeUpdates();


}
