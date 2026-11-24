package core;

import entities.*;
import entities.Throwable;
import world.*;
import items.*;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

public class GamePanel extends JPanel {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;

    private static final BufferedImage[] SPRITES = getSprites();

    public final World world;

    private final Player player;

    private Point mousePos;

    private final Color outlineColor, highlightColor;
    private final BasicStroke outlineStroke;

    private final Rectangle inventoryBox;
    private final Rectangle[] inventorySlotBoxes;

    private static int IDCap;

    private final Console console;
    private boolean showConsole, isShowingConsole;

    public GamePanel(Player player) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        setBackground(new Color(105, 180, 255));

        this.player = player;
        world = player.getWorld();
        outlineColor = new Color(0, 0, 0, 60);
        highlightColor = new Color(0, 0, 0, 40);
        outlineStroke = new BasicStroke(1);

        console = new Console(player, world);
        showConsole = false;
        isShowingConsole = false;

        int invX = (int) (WIDTH * 0.2);
        int invY = (int) (HEIGHT * 0.01);
        int invW = (int) (WIDTH * 0.6);
        int invH = (int) (HEIGHT * 0.1);

        inventoryBox = new Rectangle(invX, invY, invW, invH);
        inventorySlotBoxes = new Rectangle[Inventory.INVENTORY_SIZE];

        Set<Integer> pressedKeys = new HashSet<>();

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                int keyCode = e.getKeyCode();

                if (pressedKeys.contains(keyCode)) {
                    return;
                }

                pressedKeys.add(keyCode);

                // Player Movement:

