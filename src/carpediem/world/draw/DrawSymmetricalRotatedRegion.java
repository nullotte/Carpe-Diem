package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawSymmetricalRotatedRegion extends DrawBlock {
    public TextureRegion region;
    public String suffix = "";

    public DrawSymmetricalRotatedRegion() {
    }

    public DrawSymmetricalRotatedRegion(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public void draw(Building build) {
        Draw.yscl = Mathf.sign(build.rotation == 0 || build.rotation == 2);
        Draw.rect(region, build.x, build.y, build.rotation == 0 || build.rotation == 2 ? 0f : 90f);
        Draw.yscl = 1f;
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        super.drawPlan(block, plan, list);
        Draw.yscl = Mathf.sign(plan.rotation == 0 || plan.rotation == 2);
        Draw.rect(region, plan.drawx(), plan.drawy(), plan.rotation == 0 || plan.rotation == 2 ? 0f : 90f);
        Draw.yscl = 1f;
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{region};
    }

    @Override
    public void load(Block block) {
        region = Core.atlas.find(block.name + suffix);
    }
}