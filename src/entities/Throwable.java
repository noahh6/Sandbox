package entities;

import items.*;
import world.*;

public abstract class Throwable implements Item {

    private final ItemType throwableType;
    int x, y;
    boolean isFalling, isActive;

    public Throwable(ThrowableType throwableType, int x, int y) {
        this.throwableType = throwableType;
        this.x = x;
        this.y = y;
        isFalling = true;
        isActive = true;
    }

    @Override
    public ItemType getItemType() {
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

    abstract void checkCollision();

    abstract void updateState();
}
