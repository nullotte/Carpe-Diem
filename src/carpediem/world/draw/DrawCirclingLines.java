package carpediem.world.draw;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.draw.*;

// honestly
public class DrawCirclingLines extends DrawBlock {
    public Color color = Pal.sapBullet;
    public boolean cap = false;
    public float length = 32f, radius = 2f, scl = 20f, mag = 12f;

    @Override
    public void draw(Building build) {
        Draw.color(color, build.warmup());
        Lines.stroke(radius);
        Lines.lineAngleCenter(build.x, build.y + Mathf.sin(build.totalProgress(), scl, mag), 0f, length, cap);
        Lines.lineAngleCenter(build.x + Mathf.cos(build.totalProgress(), scl, mag), build.y, 90f, length, cap);
        Draw.reset();
    }
}
