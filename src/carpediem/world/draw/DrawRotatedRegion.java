package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawRotatedRegion extends DrawBlock {
    public TextureRegion[] regions;
    public float layer = Layer.blockOver;

    @Override
    public void draw(Building build) {
        float z = Draw.z();
        Draw.z(layer);
        Draw.rect(regions[build.rotation], build.x, build.y);
        Draw.z(z);
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(regions[plan.rotation], plan.drawx(), plan.drawy());
    }

    @Override
    public void load(Block block) {
        regions = new TextureRegion[4];

        for (int i = 0; i < 4; i++) {
            regions[i] = Core.atlas.find(block.name + "-top" + i);
        }
    }
}