package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// this is kinda just drawcoredoor but sideways . oh well
public class DrawPress extends DrawBlock {
    public TextureRegion[] regions;
    public float length = 3f;
    public Interp interp = a -> Interp.pow2Out.apply(Mathf.curve(a, 0.2f, 0.9f) - Mathf.curve(a, 0.9f));

    @Override
    public void draw(Building build) {
        for (int i = 0; i < 2; i++) {
            Draw.rect(regions[i], build.x, build.y + (interp.apply(build.progress()) * length) * Mathf.signs[i]);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        for (TextureRegion region : regions) {
            Draw.rect(region, plan.drawx(), plan.drawy());
        }
    }

    @Override
    public void load(Block block) {
        regions = new TextureRegion[2];

        for (int i = 0; i < 2; i++) {
            regions[i] = Core.atlas.find(block.name + "-press" + i);
        }
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return regions;
    }
}
