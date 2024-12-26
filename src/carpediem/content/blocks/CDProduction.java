package carpediem.content.blocks;

import arc.math.geom.*;
import arc.struct.*;
import carpediem.content.*;
import carpediem.world.blocks.production.*;
import carpediem.world.consumers.*;
import carpediem.world.draw.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDProduction {
    public static Block drillT0, drillT1, drillT2;

    public static void load() {
        drillT0 = new ItemConsumerDrill("drill-t0") {{
            requirements(Category.production, ItemStack.with());
            size = 3;

            tier = 3;
            drillTime = 8f * 60f;
            hardnessDrillMultiplier = 0f;
            drillMultipliers.put(CDItems.sulfur, 9f / 8f);

            Seq<DrawBlock> rotators = new Seq<>();

            float offset = 26f / 4f;
            Vec2[] points = {
                    new Vec2(offset, 0f),
                    new Vec2(0f, offset),
                    new Vec2(-offset, 0f),
                    new Vec2(0f, -offset)
            };

            for (Vec2 point : points) {
                rotators.add(new DrawRegion("-rotator", 3f,  true) {{
                    x = point.x;
                    y = point.y;
                    layer = Layer.block - 1.5f;
                }});
            }

            drawer = new DrawMulti(
                    new DrawCustomIconMulti("-rotator-icon", rotators),
                    new DrawRegion("-bottom"),
                    new DrawDefault()
            );

            customShadow = true;

            consume(new ConsumeItemsUses(7, ItemStack.with(CDItems.sulfur, 1)));
            liquidBoostIntensity = 1f;
        }};

        drillT1 = new DrawerDrill("drill-t1") {{
            requirements(Category.production, ItemStack.with());
            size = 4;

            tier = 4;
            drillTime = 6f * 60f;
            hardnessDrillMultiplier = 0f;
            drillMultipliers.put(CDItems.rawSilver, 0.5f);

            Seq<DrawBlock> rotators = new Seq<>();

            // it just gets worse and worse
            Vec2 base = new Vec2(15f / 4f, 39f / 4f);
            Vec2[] points = {
                    new Vec2(base.x, base.y),
                    new Vec2(-base.x, base.y),
                    new Vec2(base.x, -base.y),
                    new Vec2(-base.x, -base.y),
                    new Vec2(base.y, base.x),
                    new Vec2(-base.y, base.x),
                    new Vec2(base.y, -base.x),
                    new Vec2(-base.y, -base.x)
            };

            for (Vec2 point : points) {
                rotators.add(new DrawRegion("-rotator", 3f,  true) {{
                    x = point.x;
                    y = point.y;
                    layer = Layer.block - 1.5f;
                }});
            }

            drawer = new DrawMulti(
                    new DrawCustomIconMulti("-rotator-icon", rotators),
                    new DrawDefault()
            );

            customShadow = true;

            consumePower(1f / 10f);
            liquidBoostIntensity = 1f;
        }};
    }
}
