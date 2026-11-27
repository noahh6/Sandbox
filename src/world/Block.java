package world;

import items.*;

import java.awt.*;

public class Block implements Item {

    private BlockType blockType;
    private final Rectangle hitBox;

    public Block(BlockType blockType, int row, int col) {
        this.blockType = blockType;

        int x = col * SIZE;
        int y = row * SIZE;

        hitBox = new Rectangle(x, y, SIZE, SIZE);
    }

    public void setBlockType(BlockType type) {
        this.blockType = type;
    }

    public Rectangle getHitbox() {
        return hitBox;
    }

    @Override
    public BlockType getItemType() {
        return blockType;
    }
}
