package core;

import entities.*;
import entities.Throwable;
import items.*;
import world.*;

public class Console {

    private final Player player;

    public Console(Player player) {
        this.player = player;
    }

    public boolean prompt(String prompt) {
        if (prompt.charAt(0) != '/') return false;

        String[] arguments = prompt.substring(1).split(" ");
        var inventory = player.getInventory();

        switch (arguments[0]) {
            case "give":
                if (arguments.length > 3 || arguments.length < 2) {
                    System.out.println("INVALID COMMAND: /give requires 2 or 3 arguments, e.g. /give eyedropper OR /give stone 64");
                    return false;
                }

                ItemType itemType = Items.getItemType(arguments[1]);

                if (itemType == null) {
                    System.out.println("INVALID INPUT: No item found: " + arguments[1]);
                    return false;
                }

                int count = 1;

                if (arguments.length == 3) {
                    try {
                        count = Integer.parseInt(arguments[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("INVALID INPUT: Count must be a valid number: " + arguments[2]);
                        return false;
                    }

                    if (count <= 0) {
                        System.out.println("INVALID INPUT: Count must be a valid number: " + count);
                        return false;
                    }
                }

                int freeIndex = inventory.getNextFreeSlot();

                if (freeIndex != -1) {
                    inventory.setItemStack(freeIndex, itemType, count);
                }

                if (count > 1 && itemType instanceof ThrowableType) {
                    System.out.printf("CONSOLE: Giving player %d %ss%n", count, arguments[1].toLowerCase());
                } else {
                    System.out.printf("CONSOLE: Giving player %d %s%n", count, arguments[1].toLowerCase());
                }

                return true;

            case "clear":

                if (arguments.length == 1) {
                    inventory.clearInventory();
                    System.out.println("CONSOLE: INVENTORY CLEARED");
                    return true;
                } else if (arguments.length == 2) {
                    int slotIndex = -1;

                    try {
                        slotIndex = Integer.parseInt(arguments[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("INVALID INPUT: Index must be a valid number: " + arguments[1]);
                        return false;
                    }

                    if (slotIndex < 0 || slotIndex >= Inventory.INVENTORY_SIZE) {
                        System.out.println("INVALID INPUT: Index must be a valid number: " + slotIndex);
                        return false;
                    }

                    inventory.removeItemStack(slotIndex);
                    System.out.println("CONSOLE: INVENTORY SLOT " + slotIndex + " CLEARED");
                    return true;
                } else {
                    System.out.println("INVALID COMMAND: /clear requires 1 or 2 argument");
                    return false;
                }

            default:
                System.out.println("INVALID COMMAND");
        }

        return false;
    }
}
