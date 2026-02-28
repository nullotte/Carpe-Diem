package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.power.*;
import carpediem.world.consumers.*;
import carpediem.world.draw.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDPower {
    public static Block
    cableNode, cableTower, accumulator,
    geothermalBurner, steamBoiler, compressionEngine;

    public static void load() {
        cableNode = new CableNode("cable-node") {{
            requirements(Category.power, ItemStack.with(
                    CDItems.aluminum, 1,
                    CDItems.aluminumRod, 1,
                    CDItems.nickelWire, 1
            ));
            laserScale = 0.4f;
            maxNodes = 20;
            laserRange = 10f;
        }};

        cableTower = new CableNode("cable-tower") {{
            requirements(Category.power, ItemStack.with(
                    CDItems.aluminum, 10,
                    CDItems.aluminumRod, 5,
                    CDItems.nickelWire, 10,
                    CDItems.nickelPlate, 5,
                    Items.silicon, 3
            ));
            size = 2;
            topOffset = 4f;
            laserScale = 0.4f;
            maxNodes = 4;
            laserRange = 40f;
        }};

        accumulator = new CableBattery("accumulator") {{
            requirements(Category.power, ItemStack.with(
                    CDItems.aluminum, 25,
                    CDItems.aluminumPlate, 10,
                    CDItems.nickelPlate, 20,
                    CDItems.nickelWire, 5,
                    Items.silicon, 10,
                    CDItems.powerCell, 20
            ));
            size = 3;
            consumePowerBuffered(5000f);

            topOffset = 9f;
            drawer = new DrawMulti(
                    new DrawDefault(),
                    new DrawRegion("-top") {{
                        layer = Layer.power + 0.01f;
                    }},
                    new DrawCableGlow()
            );
        }};

        geothermalBurner = new ThermalConsumeGenerator("geothermal-burner") {{
            requirements(Category.power, ItemStack.with(
                    CDItems.aluminum, 20,
                    CDItems.nickelPlate, 10,
                    CDItems.nickelRod, 5,
                    CDItems.nickelWire, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 10
            ));
            size = 3;
            itemCapacity = 50;

            powerProduction = (200f / 60f) / 9f;
            itemDuration = 120f;
            displayEfficiencyScale = 1f / 9f;

            topOffset = 8.1f;
            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawRegion("-rotator", 5f),
                    new DrawDefault(),
                    new DrawRegion("-top") {{
                        layer = Layer.power + 0.01f;
                    }},
                    new DrawCableGlow()
            );

            consumeItem(CDItems.sulfur);
        }};

        steamBoiler = new CableConsumeGenerator("steam-boiler") {{
            requirements(Category.power, ItemStack.with(
                    CDItems.aluminum, 50,
                    CDItems.aluminumCogwheel, 15,
                    CDItems.nickelPlate, 25,
                    CDItems.nickelWire, 25,
                    CDItems.silverPlate, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 15,
                    CDItems.fluidCell, 5
            ));
            size = 5;

            powerProduction = 30f;

            topOffset = 14.5f;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),

                    new DrawRegion("-rotator", 5f, true),
                    new DrawRegion("-rotator", 5f, true) {{
                        rotation = 22.5f;
                    }},
                    new DrawRegion("-rotator", 5f, true) {{
                        rotation = 45f;
                    }},
                    new DrawRegion("-rotator", 5f, true) {{
                        rotation = 67.5f;
                    }},

                    new DrawDefault(),
                    new DrawRegion("-top") {{
                        layer = Layer.power + 0.01f;
                    }},
                    new DrawCableGlow()
            );

            consumeLiquid(Liquids.water, 0.1f);
            consumeItems(ItemStack.with(CDItems.sulfur, 2, CDItems.tar, 1));
        }};

        compressionEngine = new CableConsumeGenerator("compression-engine") {{
            requirements(Category.power, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 6;

            powerProduction = 150f;
            itemDuration = 4f * 60f;

            topOffset = 14.5f;
            drawer = new DrawMulti(
                    new DrawDefault()
            );

            consumeItem(Items.pyratite, 1);
            consume(new ConsumePressure(20f));
        }};
    }
}
