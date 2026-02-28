package carpediem.content.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import carpediem.content.*;
import carpediem.graphics.*;
import carpediem.world.blocks.payloads.*;
import carpediem.world.blocks.payloads.FanBlock.*;
import carpediem.world.blocks.units.*;
import carpediem.world.consumers.*;
import carpediem.world.draw.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

public class CDPayloadBlocks {
    public static Block
    payloadRail, payloadRailRouter, payloadCrane,
    payloadAssembler, payloadDisassembler, payloadManufacturingGrid,
    payloadDepot, payloadLoader, payloadUnloader,
    hydraulicFan, bulkHeater,
    springLauncher,
    landingPodAssembler;

    public static void load() {
        payloadRail = new PayloadConveyor("payload-rail") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 5,
                    CDItems.aluminumCogwheel, 2,
                    CDItems.aluminumWire, 2,
                    CDItems.siliconSheet, 2,
                    CDItems.controlCircuit, 1
            ));
            size = 5;
            payloadLimit = 6f; // huge
            moveTime = 60f;
            canOverdrive = false;
        }};

        payloadRailRouter = new PayloadRouter("payload-rail-router") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 8,
                    CDItems.aluminumCogwheel, 4,
                    CDItems.aluminumWire, 4,
                    CDItems.siliconSheet, 4,
                    CDItems.controlCircuit, 1
            ));
            size = 5;
            payloadLimit = 6f;
            moveTime = 60f;
            canOverdrive = false;
        }};

        payloadCrane = new PayloadCrane("payload-crane") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 180,
                    CDItems.aluminumPlate, 100,
                    CDItems.aluminumCogwheel, 75,
                    CDItems.aluminumRod, 50,
                    CDItems.aluminumWire, 100,
                    CDItems.nickelWire, 50,
                    Items.silicon, 100,
                    CDItems.siliconSheet, 10,
                    CDItems.controlCircuit, 60,
                    CDItems.calculationCircuit, 30,
                    CDItems.powerCell, 20
            ));
            size = 5;
            outlineColor = CDColors.outline;

            consumeLiquid(Liquids.oil, 0.1f);
            consumePower(1f);
        }};

        payloadAssembler = new Constructor("payload-assembler") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 200,
                    CDItems.aluminumPlate, 100,
                    CDItems.aluminumCogwheel, 50,
                    CDItems.aluminumWire, 40,
                    CDItems.nickelWire, 20,
                    Items.silicon, 50,
                    CDItems.siliconSheet, 25,
                    CDItems.controlCircuit, 50,
                    CDItems.calculationCircuit, 40,
                    CDItems.powerCell, 20
            ));
            size = 5;
            maxBlockSize = 3;

            consumeLiquid(Liquids.water, 0.2f);
            consumePower(2f);
        }};

        payloadDisassembler = new PayloadDeconstructor("payload-disassembler") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 200,
                    CDItems.aluminumPlate, 90,
                    CDItems.aluminumCogwheel, 80,
                    CDItems.aluminumWire, 40,
                    CDItems.nickelWire, 20,
                    Items.silicon, 50,
                    CDItems.siliconSheet, 25,
                    CDItems.controlCircuit, 50,
                    CDItems.calculationCircuit, 20,
                    CDItems.powerCell, 20
            ));
            size = 5;

            consumeLiquid(Liquids.water, 0.2f);
            consumePower(2f);
        }};

        payloadManufacturingGrid = new PayloadManufacturingGrid("payload-manufacturing-grid") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 70,
                    CDItems.aluminumPlate, 40,
                    CDItems.aluminumCogwheel, 20,
                    CDItems.aluminumWire, 10,
                    CDItems.nickelWire, 10,
                    Items.silicon, 20,
                    CDItems.siliconSheet, 10,
                    CDItems.controlCircuit, 20,
                    CDItems.calculationCircuit, 20,
                    CDItems.powerCell, 10
            ));
            size = 5;

            conductivePower = true;
            consumePower(1f / 5f);
        }};

        payloadDepot = new PayloadDepot("payload-depot") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 40,
                    CDItems.aluminumPlate, 30,
                    CDItems.aluminumCogwheel, 10,
                    CDItems.aluminumWire, 10,
                    Items.silicon, 10,
                    CDItems.siliconSheet, 10,
                    CDItems.controlCircuit, 5
            ));
            size = 5;
            payloadLimit = 6f;
        }};

        payloadLoader = new PayloadFrontLoader("payload-loader") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 100,
                    CDItems.aluminumPlate, 80,
                    CDItems.aluminumCogwheel, 50,
                    CDItems.nickelWire, 10,
                    Items.silicon, 70,
                    CDItems.controlCircuit, 20,
                    CDItems.calculationCircuit, 10,
                    CDItems.powerCell, 10
            ));
            size = 3;
            outlineColor = CDColors.outline;

            consumePower(2f);
        }};

        payloadUnloader = new PayloadFrontUnloader("payload-unloader") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 100,
                    CDItems.aluminumPlate, 55,
                    CDItems.aluminumCogwheel, 75,
                    CDItems.nickelWire, 10,
                    Items.silicon, 70,
                    CDItems.controlCircuit, 20,
                    CDItems.calculationCircuit, 10,
                    CDItems.powerCell, 10
            ));
            size = 3;
            outlineColor = CDColors.outline;

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
                    new DrawBlurSpin("-rotator", 8f) {
                        @Override
                        public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
                            // this is some bullshit!!!!!!!
                            Draw.rect(region, plan.drawx(), plan.drawy());
                        }
                    },
                    new DrawRotatedRegion()
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

        springLauncher = new UnitLauncher("spring-launcher") {{
            requirements(Category.effect, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;
            range = 500f * 8f;

            consumePower(5f);
        }};

        landingPodAssembler = new SingleBlockProducer("landing-pod-assembler") {{
            requirements(Category.units, new BuildVisibility(() -> Vars.state.rules.sector == CDSectorPresets.theReserve.sector), ItemStack.with(
                    CDItems.aluminum, 500,
                    CDItems.aluminumPlate, 200,
                    CDItems.aluminumCogwheel, 50,
                    CDItems.nickelPlate, 100,
                    CDItems.nickelWire, 100,
                    CDItems.powerCell, 50,
                    CDItems.controlCircuit, 50
            ));
            size = 5;

            consumePower(2f);
        }};
    }
}
