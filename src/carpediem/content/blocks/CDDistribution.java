package carpediem.content.blocks;

import carpediem.world.blocks.distribution.*;
import carpediem.world.draw.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDDistribution {
    public static Block belt, beltMerger, beltSplitter, beltBridge;

    public static void load() {
        belt = new Belt("belt") {{
            requirements(Category.distribution, ItemStack.with());
            itemCapacity = 4;
            speed = 2f / 60f;
            displayedSpeed = 5f;
        }};

        beltMerger = new Merger("belt-merger") {{
            requirements(Category.distribution, ItemStack.with());
            speed = 12f;

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSideRegion()
            );
        }};

        beltSplitter = new Splitter("belt-splitter") {{
            requirements(Category.distribution, ItemStack.with());
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
            requirements(Category.distribution, ItemStack.with());

            squareSprite = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSideRegion()
            );

            ((Belt) belt).bridgeReplacement = this;
        }};
    }
}
