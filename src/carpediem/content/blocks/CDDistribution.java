package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.distribution.*;
import carpediem.world.draw.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDDistribution {
    public static Block belt, beltMerger, beltSplitter, beltOverflowGate, beltUnderflowGate, beltBridge;

    public static void load() {
        belt = new Belt("belt") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminumPlate, 1, CDItems.aluminumCogwheel, 1));
            itemCapacity = 3;
            moveTime = 6f;
        }};

        beltMerger = new Merger("belt-merger") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 4, CDItems.aluminumPlate, 4, CDItems.aluminumCogwheel, 4));
            speed = 6f;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawBeltUnder(belt),
                    new DrawRegion(),
                    new DrawSideRegion()
            );
        }};

        beltSplitter = new Splitter("belt-splitter") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 4, CDItems.aluminumPlate, 4, CDItems.aluminumCogwheel, 4));
            speed = 6f;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawBeltUnder(belt),
                    new DrawRegion(),
                    new DrawSortRegion(),
                    new DrawSideRegion()
            );
        }};

        beltOverflowGate = new DrawerOverflowDuct("belt-overflow-gate") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 6, CDItems.aluminumPlate, 4, CDItems.aluminumCogwheel, 4));
            speed = 6f;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawBeltUnder(belt),
                    new DrawRegion(),
                    new DrawSideRegion()
            );
        }};

        beltUnderflowGate = new DrawerOverflowDuct("belt-underflow-gate") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 6, CDItems.aluminumPlate, 4, CDItems.aluminumCogwheel, 4));
            speed = 6f;
            invert = true;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawBeltUnder(belt),
                    new DrawRegion(),
                    new DrawSideRegion()
            );
        }};

        beltBridge = new BeltBridge("belt-bridge") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 8, CDItems.aluminumPlate, 1, CDItems.aluminumCogwheel, 1));
            range = 6;
            speed = 6f;

            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawBeltUnder(belt),
                    new DrawBridgeRegion()
            );
        }};

        // for merger auto replacement . probably shouldnt use isDuct but oh well it works
        belt.isDuct = beltMerger.isDuct = beltSplitter.isDuct = beltOverflowGate.isDuct = beltUnderflowGate.isDuct = beltBridge.isDuct = true;
    }
}
