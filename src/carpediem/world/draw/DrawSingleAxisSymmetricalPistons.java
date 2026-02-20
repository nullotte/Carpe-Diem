package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawSingleAxisSymmetricalPistons extends DrawBlock {
    public TextureRegion[] leftPistons, rightPistons, leftPistonShadows, rightPistonShadows;
    public int pistonCount = 3;
    public float sinMag = 1.5f, sinScl = 2.5f;

    @Override
    public void draw(Building build) {
        boolean horizontal = build.rotation == 0 || build.rotation == 2;
        int r = horizontal ? 0 : 1, dr = horizontal ? 1 : 0;
        int dx = Geometry.d4x[dr], dy = Geometry.d4y[dr];

        for (int i = 0; i < pistonCount; i++) {
            float leftLength = -Mathf.absin(build.totalProgress() + sinScl * (Mathf.PI * ((float) i / pistonCount)), sinScl, sinMag);
            float rightLength = Mathf.absin(build.totalProgress() + sinScl * (Mathf.PI * ((float) ((i + 1) % pistonCount) / pistonCount)), sinScl, sinMag);
            float leftXOffset = dx * leftLength, leftYOffset = dy * leftLength;
            float rightXOffset = dx * rightLength, rightYOffset = dy * rightLength;

            Draw.yscl = Mathf.sign(horizontal);
            Draw.rect(leftPistonShadows[i], build.x + leftXOffset, build.y + leftYOffset, r * 90f);
            Draw.rect(rightPistonShadows[i], build.x + rightXOffset, build.y + rightYOffset, r * 90f);
            Draw.rect(leftPistons[i], build.x + leftXOffset, build.y + leftYOffset, r * 90f);
            Draw.rect(rightPistons[i], build.x + rightXOffset, build.y + rightYOffset, r * 90f);
            Draw.yscl = 1f;
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        boolean horizontal = plan.rotation == 0 || plan.rotation == 2;
        int r = horizontal ? 0 : 1;

        for (int i = 0; i < pistonCount; i++) {
            Draw.rect(leftPistonShadows[i], plan.drawx(), plan.drawy(), r * 90f);
            Draw.rect(rightPistonShadows[i], plan.drawx(), plan.drawy(), r * 90f);
            Draw.yscl = Mathf.sign(horizontal);
            Draw.rect(leftPistons[i], plan.drawx(), plan.drawy(), r * 90f);
            Draw.rect(rightPistons[i], plan.drawx(), plan.drawy(), r * 90f);
            Draw.yscl = 1f;
        }
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new Seq<TextureRegion>()
                .add(leftPistonShadows).add(rightPistonShadows)
                .add(leftPistons).add(rightPistons)
                .toArray(TextureRegion.class);
    }

    @Override
    public void load(Block block) {
        leftPistons = new TextureRegion[pistonCount];
        rightPistons = new TextureRegion[pistonCount];
        leftPistonShadows = new TextureRegion[pistonCount];
        rightPistonShadows = new TextureRegion[pistonCount];
        for (int i = 0; i < pistonCount; i++) {
            leftPistons[i] = Core.atlas.find(block.name + "-piston-left" + i);
            rightPistons[i] = Core.atlas.find(block.name + "-piston-right" + i);
            leftPistonShadows[i] = Core.atlas.find(block.name + "-piston-shadow-left" + i);
            rightPistonShadows[i] = Core.atlas.find(block.name + "-piston-shadow-right" + i);
        }
    }
}
