package carpediem.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;

public class CDItems {
    public static Item
    rawAluminum, rawNickel, rawSilver, rawPlatinum,
    sulfur, waterIce, biomass, charcoal,

    aluminum, nickel, silver, platinum, carbonAlloy,

    aluminumPlate, nickelPlate, silverPlate, platinumPlate, alloyPlate, siliconSheet, plastaniumSheet,
    aluminumRod, nickelRod, silverRod, platinumRod, alloyRod,
    aluminumWire, nickelWire, silverWire, platinumWire,

    controlCircuit, calculationCircuit, processingUnit,
    aluminumCogwheel, silverCogwheel, alloyCogwheel,
    powerCell, liquidCell, electronicMotor,

    lemon
    ;
    // + sand, silicon, pyratite, plastanium
    // TODO stats?

    public static void load() {
        rawAluminum = new Item("raw-aluminum", Color.valueOf("948fbf"));
        rawNickel = new Item("raw-nickel", Color.valueOf("cbb07e"));
        rawSilver = new Item("raw-silver", Color.valueOf("8dabd4"));
        rawPlatinum = new Item("raw-platinum", Color.valueOf("d985a3"));

        sulfur = new Item("sulfur", Color.valueOf("dece5e")) {{

        }};

        waterIce = new Item("water-ice") {{

        }};

        biomass = new Item("biomass") {{

        }};

        charcoal = new Item("charcoal", Color.valueOf("32312c")) {{

        }};

        aluminum = new Item("aluminum", rawAluminum.color) {{

        }};

        nickel = new Item("nickel", rawNickel.color) {{

        }};

        silver = new Item("silver", rawSilver.color) {{

        }};

        platinum = new Item("platinum", rawPlatinum.color) {{

        }};

        carbonAlloy = new Item("carbon-alloy", Color.valueOf("6b758b")) {{

        }};

        aluminumPlate = new Item("aluminum-plate", aluminum.color);
        nickelPlate = new Item("nickel-plate", nickel.color);
        silverPlate = new Item("silver-plate", silver.color);
        platinumPlate = new Item("platinum-plate", platinum.color);
        alloyPlate = new Item("alloy-plate", carbonAlloy.color);
        siliconSheet = new Item("silicon-sheet", Items.silicon.color);
        plastaniumSheet = new Item("plastanium-sheet", Items.plastanium.color);

        aluminumRod = new Item("aluminum-rod", aluminum.color);
        nickelRod = new Item("nickel-rod", nickel.color);
        silverRod = new Item("silver-rod", silver.color);
        platinumRod = new Item("platinum-rod", platinum.color);
        alloyRod = new Item("alloy-rod", carbonAlloy.color);

        aluminumWire = new Item("aluminum-wire", aluminum.color);
        nickelWire = new Item("nickel-wire", nickel.color);
        silverWire = new Item("silver-wire", silver.color);
        platinumWire = new Item("platinum-wire", platinum.color);

        controlCircuit = new Item("control-circuit");
        calculationCircuit = new Item("calculation-circuit");
        processingUnit = new Item("processing-unit");

        aluminumCogwheel = new Item("aluminum-cogwheel");
        silverCogwheel = new Item("silver-cogwheel");
        alloyCogwheel = new Item("alloy-cogwheel");

        powerCell = new Item("power-cell");
        liquidCell = new Item("liquid-cell");
        electronicMotor = new Item("electronic-motor");

        lemon = new Item("lemon");
    }
}
