package carpediem.world.blocks.power;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.world.blocks.power.*;

// i will never be allowed to write code in a professional environment
public class CableNode extends PowerNode {
    public TextureRegion cable1, cable2;

    public CableNode(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();

        cable1 = Core.atlas.find("carpe-diem-cable1");
        cable2 = Core.atlas.find("carpe-diem-cable2");
        laserEnd = Core.atlas.find("carpe-diem-cable-end");
    }

    public void drawCableSegment(float x, float y, float rotation, float scale) {
        float a = Draw.getColor().a;

        Draw.rect(cable1, x, y, cable1.width * scale * cable1.scl(), cable1.height * scale * cable1.scl(), rotation);
        Draw.alpha(a * Mathf.clamp(Mathf.slope(rotation / 270f) * 1.5f, 0f, 1f));
        Draw.rect(cable2, x, y, cable2.width * scale * cable2.scl(), cable2.height * scale * cable2.scl(), rotation);
        Draw.alpha(a);
    }

    public void drawCable(float x1, float y1, float x2, float y2, float scale) {
        float rot = Mathf.angle(x2 - x1, y2 - y1);
        float len = Mathf.dst(x1, y1, x2, y2);
        float div = cable1.width * scale * cable1.scl();

        Tmp.v1.setZero();

        for (int i = 0; i <= len / div; i++) {
            drawCableSegment(x1 + Tmp.v1.x, y1 + Tmp.v1.y, rot, scale);
            Tmp.v1.add(Tmp.v2.trns(rot, div));
        }

        Draw.rect(laserEnd, x1, y1, laserEnd.width * scale * laserEnd.scl(), laserEnd.height * scale * laserEnd.scl());
        Draw.rect(laserEnd, x2, y2, laserEnd.width * scale * laserEnd.scl(), laserEnd.height * scale * laserEnd.scl());

        Drawf.light(x1, y1, x2, y2);
    }

    @Override
    public void drawLaser(float x1, float y1, float x2, float y2, int size1, int size2) {
        float angle1 = Angles.angle(x1, y1, x2, y2),
                vx = Mathf.cosDeg(angle1), vy = Mathf.sinDeg(angle1),
                len1 = size1 * Vars.tilesize / 2f, len2 = size2 * Vars.tilesize / 2f;

        drawCable(x1 + vx * len1, y1 + vy * len1, x2 - vx * len2, y2 - vy * len2, laserScale);
    }
}
