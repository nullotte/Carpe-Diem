package carpediem.content.blocks;

import arc.struct.*;
import carpediem.world.blocks.payloads.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;

public class CDPayloads {
    public static Block
    payloadRail, payloadRailRouter, payloadCrane,
    payloadAssembler, payloadManufacturingGrid;

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

        payloadCrane = new PayloadCrane("payload-crane") {{
            requirements(Category.units, ItemStack.with());
            size = 5;
            outlineColor = Pal.darkOutline;
            consumePower(1f);
        }};

        payloadAssembler = new PayloadComponentConstructor("payload-assembler") {{
            requirements(Category.units, ItemStack.with());
            size = maxBlockSize = 5;
            filter = Seq.with(CDStorage.landingPodT0);

            consumePower(2f);
        }};

        payloadManufacturingGrid = new PayloadManufacturingGrid("payload-manufacturing-grid") {{
            requirements(Category.units, ItemStack.with());
            size = 5;

            recipes = Seq.with(
                    new PayloadManufacturingRecipe(UnitTypes.conquer, r -> {
                        Block c = Blocks.carbideWallLarge;
                        UnitType l = UnitTypes.locus;

                        r.mapRequirements(new UnlockableContent[][]{
                                {c, c, c, c, c},
                                {c, l, c, l, c},
                                {c, l, c, l, c},
                                {c, l, c, l, c},
                                {c, c, c, c, c}
                        });
                    })
            );

            conductivePower = true;
            consumePower(1f / 5f);
        }};
    }
}
