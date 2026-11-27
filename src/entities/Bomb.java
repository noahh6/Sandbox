package entities;

import items.*;
import world.*;

import java.awt.*;

public class Bomb extends Throwable {

    private static final int SIZE = Item.SIZE;
    private static final int FUSE_DURATION = 100;
    private static final int EXPLOSION_SIZE = 5;

    private int fuseTimer;

    public Bomb(int x, int y, World world) {
        super(ThrowableType.BOMB, x - SIZE / 2, y - SIZE, world);
        fuseTimer = 0;
    }

    @Override
    void updateState() {
        fuseTimer++;

        if (fuseTimer >= FUSE_DURATION) {
            explode();
            isActive = false;
        }
    }

    private void explode() {

        int row = (y + SIZE / 2) / Block.SIZE;
        int col = (x + SIZE / 2) / Block.SIZE;

        for (int r = row - EXPLOSION_SIZE; r <= row + EXPLOSION_SIZE; r++) {
            for (int c = col - EXPLOSION_SIZE; c <= col + EXPLOSION_SIZE; c++) {

                if (r < 0 || r >= World.ROWS || c < 0 || c >= World.COLS) {
                    continue;
                }

                int distance = Math.abs(row - r) + Math.abs(col - c);

                if (distance <= EXPLOSION_SIZE) {
                    world.setBlockAt(BlockType.AIR, r, c);
                }

            }
        }
    }
}
