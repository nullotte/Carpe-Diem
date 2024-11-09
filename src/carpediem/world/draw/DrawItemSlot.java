package carpediem.world.draw;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawItemSlot extends DrawBlock {
    public Vec2[] slots;
    // should be per-slot but i cant be bothered ok?? thats for future me to deal with , if she'll even have to
    public boolean doSymmetry;

    public DrawItemSlot(boolean doSymmetry, Vec2... slots) {
        this.doSymmetry = doSymmetry;
        this.slots = slots;
    }

    @Override
    public void load(Block block) {
        if (doSymmetry) {
            Seq<Vec2> newSlots = new Seq<>();

            for (Vec2 slot : slots) {
                int drawn = slot.x == slot.y ? 4 : 8;
                for (int i = 0; i < drawn; i++) {
                    switch (i) {
                        case 0 -> newSlots.add(new Vec2(slot.x, slot.y));
                        case 1 -> newSlots.add(new Vec2(-slot.x, slot.y));
                        case 2 -> newSlots.add(new Vec2(slot.x, -slot.y));
                        case 3 -> newSlots.add(new Vec2(-slot.x, -slot.y));
                        case 4 -> newSlots.add(new Vec2(slot.y, slot.x));
                        case 5 -> newSlots.add(new Vec2(-slot.y, slot.x));
                        case 6 -> newSlots.add(new Vec2(slot.y, -slot.x));
                        case 7 -> newSlots.add(new Vec2(-slot.y, -slot.x));
                    }
                }
            }

            slots = newSlots.toArray(Vec2.class);
            doSymmetry = false;
        }
    }

    @Override
    public void draw(Building build) {
        if (build.block.hasItems) {
            rand.setSeed(build.id);
            int[] i = {0};

            build.items.each((item, count) -> {
                if (i[0] < slots.length) {
                    Vec2 slot = slots[i[0]++];
                    // TODO the items should wobble around a bit
                    Draw.rect(
                            item.fullIcon,
                            build.x + slot.x,
                            build.y + slot.y,
                            Vars.itemSize, Vars.itemSize
                    );
                }
            });
        }
    }
}
