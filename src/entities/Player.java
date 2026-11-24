package entities;

import core.*;
import items.*;
import world.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Player {

    private int h, x, y, speed, yVelocity, jumpStrength;
    private final int w, gravity, normalSpeed, crouchSpeed, normalH, crouchH, blockRange, normalJumpStrength, crouchJumpStrength;
    private boolean isMovingLeft, isMovingRight, isJumping, isCrouching, uncrouchBlocked;

    private final Rectangle hitBox;

    private final Inventory inventory;
    private final World world;

    private final List<Tool> activeTools;

    public Player(World world) {
        // Constants
        w = 25;
        gravity = World.GRAVITY;
        normalSpeed = 6;
        crouchSpeed = 3;
        normalH = 75;
        crouchH = 50;
        blockRange = 6;
        normalJumpStrength = 25;
        crouchJumpStrength = 18;

        int spawnRow = World.ROWS - 12;
        int spawnCol = World.COLS / 2;

        // Variables
        h = normalH;
        x = spawnCol * Block.SIZE;
        y = spawnRow * Block.SIZE;
        speed = normalSpeed;
        yVelocity = 0;
        jumpStrength = normalJumpStrength;
        uncrouchBlocked = false;

        hitBox = new Rectangle(x, y, w, h);
        this.world = world;
        activeTools = new ArrayList<>();
        inventory = new Inventory();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public World getWorld() {
        return world;
    }

    public void update() {

        if (isCrouching) {
            h = crouchH;
            speed = crouchSpeed;
            jumpStrength = crouchJumpStrength;
        }
        else {
            h = normalH;
            speed = normalSpeed;
            jumpStrength = normalJumpStrength;
        }

        // Update hitbox
        hitBox.setBounds(x, y, w, h);

        if (uncrouchBlocked && !getBlockedDirections(true).contains("up")) {
            uncrouchBlocked = false;
            isCrouching = false;
            y -= Block.SIZE;
        }

        List<String> blockedDirs = getBlockedDirections(false);

        if (!onBlock()) {
            y += gravity;
        }

        if (isJumping) {
            // Slow down as you approach the top of the jump
            y -= yVelocity;
            yVelocity--;

            if (yVelocity < 0 || getBlockedDirections(true).contains("up")) {
                yVelocity = 0;
            }
        }

        if (y + h > GamePanel.HEIGHT) {
            y = GamePanel.HEIGHT - h;
            yVelocity = 0;
            isJumping = false;
        }

        if (onBlock()) {
            isJumping = false;
            // Snap to block
            y = Math.round((float) y / Block.SIZE) * Block.SIZE;
        }

        if (isMovingLeft) {
            if (blockedDirs.contains("left")) {
                // Snap to as far the player can go
                x = Math.round((float) x / Block.SIZE) * Block.SIZE;
            }
            else {
                x -= speed;

                if (x <= 0) {
                    x = 0;
                }
            }
        }

        if (isMovingRight) {
            if (blockedDirs.contains("right")) {
                x = Math.round((float) x / Block.SIZE) * Block.SIZE;
            }
            else {
                x += speed;

                if (x + w >= GamePanel.WIDTH) {
                    x = GamePanel.WIDTH - w;
                }
            }
        }

        hitBox.setBounds(x, y, w, h);
    }

    /**
     * Check if the player is intersecting any blocks in the immediate vicinity of the player
     * @return A list of directions the player can't move in, if the player's hitbox is intersecting any block hotbox within the area
     */
    private List<String> getBlockedDirections(boolean checkJump) {

        List<String> directions = new ArrayList<>();

        int startRow = y / Block.SIZE - Block.SIZE * 2;
        int endRow = (y + h) / Block.SIZE + Block.SIZE * 2;
        int startCol = x / Block.SIZE - Block.SIZE * 2;
        int endCol = (x + w) / Block.SIZE + Block.SIZE * 2;

        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {

                Point bounds = World.fitToBounds(r, c);
                Block block = world.getBlockAt((int) bounds.getX(), (int) bounds.getY());

                if (block.getItemType() == BlockType.AIR) continue;

                if (isMovingLeft) {
                    Rectangle leftBox = new Rectangle(hitBox);
                    leftBox.x -= speed;
                    if (leftBox.intersects(block.getHitbox()) && leftBox.x < block.getHitbox().x + Block.SIZE) {
                        directions.add("left");
                    }
                }

                if (isMovingRight) {
                    Rectangle rightBox = new Rectangle(hitBox);
                    rightBox.x += speed;
                    if (rightBox.intersects(block.getHitbox()) && rightBox.x + w > block.getHitbox().x) {
                        directions.add("right");
                    }
                }

                if (checkJump) {
                    Rectangle jumpBox = new Rectangle(hitBox);
                    jumpBox.y -= jumpStrength;
                    if (jumpBox.intersects(block.getHitbox()) && jumpBox.y < block.getHitbox().y + Block.SIZE) {
                        directions.add("up");
                    }
                }
            }
        }

        return directions;
    }

    private boolean onBlock() {

        int row = (y + h) / Block.SIZE;
        int startCol = x / Block.SIZE;
        int endCol = (x + w) / Block.SIZE;

        for (int col = startCol; col <= endCol; col++) {

            Point bounds = World.fitToBounds(row, col);
            Block block = world.getBlockAt((int) bounds.getX(), (int) bounds.getY());

            if (block.getItemType() == BlockType.AIR) continue;

            Rectangle nextHitBox = new Rectangle(hitBox);

            nextHitBox.y += gravity;

            if (nextHitBox.intersects(block.getHitbox())) {
                return true;
            }
        }

        return false;
    }

    public void moveLeft(boolean moveLeft) {
        isMovingLeft = moveLeft;
    }

    public void moveRight(boolean moveRight) {
        isMovingRight = moveRight;
    }

    public void setCrouching(boolean isCrouching) {
        if (this.isCrouching && getBlockedDirections(true).contains("up")) {
            uncrouchBlocked = true;
            if (isCrouching) {
                uncrouchBlocked = false;
                this.isCrouching = true;
            }
            return;
        }

        this.isCrouching = isCrouching;

        if (isCrouching) {
            y += normalH - crouchH;
        }
        else {
            y -= normalH - crouchH;
        }
    }

    public void jump() {
        if (isJumping || getBlockedDirections(true).contains("up")) return;

        yVelocity = jumpStrength;
        isJumping = true;
    }

    public void breakBlockAt(int row, int col) {
        Block block = world.getBlockAt(row, col);

        if (block == null || block.getItemType() == BlockType.AIR || !isInPlayerRange(row, col)) {
            return;
        }
        // Check that the selected item isn't a tool
        ItemStack selectedItemStack = inventory.getItemStackAt(inventory.getSelectedSlot());
        if (selectedItemStack != null && selectedItemStack.getItemType() instanceof ToolType) return;

        world.setBlockAt(BlockType.AIR, row, col);
    }

    public void placeBlockAt(int row, int col) {
        ItemStack itemStack = inventory.getItemStackAt(inventory.getSelectedSlot());
        Block currentBlock = world.getBlockAt(row, col);

        if (itemStack == null || itemStack.getItemType() == BlockType.AIR ||
                currentBlock.getItemType() != BlockType.AIR || !isInPlayerRange(row, col)) return;

        if (hitBox.intersects(currentBlock.getHitbox())) return;
        if (!itemStack.reduceStack()) return;

        world.setBlockAt((BlockType) itemStack.getItemType(), row, col);

        inventory.clearEmptyStacks();
    }

    public boolean isInPlayerRange(int row, int col) {

        int playerRow = (y + h / 2) / Block.SIZE;
        int playerCol = (x + w / 2) / Block.SIZE;

        for (int r = playerRow - blockRange; r <= playerRow + blockRange; r++) {
            for (int c = playerCol - blockRange; c <= playerCol + blockRange; c++) {

                if (r < 0 || r >= World.ROWS || c < 0 || c >= World.COLS) {
                    continue;
                }

                int distance = Math.abs(playerRow - r) + Math.abs(playerCol - c);

                if (distance <= blockRange && row == r && col == c) {
                    return true;
                }

            }
        }

        return false;
    }

    public void useThrowableAt(int x, int y) {
        if (x > GamePanel.WIDTH || x < 0 || y > GamePanel.HEIGHT || y < 0) return;

        Point point = World.fitToBounds(y / Block.SIZE, x / Block.SIZE);

        int row = (int) point.getX();
        int col = (int) point.getY();

        ItemStack itemStack = inventory.getItemStackAt(inventory.getSelectedSlot());
        Block currentBlock = world.getBlockAt(row, col);

        if (itemStack == null || currentBlock.getItemType() != BlockType.AIR || !isInPlayerRange(row, col) ||
                !(itemStack.getItemType() instanceof ThrowableType)) return;

        if (!itemStack.reduceStack()) return;

        Throwable throwable = ((ThrowableType) itemStack.getItemType()).createThrowable(world, x, y);

        world.addActiveThrowable(throwable);

        inventory.clearEmptyStacks();
    }

    public void useTool(ToolType toolType, Block block) {

        Tool targetTool = null;

        for (Tool tool : activeTools) {
            if (tool.getItemType() == toolType) {
                targetTool = tool;
                break;
            }
        }

        if (targetTool == null) {
            targetTool = toolType.createTool(this);
            if (targetTool == null) return;
            activeTools.add(targetTool);
        }

        targetTool.useTool(block);
    }

}
