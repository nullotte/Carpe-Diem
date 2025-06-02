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
    powerCell, fluidCell,

    card1, card2, card3, card4,

    lemon
    ;
    // TODO stats?

    public static void load() {
        // region raw
        rawAluminum = new Item("raw-aluminum", Color.valueOf("948fbf")) {{
            hardness = 1;
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

        // im so stupid why didnt i just name these circuit1 circuit2 circuit3
        controlCircuit = new Item("control-circuit", Color.valueOf("ab9ded"));
        calculationCircuit = new Item("calculation-circuit", Color.valueOf("5bb4ee"));
        processingUnit = new Item("processing-unit", Color.valueOf("df8dc7"));

        aluminumCogwheel = new Item("aluminum-cogwheel", aluminum.color);
        silverCogwheel = new Item("silver-cogwheel", silver.color);
        alloyCogwheel = new Item("alloy-cogwheel", sturdyAlloy.color);

        powerCell = new Item("power-cell", Color.valueOf("dfb074"));
        fluidCell = new Item("fluid-cell", Color.valueOf("88a4ff"));
        // endregion

        // region cards
        card1 = new Item("card1", Color.valueOf("87b0f5"));
        card2 = new Item("card2", Color.valueOf("8bdc9c"));
        card3 = new Item("card3", Color.valueOf("fcff80"));
        card4 = new Item("card4", Color.valueOf("ff917c"));
        // endregion

        // ahaha
        lemon = new Item("lemon", Color.valueOf("f4da7f"));
    }
}
