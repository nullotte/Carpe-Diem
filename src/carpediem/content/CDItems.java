package carpediem.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;

public class CDItems {
    public static Item
    rawAluminum, rawNickel, rawSilver, rawPlatinum,
    sulfur, tar, // sand

    aluminum, nickel, silver, platinum, sturdyAlloy, // pyratite, silicon, plastanium

    aluminumPlate, nickelPlate, silverPlate, platinumPlate, alloyPlate, siliconSheet, plastaniumSheet,
    aluminumRod, nickelRod, silverRod, platinumRod, alloyRod,
    aluminumWire, nickelWire,

    controlCircuit, calculationCircuit, processingUnit,
    aluminumCogwheel, silverCogwheel, alloyCogwheel,
    powerCell, liquidCell,

    lemon
    ;
    // TODO stats?

    public static void load() {
        // region raw
        rawAluminum = new Item("raw-aluminum", Color.valueOf("948fbf")) {{
            hardness = 1;
            alwaysUnlocked = true;
        }};

        rawNickel = new Item("raw-nickel", Color.valueOf("cbb07e")) {{
            hardness = 1;
        }};

        rawSilver = new Item("raw-silver", Color.valueOf("8dabd4")) {{
            hardness = 4;
        }};

        rawPlatinum = new Item("raw-platinum", Color.valueOf("d985a3")) {{
            hardness = 5;
        }};

        sulfur = new Item("sulfur", Color.valueOf("f7eb94")) {{
            hardness = 3;
            alwaysUnlocked = true;
        }};

        tar = new Item("tar", Color.valueOf("32312c")) {{

        }};
        // endregion
        // region refined
        aluminum = new Item("aluminum", rawAluminum.color) {{

        }};

        nickel = new Item("nickel", rawNickel.color) {{

        }};

        silver = new Item("silver", rawSilver.color) {{

        }};

        platinum = new Item("platinum", rawPlatinum.color) {{

        }};

        sturdyAlloy = new Item("sturdy-alloy", Color.valueOf("6b758b")) {{

        }};
        // endregion
        // region i dont even know
        aluminumPlate = new Item("aluminum-plate", aluminum.color);
        nickelPlate = new Item("nickel-plate", nickel.color);
        silverPlate = new Item("silver-plate", silver.color);
        platinumPlate = new Item("platinum-plate", platinum.color);
        alloyPlate = new Item("alloy-plate", sturdyAlloy.color);
        siliconSheet = new Item("silicon-sheet", Items.silicon.color);
        plastaniumSheet = new Item("plastanium-sheet", Items.plastanium.color);

        aluminumRod = new Item("aluminum-rod", aluminum.color);
        nickelRod = new Item("nickel-rod", nickel.color);
        silverRod = new Item("silver-rod", silver.color);
        platinumRod = new Item("platinum-rod", platinum.color);
        alloyRod = new Item("alloy-rod", sturdyAlloy.color);

        aluminumWire = new Item("aluminum-wire", aluminum.color);
        nickelWire = new Item("nickel-wire", nickel.color);

        controlCircuit = new Item("control-circuit");
        calculationCircuit = new Item("calculation-circuit");
        processingUnit = new Item("processing-unit");

        aluminumCogwheel = new Item("aluminum-cogwheel", aluminum.color);
        silverCogwheel = new Item("silver-cogwheel", silver.color);
        alloyCogwheel = new Item("alloy-cogwheel", sturdyAlloy.color);

        powerCell = new Item("power-cell");
        liquidCell = new Item("liquid-cell");
        // endregion

        // malicious.
        lemon = new Item("lemon", Color.valueOf("f4da7f"));
    }
}
