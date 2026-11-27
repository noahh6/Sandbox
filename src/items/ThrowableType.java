package items;

import core.*;
import entities.*;
import entities.Throwable;
import world.*;

import java.awt.image.*;

public enum ThrowableType implements ItemType {
    BOMB("Bomb", 6, Bomb::new), FILL_GRENADE("Fill Grenade", 9, FillGrenade::new);

    public static final int MAX_STACK_SIZE = 64;

    private final String displayName;
    private final int ID;
    private final ThrowableFactory factory;
    private final BufferedImage sprite;

    ThrowableType(String displayName, int ID, ThrowableFactory factory) {
        this.displayName = displayName;
        this.ID = ID;
        this.factory = factory;

        sprite = GamePanel.getSprite(ID);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getMaxStackSize() {
        return MAX_STACK_SIZE;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public BufferedImage getSprite() {
        return sprite;
    }

    public Throwable createThrowable(World world, int x, int y) {
        return factory == null ? null : factory.create(x, y, world);
    }

    @FunctionalInterface
    public interface ThrowableFactory {
        Throwable create(int x, int y, World world);
    }
}
