package carpediem.world.draw;

import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.world.draw.*;

public class DrawAlwaysGlowRegion extends DrawGlowRegion {
    @Override
    public void draw(Building build) {
        float z = Draw.z();
        if (layer > 0) Draw.z(layer);
        Draw.blend(blending);
        Draw.color(color);
        Draw.alpha((Mathf.absin(build.totalProgress(), glowScale, alpha) * glowIntensity + 1f - glowIntensity) * alpha);
        Draw.rect(region, build.x, build.y, build.totalProgress() * rotateSpeed + (rotate ? build.rotdeg() : 0f));
        Draw.reset();
        Draw.blend();
        Draw.z(z);
    }
}
