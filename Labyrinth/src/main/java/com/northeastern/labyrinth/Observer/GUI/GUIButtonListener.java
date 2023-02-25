package com.northeastern.labyrinth.Observer.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Button listener that calls the GUI when buttons are pressed
 */
public class GUIButtonListener implements ActionListener {
    private final MazeGUI gui;

    public GUIButtonListener(MazeGUI mazeGui) {
        this.gui = mazeGui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String source = e.getActionCommand();

        if (source.equals("Next")) {
            gui.nextButtonPressed();
        } else if (source.equals("Save")) {
            gui.saveButtonPressed();
        }
    }
}
