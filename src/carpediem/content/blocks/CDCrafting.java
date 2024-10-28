package carpediem.content.blocks;

import arc.struct.*;
import carpediem.content.*;
import carpediem.world.blocks.production.*;
import mindustry.type.*;
import mindustry.world.*;

public class CDCrafting {
    public static Block
    // crude crafting
    crudeSmelter, crudePress, crudeRollingMill,
    // normal crafting - maybe get better names for these later...
    smelter, press, rollingMill,
    crusher, mixer, refinery;

    public static void load() {
        // region crude crafting
        crudeSmelter = new RecipeCrafter("crude-smelter") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = new Seq<>();
            CDItems.pureItems.each(item -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(CDItems.rawItems.get(item), 2),
                            new ItemStack(item, 1)
                    )
            ));

            consumeItem(CDItems.bitumen, 1);
        }};

        crudePress = new RecipeCrafter("crude-press") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = new Seq<>();
            CDItems.pureItems.each(item -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(item, 1),
                            new ItemStack(CDItems.plates.get(item), 1)
                    )
            ));

            consumeItem(CDItems.bitumen, 1);
        }};

        crudeRollingMill = new RecipeCrafter("crude-rolling-mill") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = new Seq<>();
            CDItems.pureItems.each(item -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(item, 1),
                            new ItemStack(CDItems.rods.get(item), 2)
                    )
            ));
            // i am not a good coder
            CDItems.pureItems.each(item -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(CDItems.rods.get(item), 1),
                            new ItemStack(CDItems.wires.get(item), 2)
                    )
            ));

            consumeItem(CDItems.bitumen, 1);
        }};
        // endregion
        // region crafting
        smelter = new RecipeCrafter("smelter") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = new Seq<>();
            CDItems.pureItems.each(item -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(CDItems.rawItems.get(item), 3),
                            new ItemStack(item, 3)
                    )
            ));

            consumePower(2f);
        }};

        press = new RecipeCrafter("press") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = new Seq<>();
            CDItems.pureItems.each(item -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(item, 2),
                            new ItemStack(CDItems.plates.get(item), 2)
                    )
            ));

            consumePower(2f);
        }};

        rollingMill = new RecipeCrafter("rolling-mill") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = new Seq<>();
            CDItems.pureItems.each(item -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(item, 3),
                            new ItemStack(CDItems.rods.get(item), 6)
                    )
            ));
            CDItems.pureItems.each(item -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(CDItems.rods.get(item), 3),
                            new ItemStack(CDItems.wires.get(item), 6)
                    )
            ));

            consumePower(2f);
        }};
        // endregion
    }
}
