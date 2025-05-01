package carpediem.content.blocks;

import arc.math.geom.*;
import arc.struct.EnumSet;
import carpediem.content.*;
import carpediem.world.blocks.storage.*;
import carpediem.world.blocks.units.*;
import carpediem.world.draw.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

public class CDStorage {
    public static DrawerCoreBlock
    landingPodT0, landingPodT1, landingPodT2,
    industryHub;

    public static Block
    storageVault, shippingContainer, providerContainer, receiverContainer;

    public static void load() {
        landingPodT0 = new LandingPod("landing-pod-t0") {{
            requirements(Category.effect, BuildVisibility.editorOnly, ItemStack.with(
                    CDItems.aluminum, 1000,
                    CDItems.aluminumPlate, 800,
                    CDItems.aluminumRod, 300,
                    CDItems.aluminumCogwheel, 50,
                    CDItems.nickelPlate, 200,
                    CDItems.nickelWire, 300,
                    CDItems.powerCell, 500,
                    CDItems.controlCircuit, 250
            ));
            size = 4;
            alwaysUnlocked = true;

            itemCapacity = 5000;
            unitCapModifier = 2;
            unitType = CDUnitTypes.cache;

            craftingSpeed = 4f;
            // wow
            recipes.addAll(CDRecipes.pressRecipes).addAll(CDRecipes.rollingMillRecipes).addAll(CDRecipes.assemblerRecipes);
            craftEffect = Fx.pulverize;

            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawCoreDoor(),
                    new DrawDefault(),
                    new DrawTeam()
            );
        }};

        landingPodT1 = new LandingPod("landing-pod-t1") {{
            requirements(Category.effect, BuildVisibility.editorOnly, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;

            itemCapacity = 6000;
            unitCapModifier = 4;
            unitType = CDUnitTypes.cache;

            craftingSpeed = 8f;
            recipes.addAll(CDRecipes.pressRecipes).addAll(CDRecipes.rollingMillRecipes).addAll(CDRecipes.assemblerRecipes);
            craftEffect = Fx.pulverize;

            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawCoreDoor(),
                    new DrawDefault(),
                    new DrawTeam()
            );
        }};

        industryHub = new HubBlock("industry-hub") {{
            requirements(Category.effect, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 6;

            itemCapacity = 20000;
            unitCapModifier = 32;
            unitType = CDUnitTypes.cache;

            clipSize = size * 8f * 3f;
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
            requirements(Category.effect, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 6;
            itemCapacity = 5000;

            clipSize = size * 8f * 3f;
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

        shippingContainer = new DrawerStorageBlock("shipping-container") {{
            requirements(Category.effect, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
            itemCapacity = 2000;
            coreMerge = false;

            clipSize = size * 8f * 3f;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    DrawItemSlot.mirrored(
                            new Vec2(12f / 4f, 22f / 4f),
                            new Vec2(22f / 4f, 12f / 4f)
                    ),
                    new DrawDefault(),
                    new DrawTeam()
            );
        }};

        providerContainer = new DrawerStorageBlock("provider-container") {{
            requirements(Category.effect, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
            itemCapacity = 200;
            coreMerge = false;
            flags = EnumSet.of(BlockFlag.storage, BlockFlag.extinguisher); // shh shh i cant override enums

            drawer = new DrawMulti(
                    new DrawDefault(),
                    new DrawTeam()
            );
        }};

        receiverContainer = new CDUnitCargoUnloadPoint("receiver-container") {{
            requirements(Category.effect, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
            itemCapacity = 200;
        }};
    }
}
