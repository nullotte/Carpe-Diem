package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.logic.*;

public class CDLogicBlocks {
    public static Block
    computationProcessor,
    computationMessage, lever,
    memoryModule, largeMemoryModule,
    computationDisplay, largeComputationDisplay, tiledComputationDisplay;

    public static void load() {
        computationProcessor = new LogicBlock("computation-processor") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
            instructionsPerTick = 25;
            range = 64f * 8f;
        }};

        computationMessage = new MessageBlock("computation-message") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.lemon, 39
            ));
        }};

        lever = new Lever("lever") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.lemon, 39
            ));
        }};

        memoryModule = new MemoryBlock("memory-module") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.lemon, 39
            ));
            memoryCapacity = 64;
        }};

        largeMemoryModule = new MemoryBlock("large-memory-module") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.lemon, 39
            ));
            memoryCapacity = 512;
            size = 2;
        }};

        computationDisplay = new CDLogicDisplay("computation-display") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
            displaySize = (size * 32) - 16;
        }};

        largeComputationDisplay = new CDLogicDisplay("large-computation-display") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 6;
            displaySize = (size * 32) - 16;
        }};

        tiledComputationDisplay = new TileableLogicDisplay("tiled-computation-display") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.lemon, 39
            ));
            frameSize = 8;
        }};
    }
}
