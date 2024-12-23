package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import carpediem.world.blocks.storage.DrawerCoreBlock.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawCoreDoor extends DrawBlock {
    public TextureRegion[] regions;
    public float distance = 5f;
    public Interp interp = a -> Interp.pow2Out.apply(Mathf.clamp(Mathf.slope(a) * 3f));

    @Override
    public void draw(Building build) {
        for (int i = 0; i < 2; i++) {
            Draw.rect(regions[i], build.x + (interp.apply(build.progress()) * distance) * Mathf.signs[i], build.y);
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
        regions = new TextureRegion[]{
                Core.atlas.find(block.name + "-door-l"),
                Core.atlas.find(block.name + "-door-r")
        };
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return regions;
    }
}
