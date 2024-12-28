package carpediem.world.blocks.power;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;

// i will never be allowed to write code in a professional environment
public class CableNode extends PowerNode implements CableBlock {
    public static float realSatisfaction;

    public TextureRegion cable1, cable2, cableGlow, cableEndGlow, top, glow;
    public float topOffset;
    public float warmupSpeed = 0.05f;

    public CableNode(String name) {
        super(name);
        update = true;

        laserColor2 = Pal.turretHeat;
    }

    @Override
    public void load() {
        super.load();

        cable1 = Core.atlas.find("carpe-diem-cable1");
        cable2 = Core.atlas.find("carpe-diem-cable2");
        cableGlow = Core.atlas.find("carpe-diem-cable-glow");
        cableEndGlow = Core.atlas.find("carpe-diem-cable-end-glow");
        top = Core.atlas.find(name + "-top");
        glow = Core.atlas.find(name + "-glow");
        laserEnd = Core.atlas.find("carpe-diem-cable-end");
    }

    public void drawCableSegment(float x, float y, float rotation, float scale) {
        float a = Draw.getColor().a;

        Draw.rect(cable1, x, y, cable1.width * scale * cable1.scl(), cable1.height * scale * cable1.scl(), rotation);
        Draw.alpha(Mathf.equal(rotation, 45f, 0.1f) || Mathf.equal(rotation, 225f, 0.1f) ? a * 0.5f : (rotation > 45f && rotation < 225f ? a : 0f)); // good lord
        Draw.rect(cable2, x, y, cable2.width * scale * cable2.scl(), cable2.height * scale * cable2.scl(), rotation);
        Draw.alpha(a);
    }

    public void drawCable(float x1, float y1, float x2, float y2, boolean end1, boolean end2, float scale) {
        float rot = Mathf.angle(x2 - x1, y2 - y1);
        float len = Mathf.dst(x1, y1, x2, y2);
        float div = cable1.width * scale * cable1.scl();

        Tmp.v1.setZero();

        for (int i = 0; i <= len / div; i++) {
            drawCableSegment(x1 + Tmp.v1.x, y1 + Tmp.v1.y, rot, scale);
            Tmp.v1.add(Tmp.v2.trns(rot, div));
        }

        if (end1) Draw.rect(laserEnd, x1, y1, laserEnd.width * scale * laserEnd.scl(), laserEnd.height * scale * laserEnd.scl());
        if (end2) Draw.rect(laserEnd, x2, y2, laserEnd.width * scale * laserEnd.scl(), laserEnd.height * scale * laserEnd.scl());

        float z = Draw.z();
        Lines.stroke(12f * scale);
        Draw.z(Layer.power + 0.02f);
        Draw.color(laserColor2, realSatisfaction);
        Draw.blend(Blending.additive);
        Lines.line(cableGlow, x1, y1, x2, y2, false);
        if (end1) Draw.rect(cableEndGlow, x1, y1, laserEnd.width * scale * laserEnd.scl(), laserEnd.height * scale * laserEnd.scl());
        if (end2) Draw.rect(cableEndGlow, x2, y2, laserEnd.width * scale * laserEnd.scl(), laserEnd.height * scale * laserEnd.scl());
        Draw.blend();
        Draw.color();
        Draw.z(z);
        Lines.stroke(1f);

        Drawf.light(x1, y1, x2, y2);
    }

    @Override
    public void drawLaser(float x1, float y1, float x2, float y2, int size1, int size2) {
        Building build = Vars.world.buildWorld(x1, y1), other = Vars.world.buildWorld(x2, y2);
        CableBlock block1 = null, block2 = null;
        boolean end1 = true, end2 = true;

        if (build != null && build.block instanceof CableBlock block) {
            block1 = block;
        } else if (otherReq != null) {
            block1 = this;
        } else if (Vars.control.input.block instanceof CableBlock block) {
            block1 = block;
        }

        if (other != null && other.block instanceof CableBlock block) {
            block2 = block;
        } else if (otherReq != null && otherReq.block instanceof CableBlock block) {
            block2 = block;
        }

        float angle1 = Angles.angle(x1, y1, x2, y2),
                vx = Mathf.cosDeg(angle1), vy = Mathf.sinDeg(angle1),
                len1 = size1 * Vars.tilesize / 2f, len2 = size2 * Vars.tilesize / 2f;

        if (block1 != null) {
            len1 = block1.topOffset();
            end1 = false;
        }

        if (block2 != null) {
            len2 = block2.topOffset();
            end2 = false;
        }

        drawCable(x1 + vx * len1, y1 + vy * len1, x2 - vx * len2, y2 - vy * len2, end1, end2, laserScale);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        Tile tile = Vars.world.tile(x, y);

        if (tile == null || !autolink) return;

        Lines.stroke(1f);
        Draw.color(Pal.placing);
        Drawf.circles(x * Vars.tilesize + offset, y * Vars.tilesize + offset, laserRange * Vars.tilesize);

        getPotentialLinks(tile, Vars.player.team(), other -> {
            Draw.alpha(Renderer.laserOpacity * 0.5f);
            drawLaser(x * Vars.tilesize + offset, y * Vars.tilesize + offset, other.x, other.y, 0, other.block.size);

            Drawf.square(other.x, other.y, other.block.size * Vars.tilesize / 2f + 2f, Pal.place);
        });

        Draw.reset();
    }

    @Override
    protected void setupColor(float satisfaction) {
        Draw.color();
        Draw.alpha(Renderer.laserOpacity);
    }

    @Override
    public float topOffset() {
        return topOffset;
    }

    public class CableNodeBuild extends PowerNodeBuild {
        public float warmup;

        @Override
        public void updateTile() {
            warmup = Mathf.lerpDelta(warmup, power.graph.getSatisfaction(), warmupSpeed);
        }

        @Override
        public void draw() {
            realSatisfaction = warmup;
            super.draw();

            Draw.z(Layer.power + 0.01f);
            Draw.rect(top, x, y);

            Draw.z(Layer.power + 0.02f);
            Draw.color(laserColor2, realSatisfaction);
            Draw.blend(Blending.additive);
            Draw.rect(glow, x, y);
            Draw.blend();
            Draw.color();


            realSatisfaction = 0f;
        }
    }
}
