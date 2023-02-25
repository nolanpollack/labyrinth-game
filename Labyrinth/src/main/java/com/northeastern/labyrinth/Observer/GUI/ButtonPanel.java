package com.northeastern.labyrinth.Observer.GUI;

import com.northeastern.labyrinth.Util.ColorHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Represents the JPanel that the Next and Save buttons are contained in
 */
public class ButtonPanel extends JPanel {
    //Listener used to notify GUI when a button is pressed
    private final ActionListener listener;

    public ButtonPanel(ActionListener listener) {
        this.listener = listener;
        this.setBackground(ColorHandler.GUI_BACKGROUND_COLOR);
        initializeButton("Save");
        initializeButton("Next");
    }

    /**
     * Disables the next button by removing it from the JPanel
     */
    public void removeNextButton() {
        this.removeAll();
        initializeButton("Save");
    }

    /**
     * Initializes and adds a button with given name to the ButtonPanel
     */
    private void initializeButton(String name) {
        JButton newButton = new JButton(name);
        newButton.addActionListener(listener);
        newButton.setBackground(Color.LIGHT_GRAY);
        newButton.setPreferredSize(new Dimension(250, 35));

        this.add(newButton);
    }
}
