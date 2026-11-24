package items;

import core.*;

import java.awt.image.*;

public enum BlockType implements ItemType {
    AIR("Air", -1), STONE("Stone", 0), DIRT("Dirt", 1), GRASS("Grass", 2),
    WOOD_PLANKS("Wood Planks", 3), LOG("Log", 4), LEAVES("Leaves", 5), CONCRETE("Concrete", 7);

    public static final int MAX_STACK_SIZE = 64;

    private final String displayName;
    private final int ID;
    private final BufferedImage sprite;

    BlockType(String displayName, int ID) {
        this.displayName = displayName;
        this.ID = ID;

        sprite = GamePanel.getSprite(ID);
    }

    @Override
    public BufferedImage getSprite() {
        return sprite;
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
}
