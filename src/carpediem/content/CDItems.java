package carpediem.content;

import arc.struct.*;
import mindustry.type.*;

public class CDItems {
    public static Item
    rawAluminum, rawNickel, rawSilver, rawPlatinum, bitumen, sulfur,
    aluminum, nickel, silver, platinum,
    unnamedAlloy, heliorite,

    aluminumPlate, nickelPlate, silverPlate, platinumPlate,
    aluminumRod, nickelRod, silverRod, platinumRod,
    aluminumWire, nickelWire, silverWire, platinumWire,

    lemon
    ;
    // + silicon. pyratite...?
    // also do remember you can always change these later.

    public static Seq<Item> pureItems;
    public static ObjectMap<Item, Item> rawItems, plates, rods, wires;

    public static void load() {
        aluminum = new Item("aluminum") {{

        }};

        nickel = new Item("nickel") {{

        }};

        silver = new Item("silver") {{

        }};

        platinum = new Item("platinum") {{

        }};

        bitumen = new Item("bitumen") {{

        }};

        sulfur = new Item("sulfur") {{

        }};

        // I MAKE REALLY BAD DECISIONS OK
        rawAluminum = new Item("raw-aluminum");
        rawNickel = new Item("raw-nickel");
        rawSilver = new Item("raw-silver");
        rawPlatinum = new Item("raw-platinum");

        aluminumPlate = new Item("aluminum-plate");
        nickelPlate = new Item("nickel-plate");
        silverPlate = new Item("silver-plate");
        platinumPlate = new Item("platinum-plate");

        aluminumRod = new Item("aluminum-rod");
        nickelRod = new Item("nickel-rod");
        silverRod = new Item("silver-rod");
        platinumRod = new Item("platinum-rod");

        aluminumWire = new Item("aluminum-wire");
        nickelWire = new Item("nickel-wire");
        silverWire = new Item("silver-wire");
        platinumWire = new Item("platinum-wire");

        pureItems = Seq.with(aluminum, nickel, silver, platinum);

        rawItems = ObjectMap.of(
                aluminum, rawAluminum,
                nickel, rawNickel,
                silver, rawSilver,
                platinum, rawPlatinum
        );

        plates = ObjectMap.of(
                aluminum, aluminumPlate,
                nickel, nickelPlate,
                silver, silverPlate,
                platinum, platinumPlate
        );

        rods = ObjectMap.of(
                aluminum, aluminumRod,
                nickel, nickelRod,
                silver, silverRod,
                platinum, platinumRod
        );

        wires = ObjectMap.of(
                aluminum, aluminumWire,
                nickel, nickelWire,
                silver, silverWire,
                platinum, platinumWire
        );

        lemon = new Item("lemon");
    }
}
