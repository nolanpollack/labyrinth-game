package com.northeastern.labyrinth.Observer.GUI;

import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Util.ColorHandler;

import javax.swing.*;
import java.awt.*;

import static com.northeastern.labyrinth.Util.ColorHandler.colorToString;

/**
 * JPanel that displays current avatar and previous slide information about the Gamestate
 */
public class InformationPanel extends JPanel {
    public InformationPanel(IRefereeState state) {
        initializeSelf();
        initializeJLabels(state);
    }

    private void initializeSelf() {
        this.setBackground(ColorHandler.GUI_BACKGROUND_COLOR);
        this.setLayout(new GridLayout(3, 1));
    }

    private void initializeJLabels(IRefereeState state) {
        String currentPlayerAvatar = colorToString(state.getCurrentPlayerData().getAvatarColor());
        String lastSlide = state.getPreviousSlide().toString();

        this.add(new JLabel("Current Player Avatar: " + currentPlayerAvatar));
        this.add(new JLabel("Previous Slide: " + lastSlide));
    }
}
