package carpediem.ui;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.struct.*;
import arc.struct.IntMap.*;
import mindustry.ctype.*;
import mindustry.graphics.*;

public class CraftingGridImage extends Element {
    public IntMap<UnlockableContent> items = new IntMap<>();
    public float gridSlotSize = 60f, gridImageSize = 40f;

    public float itemsWidth() {
        int max = 0;
        for (Entry<UnlockableContent> entry : items.entries()) {
            Point2 pos = Point2.unpack(entry.key);
            max = Math.max(max, pos.x + 1);
        }
        return max * gridSlotSize;
    }

    public float itemsHeight() {
        int max = 0;
        for (Entry<UnlockableContent> entry : items.entries()) {
            Point2 pos = Point2.unpack(entry.key);
            max = Math.max(max, pos.y + 1);
        }
        return max * gridSlotSize;
    }

    @Override
    public void draw() {
        int linesX = (int) (itemsWidth() / gridSlotSize) + 1, linesY = (int) (itemsHeight() / gridSlotSize) + 1;

        Draw.color(Pal.gray, parentAlpha);
        Lines.stroke(6f);
        for (int lx = 0; lx < linesX; lx++) {
            Lines.line(x + lx * gridSlotSize, y, x + lx * gridSlotSize, y + height, true);
        }
        for (int ly = 0; ly < linesY; ly++) {
            Lines.line(x, y + ly * gridSlotSize, x + width, y + ly * gridSlotSize, true);
        }

        Draw.color(Color.white, parentAlpha);
        for (Entry<UnlockableContent> entry : items.entries()) {
            Point2 pos = Point2.unpack(entry.key);
            Draw.rect(
                    entry.value.uiIcon,
                    x + (pos.x * gridSlotSize) + (gridSlotSize / 2f),
                    y + (pos.y * gridSlotSize) + (gridSlotSize / 2f),
                    gridImageSize,
                    gridImageSize
            );
        }
    }
}
