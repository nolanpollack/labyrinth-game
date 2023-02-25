package com.northeastern.labyrinth.Observer.GUI;

import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Util.ColorHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Displays everything from the game state:
 * Board
 * Avatars, homes, and goals
 * Spare tile
 * Current Player and Previous Slide
 */
public class GameStatePanel extends JPanel {

    public GameStatePanel(IRefereeState state) {
        initializeSelf();
        loadNewGameState(state);
    }

    public void loadNewGameState(IRefereeState state) {
        this.removeAll();

        BoardPanel boardPanel = new BoardPanel(state);
        TilePanel spareTilePanel = new TilePanel(state.getSpareTile());
        InformationPanel informationPanel = new InformationPanel(state);

        this.add(boardPanel);
        this.add(spareTilePanel);
        this.add(informationPanel);
    }

    private void initializeSelf() {
        this.setPreferredSize(new Dimension(1000, 725));
        this.setBackground(ColorHandler.GUI_BACKGROUND_COLOR);
        this.setLayout(new FlowLayout());
    }
}
