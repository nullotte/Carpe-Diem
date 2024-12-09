package carpediem.world.draw;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.draw.*;
import mindustry.world.modules.*;

public class DrawItemSlot extends DrawBlock {
    public static final int stack = 1000;

    public static int currentItem;
    public static int currentStack;
    public static boolean continueDrawing;
    public static ItemModule currentDrawn;

    public Vec2 slot;

    public DrawItemSlot(Vec2 slot) {
        this.slot = slot;
    }

    public static DrawMulti mirrored(Vec2... slots) {
        Seq<DrawBlock> drawers = new Seq<>();

        for (Vec2 slot : slots) {
            drawers.add(new DrawItemSlot(new Vec2(slot.x, slot.y)));
            drawers.add(new DrawItemSlot(new Vec2(slot.x, -slot.y)));
            drawers.add(new DrawItemSlot(new Vec2(-slot.x, slot.y)));
            drawers.add(new DrawItemSlot(new Vec2(-slot.x, -slot.y)));
        }

        return new DrawMulti(drawers.as());
    }

    @Override
    public void draw(Building build) {
        if (build.block.hasItems) {
            if (currentDrawn != build.items) {
                currentDrawn = build.items;
                currentItem = 0;
                currentStack = 0;
                continueDrawing = true;
            }

            if (continueDrawing) {
                boolean success = false;

                for (int i = currentItem; i < Vars.content.items().size; i++) {
                    // check if the building has enough of the current item - if so, draw it
                    if (currentDrawn.has(Vars.content.item(i), 1 + currentStack * stack)) {
                        float size = Vars.itemSize * Mathf.lerp(Math.min((float) (build.items.get(i) - currentStack * stack) / stack, 1), 1f, 0.5f);

                        Draw.rect(
                                Vars.content.item(i).fullIcon,
                                build.x + slot.x,
                                build.y + slot.y,
                                size, size
                        );

                        currentItem = i;
                        currentStack++;
                        success = true;
                        break;
                    } else {
                        // not enough, set required amount back to 1
                        currentStack = 0;
                    }
                }

                if (!success) {
                    continueDrawing = false;
                }
            }
        }
    }
}
