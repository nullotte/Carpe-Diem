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
                    CDItems.lemon, 39
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
                    CDItems.silver, 85,
                    CDItems.silverPlate, 50
            ));
            size = 3;
            liquidCapacity = 3000;
        }};
    }
}
