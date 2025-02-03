package carpediem.world.draw;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.draw.*;

public class DrawScanner extends DrawBlock {
    public Color color = Pal.sapBullet;
    public float stroke = 2f, scl = 10f;

    @Override
    public void draw(Building build) {
        Building front = build.front();
        if (front != null && build.warmup() > 0f) {
            float z = Draw.z();

            Draw.z(Layer.buildBeam);
            Draw.color(color, build.warmup());
            Drawf.buildBeam(build.x, build.y, front.x, front.y, front.block.size * Vars.tilesize / 2f);
            Fill.square(front.x, front.y, front.block.size * Vars.tilesize / 2f);
            Fill.square(build.x, build.y, 1.8f + Mathf.absin(Time.time, 2.2f, 1.1f), 45f);

            Draw.z(Layer.blockOver);
            Lines.stroke(stroke);
            Lines.square(front.x, front.y, front.block.size * Vars.tilesize / 2f);
            Lines.lineAngleCenter(
                    front.x + Mathf.sin(build.totalProgress(), scl, (front.block.size * Vars.tilesize / 2f) - stroke / 2f),
                    front.y,
                    90f, front.block.size * Vars.tilesize, false
            );

            Draw.z(z);
            Draw.reset();
        }
    }
}
