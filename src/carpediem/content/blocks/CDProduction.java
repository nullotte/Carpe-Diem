package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.production.*;
import carpediem.world.consumers.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;

public class CDProduction {
    public static Block drillT0, drillT1, drillT2;

    public static void load() {
        drillT0 = new ItemConsumerDrill("drill-t0") {{
            requirements(Category.production, ItemStack.with());
            tier = 3;
            drillTime = 480f;
            size = 3;

            consume(new ConsumeItemsUses(7, ItemStack.with(CDItems.sulfur, 1)));
            consumeLiquid(Liquids.water, 0.06f).boost(); // TODO should it?
        }};

        drillT1 = new Drill("drill-t1") {{
            requirements(Category.production, ItemStack.with());
            tier = 4;
            drillTime = 480f;
            size = 4;

            consumeLiquid(Liquids.water, 0.06f).boost();
        }};
    }
}
