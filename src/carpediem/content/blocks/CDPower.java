package carpediem.content.blocks;

import carpediem.world.blocks.power.*;
import mindustry.type.*;
import mindustry.world.*;

public class CDPower {
    // wind3
    public static Block
    cableNode, cableTower,
    burnerGenerator, steamTurbineGenerator, gasTurbineGenerator;

    public static void load() {
        cableNode = new CableNode("cable-node") {{
            requirements(Category.power, ItemStack.with());
            laserScale = 0.4f;
            maxNodes = 20;
            laserRange = 10f;
        }};

        cableTower = new CableNode("cable-tower") {{
            requirements(Category.power, ItemStack.with());
            size = 2;
            laserScale = 0.4f;
            maxNodes = 4;
            laserRange = 40f;
        }};
    }
}
