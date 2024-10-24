package nowhere.content;

import arc.struct.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import nowhere.world.blocks.payloads.*;

public class ModBlocks {
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
            size = 5;

            recipes = Seq.with(
                    new PayloadManufacturingRecipe(
                            new String[]{
                                    "CCC",
                                    "CTC",
                                    "HHH"
                            },
                            ObjectMap.of(
                                    'C', Blocks.copperWallLarge,
                                    'T', Blocks.titaniumWallLarge,
                                    'H', Blocks.thoriumWallLarge
                            ),
                            UnitTypes.zenith
                    )
            );
        }};

        payloadManufacturingComponent = new PayloadManufacturingComponent("payload-manufacturing-component") {{
            requirements(Category.units, ItemStack.with());
            size = 3;
        }};
    }
}
