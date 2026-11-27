<img width="1197" height="796" alt="Screenshot 2026-11-24 204527" src="https://github.com/user-attachments/assets/d98db893-8be6-4cd0-a141-c6ac5699d1bb" />

# Sandbox

A 2D tile-based sandbox game built from scratch in **Java** using **Java Swing / AWT**—with no external game engines or physics libraries. Designed to explore real-time game loops, grid-based world generation, custom AABB collision detection, and modular item/entity systems.

\---

## Features

* **Custom 60 UPS Fixed-Timestep Game Loop:** Decoupled update and render timing using `System.nanoTime()` with live FPS/UPS tracking.
* **Grid-Based World \& Layered Generation:** Procedural terrain layers (grass, dirt, stone) with reach-limited (`blockRange`) block placement, breaking, and semi-transparent placement previews.
* **Custom Physics \& Collision:** Gravity, jumping, crouching with ceiling-clearance detection (`uncrouchBlocked`), and grid-snapping hitbox resolution.
* **Hotbar Inventory System:** 9-slot stackable inventory (`ItemStack`) supporting blocks, tools, and throwables.
* **In-Game Command Console:** Interactive command prompt (`/` or `Enter`) for spawning items and managing inventory slots.

\---

## Items, Tools \& Throwables

### Blocks

* **Stone, Dirt, Grass, Wood Planks, Log, Leaves, Concrete** — Placeable and breakable terrain/building blocks (max stack: 64).

### Tools

* **Eyedropper:** Samples a targeted block in the world and adds a full stack of it to the next available hotbar slot.
* **Info Tool:** Inspects a targeted block in the world and outputs its coordinates and block details.

### Throwables

* **Bomb:** Gravity-affected explosive with a fuse timer that clears blocks within a Manhattan-distance blast radius.
* **Fill Grenade:** Terraforming throwable that populates surrounding tiles within its blast radius with a selected block type.

\---

## Controls

|Input|Action|
|-|-|
|**`A` / `D`** or **`Left` / `Right`**|Move left / right|
|**`W` / `Up` / `Space`**|Jump|
|**`Shift`**|Crouch (hold)|
|**`Left Click`**|Select hotbar slot, break block, or use equipped tool|
|**`Right Click`**|Place selected block or throw equipped throwable|
|**`/`** or **`Enter`**|Open command console|
|**`Esc`**|Close command console|

\---

## Console Commands

Press **`/`** or **`Enter`** in-game to open the console dialog:

* `/give <item>` — Gives `1` of the specified item in the next free slot (e.g., `/give eyedropper`).
* `/give <item> <count>` — Gives a specific quantity of an item (e.g., `/give stone 64`).
* `/clear` — Clears the entire hotbar inventory.
* `/clear <index>` — Clears the item stack at a specific hotbar slot index (e.g., `/clear 0`).

\---

## Project Structure

```text
src/
├── core/        # Entry point (Main), fixed-timestep GameLoop, rendering/input (GamePanel), and Console
├── entities/    # Player physics/actions and Throwable entities (Bomb, FillGrenade)
├── items/       # ItemType interfaces, enums (BlockType, ToolType, ThrowableType), Inventory, ItemStack, and Tools
└── world/       # 2D Block grid, terrain generation, coordinate snapping, and bounds utilities
```

\---

## Getting Started

### Prerequisites

* **Java Development Kit (JDK) 21+** (uses `Math.clamp` and modern Java features)

### Running Locally

1. Clone the repository:

```bash
   git clone https://github.com/noahh6/Sandbox.git
   cd Sandbox
   ```

2. Compile and run from the `src` directory (making sure `/images/itemsheet.png` is on the classpath):

```bash
   javac -d out src/core/\*.java src/entities/\*.java src/items/\*.java src/world/\*.java
   cp -r resources/\* out/   # if sprites are stored in a resources folder
   java -cp out core.Main
   ```

   *(Or open the project directly in IntelliJ IDEA and run `core.Main`.)*

\---

## Roadmap \& Future Ideas

* World saving and loading (serialization to file)
* Additional tools (area brush / block swapper) and throwables
* Further physics and collision refinements

