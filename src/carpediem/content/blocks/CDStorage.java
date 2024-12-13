package carpediem.content.blocks;

import arc.math.geom.*;
import carpediem.content.*;
import carpediem.world.blocks.storage.*;
import carpediem.world.draw.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

public class CDStorage {
    public static CoreBlock
    landingPodT0, landingPodT1, landingPodT2,
    industryHub;

    public static Block
    storageVault, storageRelay;

    public static void load() {
        landingPodT0 = new LandingPod("landing-pod-t0") {{
            requirements(Category.effect, BuildVisibility.editorOnly, ItemStack.with());
            size = 4;
            alwaysUnlocked = true;

            itemCapacity = 5000;
            unitCapModifier = 2;
            unitType = CDUnitTypes.cache;

            craftingSpeed = 0.5f;
            // wow
            recipes.addAll(CDRecipes.pressRecipes).addAll(CDRecipes.rollingMillRecipes).addAll(CDRecipes.assemblerRecipes);

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

            itemCapacity = 20000;
            unitCapModifier = 32;
            unitType = CDUnitTypes.cache;

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    DrawItemSlot.mirrored(
                            new Vec2(6f, 16f),
                            new Vec2(16f, 6f),
                            new Vec2(12f, 12f)
                    ),
                    new DrawCoreDoor(),
                    new DrawDefault(),
                    new DrawTeam()
            );
        }};

        storageVault = new DrawerStorageBlock("storage-vault") {{
            requirements(Category.effect, ItemStack.with());
            size = 6;
            itemCapacity = 5000;

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    DrawItemSlot.mirrored(
                            new Vec2(33f / 4f, 12f / 4f),
                            new Vec2(57f / 4f, 12f / 4f),
                            new Vec2(12f / 4f, 33f / 4f),
                            new Vec2(12f / 4f, 57f / 4f)
                    ),
                    new DrawDefault(),
                    new DrawTeam()
            );
        }};

        storageRelay = new StorageRelay("storage-relay") {{
            requirements(Category.effect, ItemStack.with());
            size = 5;

            drawer = new DrawDefault();

            consumePower(2f);
        }};
    }
}
