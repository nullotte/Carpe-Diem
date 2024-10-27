package carpediem.content;

import arc.struct.*;
import carpediem.world.blocks.payloads.*;
import carpediem.world.blocks.production.*;
import carpediem.world.consumers.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

public class CDBlocks {
    public static Block
    // crude crafting
    crudeSmelter, crudePress, crudeRollingMill,
    // crafting - maybe get better names for these later...
    smelter, press, rollingMill,
    crusher, polishingWheel,
    // payloads
    payloadCrane, payloadManufacturingPlant, payloadManufacturingComponent;

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

            consume(new ConsumeDurability(20));
            consumePower(1f);
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

            consume(new ConsumeDurability(20));
            consumePower(1f);
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

            consume(new ConsumeDurability(20));
            consumePower(1f);
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
        // region payloads
        payloadCrane = new PayloadCrane("payload-crane") {{
            requirements(Category.units, ItemStack.with());
            size = 5;
            outlineColor = Pal.darkOutline;
            consumePower(1f);
        }};

        payloadManufacturingPlant = new PayloadManufacturingPlant("payload-manufacturing-plant") {{
            requirements(Category.units, ItemStack.with());
            regionSuffix = "-dark";
            size = 5;

            recipes = Seq.with(
                    new PayloadManufacturingRecipe(
                            new String[]{
                                    "CCCCC",
                                    "CLCLC",
                                    "CLCLC",
                                    "CLCLC",
                                    "CCCCC"
                            },
                            ObjectMap.of(
                                    'L', UnitTypes.locus,
                                    'C', Blocks.carbideWallLarge
                            ),
                            UnitTypes.conquer
                    )
            );
        }};

        payloadManufacturingComponent = new PayloadManufacturingComponent("payload-manufacturing-component") {{
            requirements(Category.units, ItemStack.with());
            size = 3;
        }};
        // endregion
    }
}
