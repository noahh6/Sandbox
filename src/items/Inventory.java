package items;

import entities.*;

public class Inventory {

    public static final int INVENTORY_SIZE = 9;

    private int selectedSlot;

    private final ItemStack[] inventory;

    public Inventory() {
        inventory = new ItemStack[INVENTORY_SIZE];
        selectedSlot = 0;

        setBasicInventory();
    }

    private void setBasicInventory() {
        inventory[0] = new ItemStack(BlockType.CONCRETE, 64);
        inventory[1] = new ItemStack(BlockType.WOOD_PLANKS, 64);
        inventory[2] = new ItemStack(BlockType.LOG, 64);
        inventory[3] = new ItemStack(BlockType.LEAVES, 64);
        inventory[4] = new ItemStack(BlockType.STONE, 64);
        inventory[5] = new ItemStack(ThrowableType.BOMB, 64);
        inventory[8] = new ItemStack(ToolType.EYEDROPPER, 1);
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public ItemStack getItemStackAt(int i) {
        if (i >= INVENTORY_SIZE || i < 0) return null;
        return inventory[i];
    }

    public void removeItemStack(int index) {
        if (index >= INVENTORY_SIZE || index < 0) return;

        inventory[index] = null;
    }

    public void setItemStack(int index, ItemType itemType, int stackCount) {
        if (index >= INVENTORY_SIZE || index < 0 || itemType == null || itemType == BlockType.AIR) return;
        inventory[index] = new ItemStack(itemType, stackCount);
    }

    public void clearEmptyStacks() {
        // Remove any ItemStacks that have a stackSize < 1
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (getItemStackAt(i) == null) continue;

            if (getItemStackAt(i).getStackSize() < 1) {
                removeItemStack(i);
            }
        }
    }

    public void clearInventory() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            inventory[i] = null;
        }
    }

    public int getNextFreeSlot() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (getItemStackAt(i) == null) {
                return i;
            }
        }

        return -1;
    }
}
