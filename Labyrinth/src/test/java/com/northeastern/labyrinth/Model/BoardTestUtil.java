package com.northeastern.labyrinth.Model;

import com.northeastern.labyrinth.Model.Board.Tile;

/**
 * Utility class for testing Maze Board.
 */
public class BoardTestUtil {
    // │, └, ┬, ┼
    // Assume this to be default
    public static Tile create_l() {
        Tile tile = new Tile('│', "beryl", "beryl");
        return tile;
    }

    public static Tile create_L() {
        Tile tile = new Tile('└', "beryl", "amethyst");
        return tile;
    }

    public static Tile create_T() {
        Tile tile = new Tile('┬', "beryl", "aplite");
        return tile;
    }

    public static Tile create_PLUS() {
        Tile tile = new Tile('┼', "beryl", "carnelian");
        return tile;
    }

    /**
     * Creates this board
     * ┌ ┐ ─
     * ┌ ┼ ─
     * ┤ ┴ ┘
     */
    public static Tile[][] create_SampleBoard() {
        Tile[][] board = new Tile[3][3];
        board[0][0] = new Tile('┌', "alexandrite", "alexandrite");
        board[0][1] = new Tile('┐', "alexandrite", "amethyst");
        board[0][2] = new Tile('─', "alexandrite", "ametrine");
        board[1][0] = new Tile('┌', "alexandrite", "ammolite");
        board[1][1] = new Tile('┼', "alexandrite", "apatite");
        board[1][2] = new Tile('─', "alexandrite", "aplite");
        board[2][0] = new Tile('┤', "alexandrite", "azurite");
        board[2][1] = new Tile('┴', "alexandrite", "beryl");
        board[2][2] = new Tile('┘', "alexandrite", "carnelian");
        return board;
    }

    public static Tile[][] create_SampleBoard_7x7(char[][] connectors) {
        Tile[][] board = new Tile[7][7];
        String[] gems1 = new String[] {
                "alexandrite",
                "amethyst",
                "ametrine",
                "ammolite",
                "apatite",
                "aplite",
                "azurite",
        };
        String[] gems2 = new String[] {
                "beryl",
                "carnelian",
                "chrysolite",
                "citrine",
                "diamond",
                "grandidierite",
                "hackmanite"
        };

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = new Tile(connectors[i][j], gems1[i], gems2[j]);
            }
        }

        return board;
    }

    public static Tile[][] create_Rectangle_9x7(char[][] connectors) {
        Tile[][] board = new Tile[connectors.length][connectors[0].length];
        String[] gems1 = new String[] {
                "alexandrite",
                "amethyst",
                "ametrine",
                "ammolite",
                "apatite",
                "aplite",
                "azurite",
                "carnelian",
                "chrysolite"
        };
        String[] gems2 = new String[] {
                "beryl",
                "carnelian",
                "chrysolite",
                "citrine",
                "diamond",
                "grandidierite",
                "hackmanite",
                "carnelian",
                "chrysolite"
        };

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = new Tile(connectors[i][j], gems1[i], gems2[j]);
            }
        }

        return board;
    }



}
