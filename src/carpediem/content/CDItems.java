package carpediem.content;

import mindustry.type.*;

public class CDItems {
    public static Item
    rawAluminum, rawNickel, rawSilver, rawPlatinum, bitumen, sulfur,
    waterIce, biomass, charcoal,

    aluminum, nickel, silver, platinum, carbonAlloy,

    aluminumPlate, nickelPlate, silverPlate, platinumPlate, alloyPlate, siliconSheet, plastaniumSheet,
    aluminumRod, nickelRod, silverRod, platinumRod, alloyRod,
    aluminumWire, nickelWire, silverWire, platinumWire,

    controlCircuit, calculationCircuit, processingUnit,
    aluminumCogwheel, silverCogwheel, alloyCogwheel,
    powerCell, liquidCell, electronicMotor,

    lemon
    ;
    // + silicon, pyratite, plastanium
    // TODO item colors
    // also i just realized i could probably do a JEI and put all the uses and production methods in the stats
    // more work for future me i guess !

    public static void load() {
        // "wow thats a stupid implementation" just you waitt it's going to get a hundred times worse
        rawAluminum = new Item("raw-aluminum");
        rawNickel = new Item("raw-nickel");
        rawSilver = new Item("raw-silver");
        rawPlatinum = new Item("raw-platinum");

        bitumen = new Item("bitumen") {{

        }};

        sulfur = new Item("sulfur") {{

        }};

        waterIce = new Item("water-ice") {{

        }};

        biomass = new Item("biomass") {{

        }};

        charcoal = new Item("charcoal") {{

        }};

        aluminum = new Item("aluminum") {{

        }};

        nickel = new Item("nickel") {{

        }};

        silver = new Item("silver") {{

        }};

        platinum = new Item("platinum") {{

        }};

        carbonAlloy = new Item("carbon-alloy") {{

        }};

        aluminumPlate = new Item("aluminum-plate");
        nickelPlate = new Item("nickel-plate");
        silverPlate = new Item("silver-plate");
        platinumPlate = new Item("platinum-plate");
        alloyPlate = new Item("alloy-plate");
        siliconSheet = new Item("silicon-sheet");
        plastaniumSheet = new Item("plastanium-sheet");

        aluminumRod = new Item("aluminum-rod");
        nickelRod = new Item("nickel-rod");
        silverRod = new Item("silver-rod");
        platinumRod = new Item("platinum-rod");
        alloyRod = new Item("alloy-rod");

        aluminumWire = new Item("aluminum-wire");
        nickelWire = new Item("nickel-wire");
        silverWire = new Item("silver-wire");
        platinumWire = new Item("platinum-wire");

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
