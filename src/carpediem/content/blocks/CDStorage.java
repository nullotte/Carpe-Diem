package carpediem.content.blocks;

import arc.math.geom.*;
import carpediem.world.blocks.storage.*;
import carpediem.world.draw.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDStorage {
    public static Block hub;

    public static void load() {
        hub = new HubBlock("hub") {{
            requirements(Category.effect, ItemStack.with());
            unitType = UnitTypes.gamma;
            size = 6;

            // it's balanced i swear
            health = 99999;
            itemCapacity = 10000;
            alwaysUnlocked = true;
            isFirstTier = true;
            unitCapModifier = 32;

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawItemSlot(true, new Vec2(6f, 16f), new Vec2(12f, 12f)),
                    new DrawDefault(),
                    new DrawTeam()
            );
        }};
    }
}
