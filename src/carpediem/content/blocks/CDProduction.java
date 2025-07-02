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
            requirements(Category.production, ItemStack.with(
                    CDItems.aluminum, 10,
                    CDItems.aluminumRod, 5,
                    CDItems.aluminumCogwheel, 5
            ));
            size = 3;
            itemCapacity = 50;

            tier = 3;
            drillTime = 32f * 60f; // A WHOLE THIRTY TWO SECONDS
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

            squareSprite = false;
            customShadow = true;

            consume(new ConsumeItemsUses(7, ItemStack.with(CDItems.sulfur, 1)));
            liquidBoostIntensity = 1f;
        }};

        drillT1 = new DrawerDrill("drill-t1") {{
            requirements(Category.production, ItemStack.with(
                    CDItems.aluminum, 20,
                    CDItems.aluminumRod, 15,
                    CDItems.aluminumCogwheel, 20,
                    CDItems.nickelWire, 5,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 15
            ));
            size = 4;

            tier = 4;
            drillTime = 24f * 60f;
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

            squareSprite = false;
            customShadow = true;

            consumePower(1f / 10f);
            liquidBoostIntensity = 1f;
        }};

        drillT2 = new DrawerDrill("drill-t2") {{
            requirements(Category.production, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 5;

            tier = 5;
            drillTime = 24f * 60f;
            hardnessDrillMultiplier = 0f;

            drawer = new DrawMulti(
                    new DrawDrillT2(),
                    new DrawDefault()
            );

            squareSprite = false;
            customShadow = true;

            consumePower(5f / 10f);
            liquidBoostIntensity = 1f;
        }};
    }
}
