package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// that makes three i think?
// four? five? i've lost count
public class DrawPressurizationChamber extends DrawBlock {
    public TextureRegion press1, press2, node, icon;
    public float pressLength = 9.7f, nodeLength = 6f, speed = 3f;

    @Override
    public void draw(Building build) {
        float px = Angles.trnsx(build.totalProgress() * speed + build.rotdeg(), pressLength);
        float py = Angles.trnsy(build.totalProgress() * speed + build.rotdeg(), pressLength);
        float nx = Angles.trnsx(build.totalProgress() * speed + build.rotdeg(), nodeLength);
        float ny = Angles.trnsy(build.totalProgress() * speed + build.rotdeg(), nodeLength);

        if (build.rotation % 2 == 0) {
            py = 0f;
        } else {
            px = 0f;
        }

        Draw.rect(build.rotation > 1 ? press2 : press1, build.x + px, build.y + py, build.rotdeg());
        Drawf.spinSprite(node, build.x + nx, build.y + ny, build.totalProgress() * speed + build.rotdeg());
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        int gx = Geometry.d4x(plan.rotation), gy = Geometry.d4y(plan.rotation);
        Draw.rect(plan.rotation > 1 ? press2 : press1, plan.drawx() + gx * pressLength, plan.drawy() + gy * pressLength, plan.rotation * 90f);
        Draw.rect(node, plan.drawx() + gx * nodeLength, plan.drawy() + gy * nodeLength);
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{icon};
    }

    @Override
    public void load(Block block) {
        press1 = Core.atlas.find(block.name + "-press1");
        press2 = Core.atlas.find(block.name + "-press2");
        node = Core.atlas.find(block.name + "-press-node");
        icon = Core.atlas.find(block.name + "-press-icon");
    }
}
