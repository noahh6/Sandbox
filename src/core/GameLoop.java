package core;

import entities.Player;
import world.World;

public class GameLoop implements Runnable{

    private Thread gameThread;
    private boolean running = false;

    // Target 60 updates per second
    private final double GAME_HERTZ = 60.0;
    private final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;

    // Simple performance tracking
    private int fps = 0;
    private int ups = 0;

    public synchronized void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this, "GameLoopThread");
        gameThread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;

        long lastTimer = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / TIME_BETWEEN_UPDATES;
            lastTime = now;

            // Catch up on game logic if the loop falls behind
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }

            frames++;

            // Performance tracking: prints FPS and UPS once per second
            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                fps = frames;
                ups = updates;
                System.out.println("FPS: " + fps + " | UPS: " + ups);
                frames = 0;
                updates = 0;
            }

            // Optional: Reduce CPU stress by offering a tiny pause
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private final GamePanel gp;
    private final Player player;
    private final World world;

    public GameLoop(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
        world = player.getWorld();
    }

    private void update() {
        world.updateWorldState();
        player.update();
        gp.repaint();
    }
}
