package carpediem.content.blocks;

import arc.graphics.*;
import arc.struct.*;
import carpediem.content.*;
import carpediem.world.blocks.payloads.*;
import carpediem.world.blocks.payloads.FanBlock.*;
import carpediem.world.consumers.*;
import carpediem.world.draw.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;

public class CDPayloadBlocks {
    public static Block
    payloadRail, payloadRailRouter, payloadCrane,
    payloadAssembler, payloadDisassembler, payloadManufacturingGrid,
    payloadLoader, payloadUnloader,
    hydraulicFan, bulkHeater;

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

        payloadAssembler = new Constructor("payload-assembler") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;
            maxBlockSize = 3;

            consumeLiquid(Liquids.water, 0.2f);
            consumePower(2f);
        }};

        payloadDisassembler = new PayloadDeconstructor("payload-disassembler") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;

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

        hydraulicFan = new FanBlock("hydraulic-fan") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;

            ambientSound = Sounds.wind;

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawBlurSpin("-rotator", 8f),
                    new DrawRotatedRegion(true)
            );

            consume(new ConsumePressure());
        }};

        bulkHeater = new PayloadBurner("bulk-heater") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;

            ((FanBlock) hydraulicFan).processingTypes.add(new FanProcessingType(
                    this,
                    60f * 20f,
                    Color.orange
            ));

            topDrawer = new DrawMulti(
                    new DrawBetterWarmupRegion() {{
                        sinMag = 0f;
                        color = Color.orange;
                    }},
                    new DrawGlowRegion(Layer.blockOver + 0.2f) {{
                        color = Pal.turretHeat;
                        glowIntensity = 0f;
                        alpha = 1f;
                    }}
            );

            // stupid!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            consume(new Consume() {});
        }};
    }
}
