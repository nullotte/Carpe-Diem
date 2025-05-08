package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawPress extends DrawBlock {
    public TextureRegion pressRegion;
    public float length = 1f;
    public Interp interp = a -> {
        float b = Mathf.slope(a) * 3.5f;
        float c = Mathf.clamp(b);
        return Interp.pow3Out.apply(c);
    };

    @Override
    public void draw(Building build) {
        float dst = interp.apply(build.progress()) * length;
        Draw.color(0f, 0f, 0f, 0.4f);
        Draw.rect(pressRegion, build.x - dst, build.y - dst);
        Draw.color();
        Draw.rect(pressRegion, build.x + dst, build.y + dst);
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(pressRegion, plan.drawx(), plan.drawy());
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{pressRegion};
    }

    @Override
    public void load(Block block) {
        pressRegion = Core.atlas.find(block.name + "-press");
    }
}
