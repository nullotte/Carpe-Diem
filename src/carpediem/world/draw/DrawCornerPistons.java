package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawCornerPistons extends DrawBlock {
    public TextureRegion[] pistonRegions;
    public float length = -2.25f;
    public Interp interp = a -> {
        float b = Mathf.slope(a) * 2f;
        float c = Mathf.clamp(b);
        return Interp.pow2Out.apply(c);
    };

    @Override
    public void draw(Building build) {
        float dst = interp.apply(build.progress()) * length;
        for (int i = 0; i < 4; i++) {
            Draw.rect(pistonRegions[i], build.x + dst * Geometry.d8edge[i].x, build.y + dst * Geometry.d8edge[i].y);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        for (int i = 0; i < 4; i++) {
            Draw.rect(pistonRegions[i], plan.drawx(), plan.drawy());
        }
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return pistonRegions;
    }

    @Override
    public void load(Block block) {
        pistonRegions = new TextureRegion[4];

        for (int i = 0; i < 4; i++) {
            pistonRegions[i] = Core.atlas.find(block.name + "-piston" + i);
        }
    }
}
