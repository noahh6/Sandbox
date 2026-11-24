package core;

import entities.*;
import entities.Throwable;
import items.*;
import world.*;

public class Console {

    private final Player player;

    public Console(Player player, World world) {
        this.player = player;
    }

    public boolean prompt(String prompt) {
        if (prompt.charAt(0) != '/') return false;

        String[] arguments = prompt.substring(1).split(" ");

        switch (arguments[0]) {
            case "give":
                if (arguments.length > 3 || arguments.length < 2) {
                    System.out.println("INVALID COMMAND: /give requires 2 or 3 arguments, e.g. /give eyedropper OR /give stone 64");
                    return false;
                }

                ItemType itemType = getItemType(arguments[1]);

                if (itemType == null) {
                    System.out.println("INVALID INPUT: No item found: " + arguments[1]);
                    return false;
                }

                int count = 1;

                if (arguments.length == 3) {
                    count = Integer.parseInt(arguments[2]);
                }

                var inventory = player.getInventory();
                int freeIndex = inventory.getNextFreeSlot();

                if (freeIndex != -1) {
                    inventory.setItemStack(freeIndex, itemType, count);
                }

                if (count > 1 && itemType instanceof ThrowableType) {
                    System.out.printf("CONSOLE: Giving player %d %ss%n", count, arguments[1].toLowerCase());
                }
                else {
                    System.out.printf("CONSOLE: Giving player %d %s%n", count, arguments[1].toLowerCase());
                }

                return true;

            case "clear":
                if (arguments.length > 1) {
                    System.out.println("INVALID COMMAND: /clear requires 1 or 2 argument");
                    return false;
                }

                player.getInventory().clearInventory();

                System.out.println("CONSOLE: INVENTORY CLEARED");

                return true;

            default:
                System.out.println("INVALID COMMAND");
        }

        return false;
    }

    private static ItemType getItemType(String itemName) {
        for (Items item : Items.values()) {
            if (item.getItemType() == null) continue;
            String itemN = item.getItemType().getDisplayName();

            if (itemName.equalsIgnoreCase(itemN)) {
                return item.getItemType();
            }

        }

        return null;
    }

    
}
