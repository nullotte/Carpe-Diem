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
            requirements(Category.crafting, ItemStack.with(
                    CDItems.rawAluminum, 20
            ));
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
            requirements(Category.crafting, ItemStack.with(
                    CDItems.aluminum, 20,
                    CDItems.aluminumRod, 20,
                    CDItems.nickelWire, 5,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 10
            ));
            size = 4;

            recipes.addAll(CDRecipes.basicSmelterRecipes).addAll(CDRecipes.advancedSmelterRecipes);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawDefault()
            );

            consumePower(1f / 12f);
        }};

        pressT1 = new RecipeCrafter("press-t1") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.aluminum, 25,
                    CDItems.aluminumPlate, 10,
                    CDItems.aluminumCogwheel, 5,
                    CDItems.nickelWire, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 5
            ));
            size = 4;

            recipes.addAll(CDRecipes.pressRecipes);

            consumePower(1f / 12f);
        }};

        rollingMillT1 = new RecipeCrafter("rolling-mill-t1") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.aluminum, 25,
                    CDItems.aluminumRod, 20,
                    CDItems.aluminumCogwheel, 10,
                    CDItems.nickelWire, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 5
            ));
            size = 4;

            recipes.addAll(CDRecipes.rollingMillRecipes);
            // rolling mill has configurable true too because of the rods and wires recipe thingy
            configurable = true;

            consumePower(1f / 12f);
        }};

        assemblerT1 = new RecipeCrafter("assembler-t1") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.aluminum, 20,
                    CDItems.aluminumPlate, 5,
                    CDItems.aluminumRod, 15,
                    CDItems.aluminumCogwheel, 20,
                    CDItems.nickelWire, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 5
            ));
            size = 4;

            recipes.addAll(CDRecipes.assemblerRecipes);
            // FUCKKKKKKKKK
            configurable = true;

            consumePower(1f / 10f);
        }};

        refineryT1 = new RecipeCrafter("refinery-t1") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 4;

            recipes.addAll(CDRecipes.refineryRecipes);
            configurable = true;

            consumePower(1f / 10f);
        }};
        // endregion
    }
}
