package carpediem.content.blocks;

import arc.struct.*;
import carpediem.world.blocks.payloads.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

public class CDPayloads {
    public static Block payloadCrane, payloadManufacturingPlant, payloadManufacturingComponent;

    public static void load() {
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
