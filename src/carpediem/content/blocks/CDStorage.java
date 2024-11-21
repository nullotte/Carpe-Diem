package carpediem.content.blocks;

import arc.math.geom.*;
import carpediem.world.blocks.storage.*;
import carpediem.world.draw.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

public class CDStorage {
    public static CoreBlock landingPodT0, landingPodT1, landingPodT2, industryHub;

    public static void load() {
        landingPodT0 = new DrawerCoreBlock("landing-pod-t0") {{
            requirements(Category.effect, BuildVisibility.editorOnly, ItemStack.with());
            size = 4;
            alwaysUnlocked = true;
            itemCapacity = 5000;
            unitCapModifier = 2;

            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawCoreDoor(),
                    new DrawDefault(),
                    new DrawTeam()
            );
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
                    new DrawCoreDoor(),
                    new DrawDefault(),
                    new DrawTeam()
            );
        }};
    }
}
