package items;

public enum Items {
    AIR(null), STONE(BlockType.STONE), DIRT(BlockType.DIRT), GRASS(BlockType.GRASS), WOOD_PLANKS(BlockType.WOOD_PLANKS),
    LOG(BlockType.LOG), LEAVES(BlockType.LEAVES), BOMB(ThrowableType.BOMB), CONCRETE(BlockType.CONCRETE), EYEDROPPER(ToolType.EYEDROPPER);

    private final ItemType itemType;

    private Items(ItemType itemType) {
        this.itemType = itemType;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
