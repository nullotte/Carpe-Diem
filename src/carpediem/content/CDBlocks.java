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
            crudeSmelter,
            payloadCrane, payloadManufacturingPlant, payloadManufacturingComponent;

    public static void load() {
        crudeSmelter = new RecipeCrafter("crude-smelter") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;

            recipes = new Seq<>();
            CDItems.pureItems.each(item -> recipes.add(
                    new CraftingRecipe(
                            new ItemStack(CDItems.rawItems.get(item), 1),
                            new ItemStack(item, 1)
                    )
            ));

            consume(new ConsumeDurability(20));
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
