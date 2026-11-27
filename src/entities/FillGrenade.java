package entities;

import items.*;
import world.*;

public class FillGrenade extends Throwable {

    private static final int FUSE_DURATION = 80;
    private static final int EXPLOSION_SIZE = 1;

    public static BlockType selectedBlockType = BlockType.AIR;
    private int fuseTimer;

    public FillGrenade(int x, int y, World world) {
        super(ThrowableType.FILL_GRENADE, x - SIZE / 2, y - SIZE / 2, world);
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
                    world.setBlockAt(selectedBlockType, r, c);
                }

            }
        }
    }
}
