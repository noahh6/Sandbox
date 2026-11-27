package entities;

import items.*;
import world.*;

import java.awt.*;

public abstract class Throwable implements Item {

    static final int SIZE = Item.SIZE;

    private final ThrowableType throwableType;
    int x, y;
    boolean isFalling, isActive;

    final World world;
    final Rectangle hitbox;

    public Throwable(ThrowableType throwableType, int x, int y, World world) {
        this.throwableType = throwableType;
        this.x = x;
        this.y = y;
        this.world = world;
        hitbox = new Rectangle(x, y, SIZE, SIZE);
        isFalling = true;
        isActive = true;
    }

    @Override
    public ThrowableType getItemType() {
        return throwableType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return isActive;
    }

    public void update() {
        updatePosition();
        updateState();
    }

    public void updatePosition() {

        if (isFalling) {
            y += World.GRAVITY;
        }
        checkCollision();
    }

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

    abstract void updateState();
}
