package items;

public enum Items {
    AIR(null), STONE(BlockType.STONE), DIRT(BlockType.DIRT), GRASS(BlockType.GRASS), WOOD_PLANKS(BlockType.WOOD_PLANKS),
    LOG(BlockType.LOG), LEAVES(BlockType.LEAVES), BOMB(ThrowableType.BOMB), CONCRETE(BlockType.CONCRETE), EYEDROPPER(ToolType.EYEDROPPER),
    FILL_GRENADE(ThrowableType.FILL_GRENADE), PAINTBRUSH(ToolType.PAINTBRUSH), INFO_TOOL(ToolType.INFO_TOOL);

    private final ItemType itemType;

    private Items(ItemType itemType) {
        this.itemType = itemType;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public static ItemType getItemType(String input) {
        if (input == null || input.isEmpty()) return null;

        if (input.length() <= 9 && input.chars().allMatch(Character::isDigit)) {
            return getItemType(Integer.parseInt(input));
        }

        for (Items item : values()) {
            if (item.getItemType() == null) continue;
            String itemN = item.getItemType().getDisplayName().replace(' ', '_');

            if (input.equalsIgnoreCase(itemN)) {
                return item.getItemType();
            }

        }

        return null;
    }

    public static ItemType getItemType(int ID) {
        for (Items item : values()) {
            if (item.getItemType() == null) continue;
            int itemID = item.getItemType().getID();

            if (itemID == ID) {
                return item.getItemType();
            }

        }

        return null;
    }
}
