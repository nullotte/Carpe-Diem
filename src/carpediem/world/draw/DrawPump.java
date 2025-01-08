package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawPump extends DrawBlock {
    public TextureRegion region0, region1, iconRegion;
    public float speed = 0.1f, length = 18f / 4f;

    @Override
    public void draw(Building build) {
        for (int i = 0; i < 4; i++) {
            float len = (build.totalProgress() * speed) % length;
            Draw.rect(i == 0 || i == 3 ? region0 : region1, build.x + Geometry.d4x(i) * len, build.y + Geometry.d4y(i) * len, i * 90f);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(iconRegion, plan.drawx(), plan.drawy());
    }

    @Override
    public void load(Block block) {
        region0 = Core.atlas.find(block.name + "-pump0");
        region1 = Core.atlas.find(block.name + "-pump1");
        iconRegion = Core.atlas.find(block.name + "-pump-icon");
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{iconRegion};
    }
}
