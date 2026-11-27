package items;

import entities.Player;
import world.*;

public class Eyedropper extends Tool {

    private final Player player;

    public Eyedropper(Player player) {
        super(ToolType.EYEDROPPER);
        this.player = player;
    }

    @Override
    public void useTool(Block block) {

        int freeIndex = -1;
        boolean isInInventory = false;

        for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
            ItemStack selectedItemStack = player.getInventory().getItemStackAt(i);

            if (selectedItemStack != null && selectedItemStack.getItemType() == block.getItemType()) {
                // Block is already in inventory
                isInInventory = true;
                break;
            }
            else if (selectedItemStack == null && freeIndex == -1) {
                // Set the index to first free slot
                freeIndex = i;
            }
        }

        if (freeIndex != -1 && !isInInventory) {
            BlockType selectedBlock = (BlockType) block.getItemType();
            if (block.getItemType() != null && block.getItemType() != BlockType.AIR) {
                player.getInventory().setItemStack(freeIndex, selectedBlock, selectedBlock.getMaxStackSize());
            }
        }
    }
}
