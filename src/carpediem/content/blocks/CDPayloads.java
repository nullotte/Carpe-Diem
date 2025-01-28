package carpediem.content.blocks;

import arc.struct.*;
import carpediem.content.*;
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
    payloadAssembler, payloadManufacturingGrid,
    payloadLoader, payloadUnloader;

    public static void load() {
        payloadRail = new PayloadConveyor("payload-rail") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;
            payloadLimit = 6f; // huge
            moveTime = 60f;
            canOverdrive = false;
        }};

        payloadRailRouter = new PayloadRouter("payload-rail-router") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;
            payloadLimit = 6f;
            moveTime = 60f;
            canOverdrive = false;
        }};

        payloadCrane = new PayloadCrane("payload-crane") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;
            outlineColor = Pal.darkOutline;

            consumeLiquid(Liquids.oil, 0.1f);
            consumePower(1f);
        }};

        payloadAssembler = new PayloadComponentConstructor("payload-assembler") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = maxBlockSize = 5;
            filter = Seq.with(payloadRail);

            consumeLiquid(Liquids.water, 0.2f);
            consumePower(2f);
        }};

        payloadManufacturingGrid = new PayloadManufacturingGrid("payload-manufacturing-grid") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
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

        payloadLoader = new PayloadLoader("payload-loader") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;
            maxBlockSize = 6;

            consumePower(2f);
        }};

        payloadUnloader = new PayloadUnloader("payload-unloader") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;
            maxBlockSize = 6;

            consumePower(2f);
        }};
    }
}
