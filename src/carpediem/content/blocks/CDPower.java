package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.power.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDPower {
    public static Block
    cableNode, cableTower, accumulator,
    geothermalBurner, steamBoiler;

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
                    CDItems.lemon, 39
            ));
            size = 2;
            topOffset = 4f;
            laserScale = 0.4f;
            maxNodes = 4;
            laserRange = 40f;
        }};

        accumulator = new CableBattery("accumulator") {{
            requirements(Category.power, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
            consumePowerBuffered(5000f);

            topOffset = 9f;
            drawer = new DrawMulti(
                    new DrawDefault(),
                    new DrawRegion("-top") {{
                        layer = Layer.power + 0.01f;
                    }},
                    new DrawGlowRegion(Layer.power + 0.02f) {{
                        color = Pal.turretHeat;
                        glowIntensity = 0f;
                    }}
            );
        }};

        geothermalBurner = new ThermalConsumeGenerator("geothermal-burner") {{
            requirements(Category.power, ItemStack.with(
                    CDItems.aluminum, 20,
                    CDItems.nickelPlate, 20,
                    CDItems.nickelRod, 25,
                    CDItems.nickelWire, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 20
            ));
            size = 3;

            powerProduction = (100f / 60f) / 9f;
            itemDuration = 60f;
            displayEfficiencyScale = 1f / 9f;

            topOffset = 8f;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawRegion("-rotator", 5f),
                    new DrawDefault(),
                    new DrawRegion("-top") {{
                        layer = Layer.power + 0.01f;
                    }},
                    new DrawGlowRegion(Layer.power + 0.02f) {{
                        color = Pal.turretHeat;
                        glowIntensity = 0f;
                    }}
            );

            consumeItem(CDItems.sulfur);
        }};
    }
}
