package world;

import items.*;

import java.awt.*;

public class Block implements Item {

    private int x, y;

    private BlockType blockType;
    private final Rectangle hitBox;

    public Block(BlockType blockType, int row, int col) {
        this.blockType = blockType;

        x = col * SIZE;
        y = row * SIZE;

        hitBox = new Rectangle(x, y, SIZE, SIZE);
    }

    public void setBlockType(BlockType type) {
        this.blockType = type;
    }

    public Rectangle getHitbox() {
        return hitBox;
    }

    @Override
    public ItemType getItemType() {
        return blockType;
    }

    public void applyGravity() {
        y += World.GRAVITY;

        hitBox.setBounds(x, y, SIZE, SIZE);
    }
}
