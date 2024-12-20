package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.crafting.*;
import carpediem.world.consumers.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDCrafting {
    public static Block
    // T0
    smelterT0,
    // T1
    smelterT1, pressT1, rollingMillT1, refineryT1, assemblerT1;
    // TODO T2
    // also drawers . literally none of these blocks have sprites or visuals at all rn

    public static void load() {
        // a special one .
        smelterT0 = new RecipeCrafter("smelter-t0") {{
            requirements(Category.crafting, ItemStack.with(CDItems.rawAluminum, 20));
            alwaysUnlocked = true;
            size = 3;

            craftingSpeed = 0.5f;
            recipes.addAll(CDRecipes.basicSmelterRecipes);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawDefault()
            );

            consume(new ConsumeItemsUses(7, ItemStack.with(CDItems.sulfur, 1)));
        }};

        // region T1
        smelterT1 = new RecipeCrafter("smelter-t1") {{
            requirements(Category.crafting, ItemStack.with(CDItems.lemon, 39));
            size = 4;

            recipes.addAll(CDRecipes.basicSmelterRecipes).addAll(CDRecipes.advancedSmelterRecipes);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawDefault()
            );

            consumePower(1f / 12f);
        }};

        pressT1 = new RecipeCrafter("press-t1") {{
            requirements(Category.crafting, ItemStack.with(CDItems.lemon, 39));
            size = 4;

            recipes.addAll(CDRecipes.pressRecipes);

            consumePower(1f / 12f);
        }};

        rollingMillT1 = new RecipeCrafter("rolling-mill-t1") {{
            requirements(Category.crafting, ItemStack.with(CDItems.lemon, 39));
            size = 4;

            recipes.addAll(CDRecipes.rollingMillRecipes);

            consumePower(1f / 12f);
        }};

        refineryT1 = new RecipeCrafter("refinery-t1") {{
            requirements(Category.crafting, ItemStack.with(CDItems.lemon, 39));
            size = 4;

            recipes.addAll(CDRecipes.refineryRecipes);

            consumePower(1f / 10f);
        }};

        assemblerT1 = new RecipeCrafter("assembler-t1") {{
            requirements(Category.crafting, ItemStack.with(CDItems.lemon, 39));
            size = 4;

            recipes.addAll(CDRecipes.assemblerRecipes);

            consumePower(1f / 10f);
        }};
        // endregion
    }
}
