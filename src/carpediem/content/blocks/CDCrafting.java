package carpediem.content.blocks;

import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import carpediem.content.*;
import carpediem.world.blocks.crafting.*;
import carpediem.world.consumers.*;
import carpediem.world.draw.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class CDCrafting {
    public static Block
    // T0
    smelterT0,
    // T1
    smelterT1, pressT1, rollingMillT1, assemblerT1, refineryT1;

    public static void load() {
        // a special one .
        smelterT0 = new RecipeCrafter("smelter-t0") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.rawAluminum, 20
            ));
            alwaysUnlocked = true;
            size = 3;

            craftingSpeed = 0.5f;
            recipes.addAll(CDRecipes.basicSmelterRecipes);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawDefault()
            );

            consume(new ConsumeItemsUses(7, ItemStack.with(CDItems.sulfur, 1)));
        }};

        // region T1
        smelterT1 = new RecipeCrafter("smelter-t1") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.aluminum, 20,
                    CDItems.aluminumRod, 20,
                    CDItems.nickelWire, 5,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 10
            ));
            size = 4;

            recipes.addAll(CDRecipes.basicSmelterRecipes).addAll(CDRecipes.advancedSmelterRecipes);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawDefault()
            );

            consumePower(1f / 12f);
        }};

        pressT1 = new RecipeCrafter("press-t1") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.aluminum, 25,
                    CDItems.aluminumPlate, 10,
                    CDItems.aluminumCogwheel, 5,
                    CDItems.nickelWire, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 5
            ));
            size = 4;

            recipes.addAll(CDRecipes.pressRecipes);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawPress(),
                    new DrawDefault(),
                    new DrawInvert()
            );

            consumePower(1f / 12f);
        }};

        rollingMillT1 = new RecipeCrafter("rolling-mill-t1") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.aluminum, 25,
                    CDItems.aluminumRod, 20,
                    CDItems.aluminumCogwheel, 10,
                    CDItems.nickelWire, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 5
            ));
            size = 4;

            recipes.addAll(CDRecipes.rollingMillRecipes);
            // rolling mill has configurable true too because of the rods and wires recipe thingy
            configurable = true;

            // yea i copied this from the drills
            Seq<DrawBlock> rotators = new Seq<>();

            float offset = 4f;
            Vec2[] points = {
                    new Vec2(offset, offset),
                    new Vec2(offset, -offset),
                    new Vec2(-offset, -offset),
                    new Vec2(-offset, offset)
            };

            boolean sign = true;
            for (Vec2 point : points) {
                rotators.add(new DrawRegion("-rotator", 7f * Mathf.sign(sign),  true) {{
                    x = point.x;
                    y = point.y;
                }});

                sign = !sign;
            }

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawCustomIconMulti("-rotator-icon", rotators),
                    new DrawDefault()
            );

            consumePower(1f / 12f);
        }};

        assemblerT1 = new RecipeCrafter("assembler-t1") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.aluminum, 20,
                    CDItems.aluminumPlate, 5,
                    CDItems.aluminumRod, 15,
                    CDItems.aluminumCogwheel, 20,
                    CDItems.nickelWire, 10,
                    CDItems.controlCircuit, 5,
                    CDItems.powerCell, 5
            ));
            size = 4;

            recipes.addAll(CDRecipes.assemblerRecipes);
            // FUCKKKKKKKKK
            configurable = true;

            // and i did it again!
            Seq<DrawBlock> rotators = new Seq<>();

            float offset = 11f;
            Vec2[] points = {
                    new Vec2(offset, 0f),
                    new Vec2(0f, offset),
                    new Vec2(-offset, 0f),
                    new Vec2(0f, -offset)
            };

            for (Vec2 point : points) {
                rotators.add(new DrawRegion("-rotator", 5f,  true) {{
                    x = point.x;
                    y = point.y;
                }});
            }

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawCustomIconMulti("-rotator-icon", rotators),
                    new DrawPistons() {{
                        angleOffset = 45f;
                        sinMag = -4f;
                        sinScl = 4f;
                        sideOffset = Mathf.PI / 2f;
                    }},
                    new DrawDefault()
            );

            consumePower(1f / 10f);
        }};

        refineryT1 = new RecipeCrafter("refinery-t1") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 4;

            recipes.addAll(CDRecipes.refineryRecipes);
            configurable = true;

            consumePower(1f / 10f);
        }};
        // endregion
    }
}
