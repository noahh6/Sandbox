package items;

import java.awt.image.*;

public interface ItemType {

    String getDisplayName();
    int getMaxStackSize();
    int getID();
    BufferedImage getSprite();
}
