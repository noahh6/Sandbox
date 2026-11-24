package items;

import world.Block;

public abstract class Tool implements Item {

    public static final int SIZE = 25;

    private final ItemType toolType;

    public Tool(ToolType toolType) {
        this.toolType = toolType;
    }

    @Override
    public ItemType getItemType() {
        return toolType;
    }

    public abstract void useTool(Block block);
}
