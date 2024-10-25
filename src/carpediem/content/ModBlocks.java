package carpediem.content;

import arc.struct.*;
import carpediem.world.blocks.payloads.*;
import carpediem.world.blocks.production.*;
import carpediem.world.consumers.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

public class ModBlocks {
    public static Block
            crudeSmelter,
            payloadCrane, payloadManufacturingPlant, payloadManufacturingComponent;

    public static void load() {
        crudeSmelter = new RecipeCrafter("crude-smelter") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = Seq.with(
                    // lol imagine not having a recipe to craft with
            );

            // obviously you will remember but like. tweak this later ok
            consume(new ConsumeDamage(18f));
            consumePower(1f);
        }};

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
    }
}
