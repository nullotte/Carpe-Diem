package carpediem.content.blocks;

import arc.struct.*;
import carpediem.content.*;
import carpediem.world.blocks.production.*;
import carpediem.world.consumers.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDCrafting {
    public static Block
    // T0
    smelterT0, pressT0, rollingMillT0, assemblerT0,
    // T1
    smelterT1, pressT1, rollingMillT1, refineryT1, assemblerT1;
    // TODO T2
    // also drawers . literally none of these blocks have sprites or visuals at all rn

    public static OrderedMap<Item, Item> rawItems, plates, rods, wires;

    public static void load() {
        rawItems = OrderedMap.of(
                CDItems.rawAluminum, CDItems.aluminum,
                CDItems.rawNickel, CDItems.nickel,
                CDItems.rawSilver, CDItems.silver,
                CDItems.rawPlatinum, CDItems.platinum
        );

        plates = OrderedMap.of(
                CDItems.aluminum, CDItems.aluminumPlate,
                CDItems.nickel, CDItems.nickelPlate,
                CDItems.silver, CDItems.silverPlate,
                CDItems.platinum, CDItems.platinumPlate,
                CDItems.sturdyAlloy, CDItems.alloyPlate,
                Items.silicon, CDItems.siliconSheet,
                Items.plastanium, CDItems.plastaniumSheet
        );

        rods = OrderedMap.of(
                CDItems.aluminum, CDItems.aluminumRod,
                CDItems.nickel, CDItems.nickelRod,
                CDItems.silver, CDItems.silverRod,
                CDItems.platinum, CDItems.platinumRod,
                CDItems.sturdyAlloy, CDItems.alloyRod
        );

        wires = OrderedMap.of(
                CDItems.aluminumRod, CDItems.aluminumWire,
                CDItems.nickelRod, CDItems.nickelWire
        );

        // region T0
        smelterT0 = new RecipeCrafter("smelter-t0") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = new Seq<>();
            rawItems.each((in, out) -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(in, 2),
                            new ItemStack(out, 1)
                    )
            ));

            consume(new ConsumeItemsUses(7, ItemStack.with(CDItems.sulfur, 1)));
        }};

        pressT0 = new RecipeCrafter("press-t0") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = new Seq<>();
            plates.each((in, out) -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(in, 1),
                            new ItemStack(out, 1)
                    )
            ));

            consume(new ConsumeItemsUses(7, ItemStack.with(CDItems.sulfur, 1)));
        }};

        rollingMillT0 = new RecipeCrafter("rolling-mill-t0") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = new Seq<>();
            rods.each((in, out) -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(in, 1),
                            new ItemStack(out, 2)
                    )
            ));
            // i am not a good coder
            rawItems.each((in, out) -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(in, 1),
                            new ItemStack(out, 2)
                    )
            ));

            consume(new ConsumeItemsUses(7, ItemStack.with(CDItems.sulfur, 1)));
        }};

        assemblerT0 = new RecipeCrafter("assembler-t0") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = Seq.with(
                    new CraftingRecipe(
                            ItemStack.with(CDItems.aluminumPlate, 1, CDItems.nickelWire, 5),
                            ItemStack.with(CDItems.controlCircuit, 1)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.lemon, 39),
                            ItemStack.with(CDItems.calculationCircuit, 1)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.lemon, 39),
                            ItemStack.with(CDItems.processingUnit, 1)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.aluminum, 1, CDItems.aluminumPlate, 4),
                            ItemStack.with(CDItems.aluminumCogwheel, 6)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.silver, 1, CDItems.silverPlate, 4),
                            ItemStack.with(CDItems.silverCogwheel, 6)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.sturdyAlloy, 1, CDItems.alloyPlate, 4),
                            ItemStack.with(CDItems.alloyCogwheel, 6)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.aluminum, 2, CDItems.nickelPlate, 2, CDItems.sulfur, 1),
                            ItemStack.with(CDItems.powerCell, 1)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.lemon, 39),
                            ItemStack.with(CDItems.liquidCell, 1)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.lemon, 39),
                            ItemStack.with(CDItems.electronicMotor, 1)
                    )
            );

            consume(new ConsumeItemsUses(7, ItemStack.with(CDItems.sulfur, 1)));
        }};
        // endregion
        // region T1
        smelterT1 = new RecipeCrafter("smelter-t1") {{
            requirements(Category.crafting, ItemStack.with());
            size = 4;

            recipes = new Seq<>();
            rawItems.each((in, out) -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(in, 3),
                            new ItemStack(out, 3)
                    )
            ));

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawDefault()
            );

            consumePower(2f);
        }};

        pressT1 = new RecipeCrafter("press-t1") {{
            requirements(Category.crafting, ItemStack.with());
            size = 4;

            recipes = new Seq<>();
            plates.each((in, out) -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(in, 2),
                            new ItemStack(out, 2)
                    )
            ));

            consumePower(2f);
        }};

        rollingMillT1 = new RecipeCrafter("rolling-mill-t1") {{
            requirements(Category.crafting, ItemStack.with());
            size = 4;

            recipes = new Seq<>();
            rods.each((in, out) -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(in, 3),
                            new ItemStack(out, 6)
                    )
            ));
            wires.each((in, out) -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(in, 3),
                            new ItemStack(out, 6)
                    )
            ));

            consumePower(2f);
        }};

        refineryT1 = new RecipeCrafter("refinery-t1") {{
            requirements(Category.crafting, ItemStack.with());
            size = 4;

            recipes = Seq.with(
                    new CraftingRecipe() {{
                        inputItems = ItemStack.with(CDItems.waterIce, 3);
                        outputLiquids = LiquidStack.with(Liquids.water, 0.2f);
                    }},
                    new CraftingRecipe() {{
                        inputLiquids = LiquidStack.with(CDLiquids.petroleum, 0.2f);
                        outputLiquids = LiquidStack.with(Liquids.oil, 0.2f);
                    }},
                    new CraftingRecipe(
                            ItemStack.with(Items.sand, 2),
                            ItemStack.with(Items.silicon, 4)
                    ) {{
                        inputLiquids = LiquidStack.with(Liquids.oil, 0.2f);
                    }},
                    new CraftingRecipe(
                            ItemStack.with(Items.sand, 1, CDItems.sulfur, 2),
                            ItemStack.with(Items.pyratite, 4)
                    ) {{
                        inputLiquids = LiquidStack.with(Liquids.oil, 0.2f);
                    }},
                    new CraftingRecipe(
                            ItemStack.with(CDItems.aluminum, 2, Items.silicon, 2, Items.pyratite, 1),
                            ItemStack.with(CDItems.sturdyAlloy, 4)
                    ) {{
                        inputLiquids = LiquidStack.with(Liquids.water, 0.1f);
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

        assemblerT1 = new RecipeCrafter("assembler-t1") {{
            requirements(Category.crafting, ItemStack.with());
            size = 4;

            recipes = Seq.with(
                    new CraftingRecipe(
                            ItemStack.with(CDItems.aluminumPlate, 1, CDItems.nickelWire, 5),
                            ItemStack.with(CDItems.controlCircuit, 1)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.lemon, 39),
                            ItemStack.with(CDItems.calculationCircuit, 1)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.lemon, 39),
                            ItemStack.with(CDItems.processingUnit, 1)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.aluminum, 1, CDItems.aluminumPlate, 4),
                            ItemStack.with(CDItems.aluminumCogwheel, 6)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.silver, 1, CDItems.silverPlate, 4),
                            ItemStack.with(CDItems.silverCogwheel, 6)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.sturdyAlloy, 1, CDItems.alloyPlate, 4),
                            ItemStack.with(CDItems.alloyCogwheel, 6)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.aluminum, 2, CDItems.nickelPlate, 2, CDItems.sulfur, 1),
                            ItemStack.with(CDItems.powerCell, 1)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.lemon, 39),
                            ItemStack.with(CDItems.liquidCell, 1)
                    ),
                    new CraftingRecipe(
                            ItemStack.with(CDItems.lemon, 39),
                            ItemStack.with(CDItems.electronicMotor, 1)
                    )
            );

            consumePower(2f);
        }};
        // endregion
    }
}
