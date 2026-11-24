package items;

public class ItemStack {

    private final ItemType itemType;

    private int stackSize;

    public ItemStack(ItemType itemType, int stackSize) {
        this.itemType = itemType;
        int maxStackSize = itemType.getMaxStackSize();
        // Prevent item stack going over max stack size
        this.stackSize = Math.min(stackSize, maxStackSize);

    }

    public int getStackSize() {
        return stackSize;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public boolean reduceStack() {
        if  (stackSize >= 1) {
            stackSize--;
            return true;
        }
        return false;
    }
}
