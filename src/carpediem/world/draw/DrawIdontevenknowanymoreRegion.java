package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawIdontevenknowanymoreRegion extends DrawBlock {
    public TextureRegion region;
    public String suffix;
    public float layer = Layer.blockOver;

    public DrawIdontevenknowanymoreRegion(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public void draw(Building build) {
        float z = Draw.z();
        Draw.z(layer);
        Draw.yscl = Mathf.sign(build.rotation == 0 || build.rotation == 3);
        Draw.rect(region, build.x, build.y, build.drawrot());
        Draw.yscl = 1f;
        Draw.z(z);
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        super.drawPlan(block, plan, list);
        Draw.yscl = Mathf.sign(plan.rotation == 0 || plan.rotation == 3);
        Draw.rect(region, plan.drawx(), plan.drawy(), plan.rotation * 90f);
        Draw.yscl = 1f;
    }

    @Override
    public void load(Block block) {
        region = Core.atlas.find(block.name + suffix);
    }
}
