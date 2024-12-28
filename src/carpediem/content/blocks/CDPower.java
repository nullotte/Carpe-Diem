package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.power.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;

public class CDPower {
    public static Block
    cableNode, cableTower, accumulator,
    // TODO names
    sulfurBurner, steamBoiler, fuelGenerator;

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

        accumulator = new Battery("accumulator") {{
            requirements(Category.power, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
            consumePowerBuffered(5000f);
        }};

        sulfurBurner = new ConsumeGenerator("sulfur-burner") {{
            requirements(Category.power, ItemStack.with(
                    CDItems.aluminum, 20,
                    CDItems.nickelPlate, 20,
                    CDItems.nickelRod, 25,
                    CDItems.nickelWire, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 20
            ));
            size = 3;
            powerProduction = 2f;

            consumeItem(CDItems.sulfur);
        }};
    }
}
