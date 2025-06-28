package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.distribution.*;
import carpediem.world.draw.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDDistribution {
    public static Block belt, beltMerger, beltSplitter, beltOverflowGate, beltBridge, beltUnloader;

    public static void load() {
        belt = new Belt("belt") {{
            requirements(Category.distribution, ItemStack.with(
                    CDItems.aluminumPlate, 1,
                    CDItems.aluminumCogwheel, 1
            ));
            itemCapacity = 3;
            moveTime = 6f;
        }};

        beltMerger = new Merger("belt-merger") {{
            requirements(Category.distribution, ItemStack.with(
                    CDItems.aluminum, 2,
                    CDItems.aluminumPlate, 2,
                    CDItems.aluminumCogwheel, 2
            ));
            speed = 6f;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawBeltUnder(belt),
                    new DrawRegion(),
                    new DrawRegion("-top") {{
                        buildingRotate = true;
                    }}
            );
        }};

        beltSplitter = new Splitter("belt-splitter") {{
            requirements(Category.distribution, ItemStack.with(
                    CDItems.aluminum, 2,
                    CDItems.aluminumPlate, 2,
                    CDItems.aluminumCogwheel, 2
            ));
            speed = 6f;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawBeltUnder(belt),
                    new DrawRegion(),
                    new DrawSortRegion(),
                    new DrawRegion("-top") {{
                        buildingRotate = true;
                    }}
            );
        }};

        beltOverflowGate = new DrawerOverflowDuct("belt-overflow-gate") {{
            requirements(Category.distribution, ItemStack.with(
                    CDItems.aluminum, 2,
                    CDItems.aluminumPlate, 2,
                    CDItems.aluminumCogwheel, 2
            ));
            speed = 6f;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawBeltUnder(belt),
                    new DrawRegion(),
                    new DrawRegion("-top") {{
                        buildingRotate = true;
                    }}
            );
        }};

        beltBridge = new BeltBridge("belt-bridge") {{
            requirements(Category.distribution, ItemStack.with(
                    CDItems.aluminum, 8,
                    CDItems.aluminumPlate, 1,
                    CDItems.aluminumCogwheel, 1
            ));
            range = 6;
            speed = 6f;

            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawBeltUnder(belt),
                    new DrawBridgeRegion()
            );
        }};

        beltUnloader = new DrawerDirectionalUnloader("belt-unloader") {{
            requirements(Category.distribution, ItemStack.with(
                    CDItems.aluminum, 10,
                    CDItems.aluminumPlate, 6,
                    CDItems.aluminumCogwheel, 2,
                    CDItems.controlCircuit, 1
            ));
            allowCoreUnload = true;

            speed = 5f;
            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSortRegion(),
                    new DrawRegion("-top") {{
                        buildingRotate = true;
                    }}
            );
        }};

        // for merger auto replacement . probably shouldnt use isDuct but oh well it works
        belt.isDuct = beltMerger.isDuct = beltSplitter.isDuct = beltOverflowGate.isDuct = beltBridge.isDuct = beltUnloader.isDuct = true;
    }
}
