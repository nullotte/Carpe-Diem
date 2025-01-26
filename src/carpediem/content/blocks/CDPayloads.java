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
                    CDItems.aluminum, 10,
                    CDItems.aluminumPlate, 20,
                    CDItems.aluminumRod, 5,
                    CDItems.aluminumCogwheel, 5,
                    CDItems.aluminumWire, 5,
                    CDItems.nickelWire, 5
            ));
            size = 5;
            payloadLimit = 6f; // huge
            moveTime = 60f;
            canOverdrive = false;
        }};

        payloadRailRouter = new PayloadRouter("payload-rail-router") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 20,
                    CDItems.aluminumPlate, 20,
                    CDItems.aluminumRod, 10,
                    CDItems.aluminumCogwheel, 10,
                    CDItems.aluminumWire, 10,
                    CDItems.nickelWire, 5
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
                    CDItems.aluminum, 150,
                    CDItems.aluminumPlate, 100,
                    CDItems.aluminumRod, 60,
                    CDItems.nickelRod, 40,
                    CDItems.aluminumCogwheel, 25,
                    CDItems.aluminumWire, 30,
                    CDItems.nickelWire, 30,
                    CDItems.controlCircuit, 10,
                    CDItems.powerCell, 10
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
            size = 7;
            maxBlockSize = 6;

            consumePower(2f);
        }};

        payloadUnloader = new PayloadUnloader("payload-unloader") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 7;
            maxBlockSize = 6;

            consumePower(2f);
        }};
    }
}
