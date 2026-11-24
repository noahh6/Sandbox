package world;

import core.*;
import items.*;
import entities.Throwable;

import java.awt.*;
import java.util.*;
import java.util.List;

public class World {

    public static final int ROWS = GamePanel.HEIGHT / 25;
    public static final int COLS = GamePanel.WIDTH / 25;
    public static final int GRAVITY = 10;

    private final Block[][] blocks;
    private final List<Throwable> activeThrowables;

    public World() {
        blocks = new Block[ROWS][COLS];
        activeThrowables = new ArrayList<>();

        createPlainWorld();
    }

    private void createPlainWorld() {

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Block block = new Block(BlockType.AIR, r, c);
                blocks[r][c] = block;
            }
        }

        int stoneDepth = 4;
        int dirtDepth = 4;
        int grassDepth = 1;
        int worldHeight = ROWS - 1;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Block block = blocks[r][c];

                if (r > worldHeight - stoneDepth) {
                    // Stone layer
                    block.setBlockType(BlockType.STONE);
                }
                else if (r > worldHeight - stoneDepth - dirtDepth) {
                    // Dirt Layer
                    block.setBlockType(BlockType.DIRT);
                }
                else if (r > worldHeight - stoneDepth - dirtDepth - grassDepth) {
                    // Grass Layer
                    block.setBlockType(BlockType.GRASS);
                }
            }
        }
    }

    public void addActiveThrowable(Throwable throwable) {
        activeThrowables.add(throwable);
    }

    public List<Throwable> getActiveThrowables() {
        return activeThrowables;
    }

    public Block getBlockAt(int row, int col) {
        if (row >= ROWS || row < 0 || col >= COLS || col < 0) return null;
        return blocks[row][col];
    }

    public void setBlockAt(BlockType blockType, int row, int col) {
        if (row >= ROWS || row < 0 || col >= COLS || col < 0) return;
        blocks[row][col].setBlockType(blockType);
    }

    public void updateWorldState() {
        if (activeThrowables.isEmpty()) return;

        for (int i = activeThrowables.size() - 1; i >= 0; i--) {
            Throwable throwable = activeThrowables.get(i);
            throwable.update();
            if (!throwable.isActive()) {
                activeThrowables.remove(throwable);
            }
        }
    }

    public static int snapToGrid(int coord) {
        return Math.floorDiv(coord, Block.SIZE) * Block.SIZE;
    }

    public static Point fitToBounds(int row, int col) {
        int maxRow = ROWS - 1;
        int maxCol = COLS - 1;

        int r = Math.clamp(row, 0, maxRow);
        int c = Math.clamp(col, 0, maxCol);

        return new Point(r, c);
    }
}
