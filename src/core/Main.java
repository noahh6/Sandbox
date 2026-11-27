package core;

import entities.Player;
import world.World;

import javax.swing.*;

/**
 * 2D game with a player and an inventory where the player can place a variety of blocks
 */

/// TODO:
/// MAKE A STRONG README FILE
/// * Fix player getting stuck inside blocks by teleporting them out
/// * Implement paintbrush and infotool logic
/// * Display currently selected blocktype for fill grenade
/// * Implement save/loading for player position, inventory, and world state. making sure to update hitbox after loading

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