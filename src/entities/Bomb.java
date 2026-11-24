package entities;

import items.*;
import world.*;

import java.awt.*;

public class Bomb extends Throwable {

    private static final int SIZE = Item.SIZE;
    private static final int FUSE_DURATION = 100;
    private static final int EXPLOSION_SIZE = 5;

    private final World world;
    private final Rectangle hitbox;

    private int fuseTimer;

    public Bomb(World world, int x, int y) {
        super(ThrowableType.BOMB, x - SIZE / 2, y - SIZE);
        this.world = world;
        fuseTimer = 0;
        hitbox = new Rectangle(x, y, SIZE, SIZE);
        isFalling = true;
    }

    @Override
    void updateState() {
        fuseTimer++;

        if (fuseTimer >= FUSE_DURATION) {
            explode();
            isActive = false;
        }
    }

    @Override
    void checkCollision() {
        hitbox.setBounds(x, y, SIZE, SIZE);

        int targetRow = (y + SIZE) / Block.SIZE;
        int targetCol = (x + SIZE / 2) / Block.SIZE;

        for (int c = targetCol - 1; c < targetCol + 2; c++) {
            Block targetBlock = world.getBlockAt(targetRow, c);

            if (targetBlock == null) continue;

            if (hitbox.intersects(targetBlock.getHitbox()) && targetBlock.getItemType() != BlockType.AIR) {
                isFalling = false;

                // Snap y to grid
                y = World.snapToGrid(y);
            }
            else {
                isFalling = true;
            }
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
