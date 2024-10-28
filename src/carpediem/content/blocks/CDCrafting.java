package carpediem.content.blocks;

import arc.struct.*;
import carpediem.content.*;
import carpediem.world.blocks.production.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;

public class CDCrafting {
    public static Block
    // crude crafting
    crudeSmelter, crudePress, crudeRollingMill,
    // normal crafting - maybe get better names for these later...
    smelter, press, rollingMill,
    mixer, refinery, assemblyDepot;

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

            recipes.add(
                    new CraftingRecipe(
                            new ItemStack(CDItems.biomass, 2),
                            new ItemStack(CDItems.charcoal, 2)
                    )
            );

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

        mixer = new RecipeCrafter("mixer") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = Seq.with(
                    new CraftingRecipe(
                            ItemStack.with(Items.sand, 1, CDItems.bitumen, 2, CDItems.sulfur, 2),
                            ItemStack.with(Items.pyratite, 6)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(Items.sand, 2, CDItems.charcoal, 2, Items.pyratite, 1),
                            ItemStack.with(Items.silicon, 4)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.silver, 3, CDItems.charcoal, 1, Items.pyratite, 1),
                            ItemStack.with(CDItems.unnamedAlloy, 4)
                    )
            );

            consumePower(2f);
        }};

        refinery = new RecipeCrafter("refinery") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = Seq.with(
                    new CraftingRecipe() {{
                        inputItems = ItemStack.with(CDItems.waterIce, 3);
                        outputLiquids = LiquidStack.with(Liquids.water, 0.2f);
                    }},
                    new CraftingRecipe() {{
                        inputItems = ItemStack.with(CDItems.bitumen, 2);
                        inputLiquids = LiquidStack.with(CDLiquids.petroleum, 0.2f);
                        outputLiquids = LiquidStack.with(Liquids.oil, 0.2f);
                    }},
                    new CraftingRecipe(
                            new ItemStack(CDItems.aluminum, 2),
                            new ItemStack(Items.plastanium, 3)
                    ) {{
                        inputLiquids = LiquidStack.with(Liquids.oil, 0.2f);
                    }}
            );

            consumePower(2f);
        }};
        // endregion
    }
}
