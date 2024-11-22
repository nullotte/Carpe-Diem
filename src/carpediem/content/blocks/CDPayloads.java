package carpediem.content.blocks;

import arc.struct.*;
import carpediem.world.blocks.payloads.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;

public class CDPayloads {
    public static Block
    payloadRail, payloadRailRouter,
    payloadAssembler,
    payloadCrane, payloadManufacturingPlant, payloadManufacturingGrid;

    public static void load() {
        payloadRail = new PayloadConveyor("payload-rail") {{
            requirements(Category.units, ItemStack.with());
            size = 5;
            payloadLimit = 6f; // huge
            moveTime = 60f;
            canOverdrive = false;
        }};

        payloadRailRouter = new PayloadRouter("payload-rail-router") {{
            requirements(Category.units, ItemStack.with());
            size = 5;
            payloadLimit = 6f;
            moveTime = 60f;
            canOverdrive = false;
        }};

        payloadAssembler = new PayloadComponentConstructor("payload-assembler") {{
            requirements(Category.units, ItemStack.with());
            size = maxBlockSize = 5;
            filter = Seq.with(CDStorage.landingPodT0);

            consumePower(2f);
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

        payloadManufacturingGrid = new PayloadManufacturingGrid("payload-manufacturing-grid") {{
            requirements(Category.units, ItemStack.with());
            size = 5;
        }};
    }
}
