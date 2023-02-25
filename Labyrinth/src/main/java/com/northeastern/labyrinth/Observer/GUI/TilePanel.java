package com.northeastern.labyrinth.Observer.GUI;

import com.northeastern.labyrinth.Model.Board.Tile;
import com.northeastern.labyrinth.Util.ColorHandler;
import com.northeastern.labyrinth.Util.Direction;
import com.northeastern.labyrinth.Util.Gem;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * Represents a tile as a JPanel. Draws connectors and Gems
 */
public class TilePanel extends JPanel {
    private final Tile tile;

    public TilePanel(Tile tile) {
        this.tile = tile;
        initializePanel();
    }

    /**
     * Sets basic Java Swing settings for JPanel
     */
    private void initializePanel() {
        this.setPreferredSize(new Dimension(100, 100));
        this.setBackground(ColorHandler.GUI_BACKGROUND_COLOR);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    }

    /**
     * Draws connectors and Gems
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(20));

        Map<Direction, Boolean> tileConnectors = tile.getConnections();

        if (tileConnectors.get(Direction.UP)) {
            g2.drawLine(50, 50, 50, 0);
        }
        if (tileConnectors.get(Direction.DOWN)) {
            g2.drawLine(50, 50, 50, 100);
        }
        if (tileConnectors.get(Direction.RIGHT)) {
            g2.drawLine(50, 50, 100, 50);
        }
        if (tileConnectors.get(Direction.LEFT)) {
            g2.drawLine(50, 50, 0, 50);
        }

        g2.setStroke(new BasicStroke(3));

        drawGem(g, this.tile.getTreasure()[0], 70, 5);
        drawGem(g, this.tile.getTreasure()[1], 5, 70);
    }

    /**
     * Draws a gem on the tile at given location relative to top left of tile
     */
    private void drawGem(Graphics g, Gem gem, int x, int y) {
        String path1 = "/gems/" + Gem.enumToFileName(gem);

        BufferedImage img;
        try {
            img = ImageIO.read(getClass().getResource(path1));
        } catch (IOException e) {
            System.out.println("Gem image: " + Gem.enumToFileName(gem) + " failed to load!");
            return;
        }

        g.drawImage(img, x, y, 25, 25, null);
    }
}
