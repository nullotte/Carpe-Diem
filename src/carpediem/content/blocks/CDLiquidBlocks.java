package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.draw.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;

public class CDLiquidBlocks {
    public static Block
    pump;

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
    }
}
