package carpediem.content.blocks;

import arc.math.geom.*;
import carpediem.world.blocks.storage.*;
import carpediem.world.draw.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

public class CDStorage {
    // TODO tiered landing pods. and the fucking ROCKET
    public static Block landingPod, industryHub;

    public static void load() {
        landingPod = new DrawerCoreBlock("landing-pod") {{
            requirements(Category.effect, BuildVisibility.editorOnly, ItemStack.with());
            size = 4;
            alwaysUnlocked = true;
            itemCapacity = 5000;
            unitCapModifier = 2;
        }};

        industryHub = new HubBlock("industry-hub") {{
            requirements(Category.effect, ItemStack.with());
            size = 6;
            unitType = UnitTypes.gamma;

            // it's balanced i swear
            health = 99999;
            itemCapacity = 20000;
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
