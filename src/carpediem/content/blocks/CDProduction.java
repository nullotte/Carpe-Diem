package carpediem.content.blocks;

import arc.math.*;
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
            size = 3;

            tier = 3;
            drillTime = 60f * 4f * Mathf.sqr(size);
            hardnessDrillMultiplier = 0f;
            drillMultipliers.put(CDItems.sulfur, 9f / 8f);

            consume(new ConsumeItemsUses(7, ItemStack.with(CDItems.sulfur, 1)));
            consumeLiquid(Liquids.water, 0.06f).boost(); // TODO should it?
        }};

        drillT1 = new Drill("drill-t1") {{
            requirements(Category.production, ItemStack.with());
            size = 4;

            tier = 4;
            drillTime = 60f * 2f * Mathf.sqr(size);
            hardnessDrillMultiplier = 0f;
            drillMultipliers.put(CDItems.rawSilver, 0.5f);

            consumePower(1f / 10f);
            consumeLiquid(Liquids.water, 0.06f).boost();
        }};
    }
}
