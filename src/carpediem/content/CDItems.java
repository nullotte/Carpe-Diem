package carpediem.content;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.game.EventType.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;

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

    lemon, funnyCube
    ;

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
            flammability = 0.75f;
        }};

        tar = new Item("tar", Color.valueOf("32312c")) {{
            flammability = 0.1f; // is tar even flammable actually? i have no idea
        }};
        // endregion
        // region refined
        aluminum = new Item("aluminum", rawAluminum.color);
        nickel = new Item("nickel", rawNickel.color);
        silver = new Item("silver", rawSilver.color);
        platinum = new Item("platinum", rawPlatinum.color);
        sturdyAlloy = new Item("sturdy-alloy", Color.valueOf("6b758b"));
        // endregion
        // region intermediate products
        aluminumPlate = new Item("aluminum-plate", aluminum.color);
        nickelPlate = new Item("nickel-plate", nickel.color);
        silverPlate = new Item("silver-plate", silver.color);
        platinumPlate = new Item("platinum-plate", platinum.color);
        alloyPlate = new Item("alloy-plate", sturdyAlloy.color);
        siliconSheet = new Item("silicon-sheet", Items.silicon.color);
        plastaniumSheet = new Item("plastanium-sheet", Items.plastanium.color); // yes i know TECHNICALLY plastanium is slightly flammable and so should this be but I dont care okay none of the stats are relevant in actual gameplay and only exist to be funny looking numbers in the core database

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

        funnyCube = new Item("funny-cube", new Color().a(1f)) {{
            frames = 31;
            frameTime = 60f / 24f;
        }};

        BasicBulletType funnyCubeBullet = new BasicBulletType(4f, 123456f) {{
            width = 7f;
            height = 9f;
            ammoMultiplier = 3f;
            lifetime = 60f;

            trailLength = 7;
            trailWidth = 1.5f;

            hitEffect = despawnEffect = Fx.hitBulletColor;
            hitColor = backColor = trailColor = new Color().a(1f);
        }};
        ((ItemTurret) Blocks.duo).ammoTypes.put(funnyCube, funnyCubeBullet);

        Events.run(Trigger.update, () -> {
            funnyCube.color.set(
                    Mathf.absin(Time.globalTime / 5f, 0.5f, 1f),
                    Mathf.absin(Time.globalTime / 5f + Mathf.PI2 * (1f / 3f), 0.5f, 1f),
                    Mathf.absin(Time.globalTime / 5f + Mathf.PI2 * (2f / 3f), 0.5f, 1f)
            );

            // theyre all assigned to the same color so yk
            funnyCubeBullet.backColor.set(funnyCube.color);
        });
    }
}
