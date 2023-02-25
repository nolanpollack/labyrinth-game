package com.northeastern.labyrinth.Observer.GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Model.GameState.RefereeState;
import com.northeastern.labyrinth.Util.ColorHandler;
import com.northeastern.labyrinth.json.Adapter.RefereeStateAdapter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Queue;

/**
 * Master class of the GUI. Displays information about a refereestate and provides a button to move
 * to next gamestate, as well as save a current gamestate as a json
 */
public class MazeGUI extends JFrame {

    private final static Gson gson = new GsonBuilder().registerTypeAdapter(RefereeState.class,
            new RefereeStateAdapter()).create();
    private final Queue<IRefereeState> states;

    private final GameStatePanel gameStatePanel;
    private final ButtonPanel buttonPanel;

    public MazeGUI(Queue<IRefereeState> states) {
        this.states = states;

        initializeSelf();

        this.buttonPanel = new ButtonPanel(new GUIButtonListener(this));
        this.gameStatePanel = new GameStatePanel(currentState());

        this.add(gameStatePanel);
        this.add(buttonPanel);
    }

    private void initializeSelf() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Maze!");
        this.setSize(1000, 825);
        this.getContentPane().setBackground(ColorHandler.GUI_BACKGROUND_COLOR);
        this.setLayout(new FlowLayout());
    }

    /**
     * Triggers when the "Next" button is pressed, moving to the next gamestate. If this move goes to
     * the final gamestate in the queue, removes the "Next" button.
     */
    public void nextButtonPressed() {
        this.states.poll();
        this.gameStatePanel.loadNewGameState(currentState());
        this.gameStatePanel.revalidate();
        this.gameStatePanel.repaint();

        if (this.states.size() == 1) {
            this.buttonPanel.removeNextButton();
            this.buttonPanel.revalidate();
            this.buttonPanel.repaint();
        }
    }

    /**
     * Prompts the user to select a directory to save the gamestate as json Creates a file in chosen
     * location
     */
    public void saveButtonPressed() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle("Specify a location to save Gamestate to");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("json files", "json");
        fileChooser.setFileFilter(filter);

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            try {
                Writer fileWriter = new FileWriter(filePath);
                fileWriter.write(gson.toJson(currentState()));
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                System.out.println("Save failed!");
            }
        }
    }

    private IRefereeState currentState() {
        return states.peek();
    }

}
