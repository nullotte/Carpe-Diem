package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.distribution.*;
import carpediem.world.draw.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDDistribution {
    public static Block belt, beltMerger, beltSplitter, beltBridge;

    public static void load() {
        belt = new Belt("belt") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminumPlate, 1, CDItems.aluminumCogwheel, 1));
            itemCapacity = 4;
            speed = 2f / 60f;
            displayedSpeed = 5f;
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

        beltBridge = new DrawerBridge("belt-bridge") {{
            requirements(Category.distribution, ItemStack.with(CDItems.aluminum, 8, CDItems.aluminumPlate, 1, CDItems.aluminumCogwheel, 1));

            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSideRegion()
            );

            ((Belt) belt).bridgeReplacement = this;
        }};
    }
}
