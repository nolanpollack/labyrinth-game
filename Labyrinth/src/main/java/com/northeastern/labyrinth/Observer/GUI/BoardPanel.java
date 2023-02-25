package com.northeastern.labyrinth.Observer.GUI;

import com.northeastern.labyrinth.Model.Board.MazeBoard;
import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Model.GameState.IRefereeState;
import com.northeastern.labyrinth.Model.GameState.PrivatePlayerData;
import com.northeastern.labyrinth.Util.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Represents the JPanel that the Board (including player avatar and home tiles) are saved in
 */
public class BoardPanel extends JPanel {
    //com.northeastern.labyrinth.DataBinding.State to reference to draw board
    private final IRefereeState state;

    public BoardPanel(IRefereeState state) {
        this.state = state;

        initializeSelf();
        initializeTiles(state);
    }

    private void initializeSelf() {
        this.setPreferredSize(new Dimension(100 * 7, 100 * 7));
        this.setOpaque(false);

        MazeBoard board = state.getBoard();
        this.setLayout(new GridLayout(board.getNumRows(), board.getNumCols()));
    }

    /**
     * Draws connectors and Gems
     */
    public void paint(Graphics g) {
        super.paint(g);

        drawPlayerIcons(state, g);
    }

    /**
     * Draws the player icons (home, current, goal) onto the board
     * Note that goal tile is always represented as the original goal tile
     */
    private void drawPlayerIcons(IRefereeState state, Graphics g) {
        List<PrivatePlayerData> playerData = state.getListOfPlayers();

        for (PrivatePlayerData data : playerData) {
            Color playerColor = data.getAvatarColor();
            drawPlayerIcon(data.getHomePosition(), g, playerColor, IconType.HOME);
            drawPlayerIcon(data.getCurrentPosition(), g, playerColor, IconType.CURRENT);
            drawPlayerIcon(data.getTargetTile(), g, playerColor, IconType.GOAL);
        }

    }

    /**
     * Draws a player icon on a tile location
     */
    private void drawPlayerIcon(Coordinate coordinate, Graphics g, Color color, IconType iconType) {
        int x = coordinate.getCol() * 100;
        int y = coordinate.getRow() * 100;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);

        switch (iconType) {
            case HOME:
                g2.setStroke(new BasicStroke(3));
                g2.fillRect(x + 65, y + 65, 32, 32);
                break;
            case CURRENT:
                g2.setStroke(new BasicStroke(3));
                g2.fillOval(x + 5, y + 5, 40, 40);
                break;
            case GOAL:
                g2.setStroke(new BasicStroke(6));
                g2.drawLine(x + 35, y + 35, x + 65, y + 65);
                g2.drawLine(x + 65, y + 35, x + 35, y + 65);
                break;
        }
    }

    private enum IconType {
        HOME,
        CURRENT,
        GOAL
    }

    /**
     * Adds tiles to BoardPanel based on state
     */
    private void initializeTiles(IRefereeState state) {
        MazeBoard board = state.getBoard();

        int numRows = board.getNumRows();
        int numCols = board.getNumCols();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Tile tileToAdd = board.getTileAtLocation(new Coordinate(i, j));
                this.add(new TilePanel(tileToAdd));
            }
        }
    }
}
