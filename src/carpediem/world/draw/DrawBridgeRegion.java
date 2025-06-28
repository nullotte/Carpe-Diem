package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import carpediem.world.blocks.distribution.*;
import carpediem.world.blocks.distribution.BeltBridge.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// AAAHHH
public class DrawBridgeRegion extends DrawBlock {
    public TextureRegion[] in, out;

    @Override
    public void draw(Building build) {
        if (build instanceof BeltBridgeBuild bridge) {
            Draw.rect(!bridge.out ? in[build.rotation] : out[build.rotation], build.x, build.y);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        boolean planOut = false;

        if (plan.config != null) {
            planOut = plan.config == Boolean.TRUE;
        } else {
            // this shit is so ass
            int dx = Geometry.d4x(plan.rotation), dy = Geometry.d4y(plan.rotation);
            for (int i = 1; i <= ((BeltBridge) block).range; i++) {
                Tile other = Vars.world.tile(plan.x + dx * -i, plan.y + dy * -i);

                if (other != null && other.build instanceof BeltBridgeBuild build && build.rotation == plan.rotation && build.block == block && build.team == Vars.player.team()) {
                    if (!build.out) {
                        planOut = true;
                    }
                    break;
                }
            }}

        Draw.rect(planOut ? out[plan.rotation] : in[plan.rotation], plan.drawx(), plan.drawy());
    }

    @Override
    public void load(Block block) {
        in = new TextureRegion[4];
        out = new TextureRegion[4];

        for (int i = 0; i < 4; i++) {
            in[i] = Core.atlas.find(block.name + "-in" + (i + 1));
            out[i] = Core.atlas.find(block.name + "-out" + (i + 1));
        }
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{in[0]};
    }
}
