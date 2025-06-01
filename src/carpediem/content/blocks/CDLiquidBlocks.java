package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.liquid.*;
import carpediem.world.draw.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;

public class CDLiquidBlocks {
    public static Block
    pump,
    pipe, valve, pipeBridge, fluidTank;

    public static void load() {
        pump = new Pump("pump") {{
            requirements(Category.liquid, ItemStack.with(
                    CDItems.aluminum, 25,
                    CDItems.aluminumCogwheel, 20,
                    CDItems.nickelWire, 5,
                    CDItems.silverPlate, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 10,
                    CDItems.fluidCell, 15
            ));
            size = 5;
            pumpAmount = 4f / 60f; // 100 per second with 25 tiles

            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(null, 10f / 4f),
                    new DrawPump(),
                    new DrawDefault()
            );

            consumePower(2f);
        }};

        pipe = new Pipe("pipe") {{
            requirements(Category.liquid, ItemStack.with(
                    CDItems.silver, 1,
                    CDItems.silverPlate, 1
            ));
        }};

        valve = new Valve("valve") {{
            requirements(Category.liquid, ItemStack.with(
                    CDItems.silver, 3,
                    CDItems.silverPlate, 2,
                    CDItems.silverRod, 2
            ));
        }};

        pipeBridge = new PipeBridge("pipe-bridge") {{
            requirements(Category.liquid, ItemStack.with(
                    CDItems.silver, 8,
                    CDItems.silverPlate, 1,
                    CDItems.silverRod, 4
            ));
        }};

        fluidTank = new MergingLiquidBlock("fluid-tank") {{
            requirements(Category.liquid, ItemStack.with(
                    CDItems.aluminum, 100,
                    CDItems.silver, 50,
                    CDItems.silverPlate, 25,
                    CDItems.fluidCell, 25
            ));
            size = 3;
            liquidCapacity = 3000;
        }};
    }
}
