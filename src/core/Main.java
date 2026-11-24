package core;

import entities.Player;
import world.World;

import javax.swing.*;

/**
 * 2D game with a player and an inventory where the player can place a variety of blocks
 */

public class Main {

    private static void start() {
        JFrame frame = new JFrame("Sandbox");

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Player player = new Player(new World());
        GamePanel panel = new GamePanel(player);
        GameLoop loop = new GameLoop(panel, player);

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loop.start();
        panel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        start();
    }
}