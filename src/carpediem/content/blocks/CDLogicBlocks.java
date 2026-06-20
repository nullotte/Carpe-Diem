package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.graphics.*;
import carpediem.world.blocks.logic.*;
import carpediem.world.draw.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.logic.*;
import mindustry.world.draw.*;

public class CDLogicBlocks {
    public static Block
    computationProcessor,
    computationMessage, lever,
    memoryModule, largeMemoryModule,
    computationDisplay, largeComputationDisplay, tiledComputationDisplay;

    public static void load() {
        computationProcessor = new DrawerLogicBlock("computation-processor") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.aluminum, 100,
                    CDItems.aluminumPlate, 50,
                    CDItems.nickelWire, 70,
                    CDItems.nickelPlate, 50,
                    CDItems.controlCircuit, 30,
                    CDItems.calculationCircuit, 50,
                    Items.silicon, 50
            ));
            size = 3;
            instructionsPerTick = 25;
            range = 64f * 8f;
            drawer = new DrawMulti(
                    new DrawDefault(),
                    new DrawAlwaysGlowRegion() {{
                        color = CDColors.coalition;
                        alpha = 0.5f;
                    }}
            );
        }};

        computationMessage = new DrawerMessageBlock("computation-message") {{
            requirements(Category.logic, ItemStack.with(
                    Items.plastanium, 5,
                    Items.silicon, 5,
                    CDItems.calculationCircuit, 5
            ));
            drawer = new DrawMulti(
                    new DrawDefault(),
                    new DrawAlwaysGlowRegion() {{
                        color = CDColors.coalition;
                        alpha = 0.5f;
                    }}
            );
        }};

        lever = new Lever("lever") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.aluminumRod, 5,
                    CDItems.aluminumCogwheel, 5,
                    Items.silicon, 5,
                    CDItems.calculationCircuit, 1
            ));
        }};

        memoryModule = new MemoryBlock("memory-module") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.aluminum, 10,
                    CDItems.nickelPlate, 10,
                    Items.silicon, 10,
                    CDItems.calculationCircuit, 5
            ));
            memoryCapacity = 64;
        }};

        largeMemoryModule = new MemoryBlock("large-memory-module") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.aluminum, 30,
                    CDItems.nickelPlate, 25,
                    CDItems.nickelRod, 10,
                    Items.silicon, 20,
                    CDItems.calculationCircuit, 15
            ));
            memoryCapacity = 512;
            size = 2;
        }};

        computationDisplay = new CDLogicDisplay("computation-display") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.aluminum, 80,
                    CDItems.plastaniumSheet, 50,
                    Items.silicon, 30,
                    CDItems.calculationCircuit, 5
            ));
            size = 3;
            displaySize = (size * 32) - 16;
        }};

        largeComputationDisplay = new CDLogicDisplay("large-computation-display") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.aluminum, 140,
                    CDItems.plastaniumSheet, 80,
                    Items.silicon, 50,
                    CDItems.calculationCircuit, 15
            ));
            size = 6;
            displaySize = (size * 32) - 16;
        }};

        tiledComputationDisplay = new TileableLogicDisplay("tiled-computation-display") {{
            requirements(Category.logic, ItemStack.with(
                    CDItems.aluminum, 5,
                    CDItems.plastaniumSheet, 5,
                    Items.silicon, 3,
                    CDItems.calculationCircuit, 1
            ));
            frameSize = 8;
        }};
    }
}
