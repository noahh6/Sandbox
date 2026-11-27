package items;

import core.*;
import entities.*;
import world.*;

import java.awt.image.*;

public enum ToolType implements ItemType {
    EYEDROPPER("Eyedropper", 8, Eyedropper::new), PAINTBRUSH("Paintbrush", 10, Paintbrush::new),
    INFO_TOOL("Info Tool", 11, InfoTool::new);

    public static final int MAX_STACK_SIZE = 1;

    private final String displayName;
    private final int ID;
    private final ToolFactory factory;
    private final BufferedImage sprite;

    ToolType(String displayName, int ID, ToolFactory factory) {
        this.displayName = displayName;
        this.ID = ID;
        this.factory = factory;

        sprite = GamePanel.getSprite(ID);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getMaxStackSize() {
        return MAX_STACK_SIZE;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public BufferedImage getSprite() {
        return sprite;
    }

    public Tool createTool(Player player) {
        return factory == null ? null : factory.create(player);
    }

    @FunctionalInterface
    public interface ToolFactory {
        Tool create(Player player);
    }
}