                // Left
                if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
                    player.moveLeft(true);
                }

                // Right
                if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
                    player.moveRight(true);
                }

                // Crouching
                if (keyCode == KeyEvent.VK_SHIFT) {
                    player.setCrouching(true);
                }

                // Jump
                if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_SPACE) {
                    player.jump();
                }

                // MISC.

                // Toggle Console
                if (keyCode == KeyEvent.VK_SLASH || keyCode == KeyEvent.VK_ENTER) {
                    showConsole = true;
                }

                if (keyCode == KeyEvent.VK_ESCAPE) {
                    showConsole = false;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                int keyCode = e.getKeyCode();

                pressedKeys.remove(keyCode);

                // Player Movement:

                // Left
                if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
                    player.moveLeft(false);
                }

                // Right
                if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
                    player.moveRight(false);
                }

                // Crouching
                if (keyCode == KeyEvent.VK_SHIFT) {
                    player.setCrouching(false);
                }
            }
        };

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                mousePos = e.getPoint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int button = e.getButton();
                int x = e.getX();
                int y = e.getY();
                int row = y / Block.SIZE;
                int col = x / Block.SIZE;
                var inventory = player.getInventory();
                ItemStack selectedItemStack = inventory.getItemStackAt(player.getInventory().getSelectedSlot());

                if (button == MouseEvent.BUTTON1) {

                    if (inventoryBox.contains(e.getPoint())) {
                        for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
                            if (inventorySlotBoxes[i].contains(e.getPoint())) {
                                inventory.setSelectedSlot(i);
                                break;
                            }
                        }
                    }
                    else {
                        // Use tool
                        if (selectedItemStack != null && selectedItemStack.getItemType() instanceof ToolType) {
                            for (ToolType toolType : ToolType.values()) {
                                if (selectedItemStack.getItemType() == toolType) {
                                    player.useTool(toolType, world.getBlockAt(row, col));
                                }
                            }
                        }
                        else {
                            player.breakBlockAt(row, col);
                        }
                    }
                }

                if (selectedItemStack == null) return;

                if (button == MouseEvent.BUTTON3) {
                    if (selectedItemStack.getItemType() instanceof BlockType) {
                        player.placeBlockAt(row, col);
                    }
                    else if (selectedItemStack.getItemType() instanceof ThrowableType) {
                        player.useThrowableAt(x, y);
                    }
                }
            }
        };

        addKeyListener(keyAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
    }

    private static BufferedImage[] getSprites() {

        BufferedImage itemsheet;

        try {
            itemsheet = ImageIO.read(Objects.requireNonNull(GamePanel.class.getResourceAsStream("/images/itemsheet.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int spriteWidth = 16;
        int spriteHeight = 16;
        int rows = itemsheet.getHeight() / spriteHeight;
        int cols = itemsheet.getWidth() / spriteWidth;

        IDCap = rows * cols;
        BufferedImage[] result = new BufferedImage[IDCap];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                result[(y * cols) + x] = itemsheet.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
            }
        }

        return result;
    }

    public static BufferedImage getSprite(int ID) {
        if (ID >= IDCap || ID < 0) return null;
        return SPRITES[ID];
    }

    private void drawPlayer(Graphics2D g2d) {
        int playerX = player.getX();
        int playerY = player.getY();
        int playerW = player.getWidth();
        int playerH = player.getHeight();

        g2d.setColor(Color.RED);
        g2d.fillOval(playerX, playerY, playerW, playerH);
    }

    /**
     * Draws the current map state
     * @param g2d graphics from paintComponent
     */
    private void drawWorld(Graphics2D g2d) {
        for (int r = 0; r < World.ROWS; r++) {
            for (int c = 0; c < World.COLS; c++) {
                Block block = world.getBlockAt(r, c);

                int blockX = c * Block.SIZE;
                int blockY = r * Block.SIZE;

                // Skip over if the block is null or air
                if (block.getItemType() == null || block.getItemType() == BlockType.AIR || block.getItemType().getSprite() == null) continue;

                g2d.drawImage(block.getItemType().getSprite(), blockX, blockY, Block.SIZE, Block.SIZE, null);

                // Outline
                g2d.setColor(outlineColor);
                g2d.setStroke(outlineStroke);
                g2d.drawRect(blockX, blockY, Block.SIZE, Block.SIZE);
            }
        }

        // Draw active throwables
        for (Throwable throwable : world.getActiveThrowables()) {
            BufferedImage sprite = throwable.getItemType().getSprite();
            int x = throwable.getX();
            int y = throwable.getY();
            g2d.drawImage(sprite, x, y, Item.SIZE, Item.SIZE, null);
        }
    }

    private void highlightSelectedBlock(Graphics2D g2d) {

        if (mousePos == null) return;

        int mouseX = (int) mousePos.getX();
        int mouseY = (int) mousePos.getY();

        int row = mouseY / Block.SIZE;
        int col = mouseX / Block.SIZE;
        int x = col * Block.SIZE;
        int y = row * Block.SIZE;

        if (inventoryBox.contains(mousePos)) return;

        if (player.isInPlayerRange(row, col)) {
            if (world.getBlockAt(row, col).getItemType() != BlockType.AIR) {
                g2d.setColor(highlightColor);
                g2d.fillRect(x, y, Block.SIZE, Block.SIZE);
            }
            else {
                ItemStack itemStack = player.getInventory().getItemStackAt(player.getInventory().getSelectedSlot());

                if (itemStack == null) return;

                ItemType selectedItemType = player.getInventory().getItemStackAt(player.getInventory().getSelectedSlot()).getItemType();

                if (selectedItemType == null) return;

                if (selectedItemType instanceof BlockType && selectedItemType != BlockType.AIR) {
                    Composite originalComposite = g2d.getComposite();

                    float alpha = 0.4f;
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

                    BufferedImage blockSprite = selectedItemType.getSprite();

                    g2d.drawImage(blockSprite, x, y, Block.SIZE, Block.SIZE, null);

                    g2d.setComposite(originalComposite);
                }
            }
        }
    }

    private final Font stackSizeFont = new Font(null, Font.BOLD, 20);

    private void drawInventory(Graphics2D g2d) {

        // Background
        int x = inventoryBox.x;
        int y = inventoryBox.y;
        int w = inventoryBox.width;
        int h = inventoryBox.height;

        g2d.setColor(highlightColor);
        g2d.fillRect(x, y, w, h);

        double gapRatio = 0.2;

        double s = Math.min(h, w / (9.0 + 10.0 * gapRatio));
        double g = s * gapRatio;

        double totalWidth = 9.0 * s + 10.0 * g;
        double startX = x + (w - totalWidth) / 2.0 + g;
        double startY = y + (h - s) / 2.0;

        for (int i = 0; i < 9; i++) {
            int slotX = (int) Math.round(startX + i * (s + g));
            int slotY = (int) Math.round(startY);
            int slotSize = (int) Math.round(s);

            // Initialise slot boxes
            if (inventorySlotBoxes[i] == null) {
                setInventorySlotBox(i, slotX, slotY, slotSize);
            }

            // Draw the slot background
            g2d.setColor(highlightColor);
            g2d.fillRect(slotX, slotY, slotSize, slotSize);

            // Outline
            if (player.getInventory().getSelectedSlot() == i) {
                g2d.setColor(Color.RED);
            }
            else {
                g2d.setColor(Color.BLACK);
            }
            g2d.drawRect(slotX, slotY, slotSize, slotSize);

            // Items
            int itemBuffer = slotSize / 10;
            int itemSize = slotSize - itemBuffer * 2;
            int itemX = slotX + itemBuffer;
            int itemY = slotY + itemBuffer;

            ItemStack itemStack = player.getInventory().getItemStackAt(i);
            if (itemStack != null && itemStack.getItemType() != BlockType.AIR) {
                BufferedImage sprite = itemStack .getItemType().getSprite();

                g2d.drawImage(sprite, itemX, itemY, itemSize, itemSize, null);

                // Item Stack Size Display
                int stackSize = itemStack.getStackSize();

                if (stackSize > 1) {
                    int stackSizeX = itemX - itemBuffer;
                    int stackSizeY = itemY + itemSize + itemBuffer;

                    g2d.setFont(stackSizeFont);
                    g2d.setColor(Color.WHITE);
                    g2d.drawString("" + stackSize, stackSizeX, stackSizeY);
                }
            }

        }
    }

    private void setInventorySlotBox(int index, int x, int y, int size) {
        inventorySlotBoxes[index] = new Rectangle(x, y, size, size);
    }

    private final Font debugFont = new Font(null, Font.PLAIN, 20);

    private void drawDebug(Graphics2D g2d) {

        int x = player.getX() + player.getWidth() / 2;
        int y = HEIGHT - (player.getY() + player.getHeight());

        // Draw player coords:
        g2d.setColor(Color.BLACK);
        g2d.setFont(debugFont);
        g2d.drawString("x: " + x + " y: " + y, 20, 20);

        if (showConsole && !isShowingConsole) {

            isShowingConsole = true;
            String userInput = JOptionPane.showInputDialog(null, "CONSOLE: ");
            isShowingConsole = false;

            if (userInput != null) {
                if (console.prompt(userInput)) {
                    showConsole = false;
                }
            }
            else {
                showConsole = false;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        drawWorld(g2d);

        highlightSelectedBlock(g2d);

        drawPlayer(g2d);

        drawInventory(g2d);

        drawDebug(g2d);
    }
}
