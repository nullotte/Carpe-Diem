package carpediem.content.blocks;

import carpediem.world.blocks.distribution.*;
import carpediem.world.draw.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDDistribution {
    public static Block belt, beltMerger, beltSplitter;

    public static void load() {
        beltMerger = new Merger("belt-merger") {{
            requirements(Category.distribution, ItemStack.with());

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSideRegion()
            );
        }};

        beltSplitter = new Splitter("belt-splitter") {{
            requirements(Category.distribution, ItemStack.with());

            squareSprite = false;
            rotateDraw = false;
            drawer = new DrawMulti(
                    new DrawRegion(),
                    new DrawSortRegion(),
                    new DrawRotatedRegion()
            );
        }};
    }
}
