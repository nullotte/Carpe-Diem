package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// goddammit
// not actually used now but im keeping this just in case i need it later
public class DrawRotatedRegion extends DrawBlock {
    public TextureRegion[] top;

    @Override
    public void draw(Building build) {
        Draw.rect(top[build.rotation], build.x, build.y);
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(top[plan.rotation], plan.drawx(), plan.drawy());
    }

    @Override
    public void load(Block block) {
        top = new TextureRegion[4];

        for (int i = 0; i < 4; i++) {
            top[i] = Core.atlas.find(block.name + "-top" + (i + 1));
        }
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{top[0]};
    }
}
