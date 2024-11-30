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
            moveTime = 12f;
        }};

        beltMerger = new Merger("belt-merger") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 4, CDItems.aluminumPlate, 4, CDItems.aluminumCogwheel, 4));
            speed = 12f;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSideRegion()
            );
        }};

        beltSplitter = new Splitter("belt-splitter") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 4, CDItems.aluminumPlate, 4, CDItems.aluminumCogwheel, 4));
            speed = 12f;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSortRegion(),
                    new DrawSideRegion()
            );
        }};

        // these SHOULD cost circuits... but then the splitters would also cost circuits since they sort items and Im not making the player do that
        beltOverflowGate = new DrawerOverflowDuct("belt-overflow-gate") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 6, CDItems.aluminumPlate, 4, CDItems.aluminumCogwheel, 4));
            speed = 12f;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSideRegion()
            );
        }};

        beltUnderflowGate = new DrawerOverflowDuct("belt-underflow-gate") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 6, CDItems.aluminumPlate, 4, CDItems.aluminumCogwheel, 4));
            speed = 12f;
            invert = true;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSideRegion()
            );
        }};

        beltBridge = new BeltBridge("belt-bridge") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 8, CDItems.aluminumPlate, 1, CDItems.aluminumCogwheel, 1));
            range = 6;
            speed = 12f;

            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSideRegion()
            );
        }};
    }
}
