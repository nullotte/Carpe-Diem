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
    public TextureRegion press1, press2, pressIcon;
    public float length = 10f / 4f;
    public Interp interp = a -> {
        float b = Mathf.slope(a);
        float c = Mathf.curve(b, 0.4f, 0.8f);
        return Interp.pow2In.apply(c);
    };

    @Override
    public void draw(Building build) {
        for (int i = 0; i < 4; i++) {
            Tmp.v1.trns(i * 90f, -(interp.apply(build.progress()) * length));

            if (i % 2 != 0) {
                Draw.yscl = -1f;
            }
            Draw.rect(i > 1 ? press2 : press1, build.x + Tmp.v1.x, build.y + Tmp.v1.y, i * 90f);
            Draw.yscl = 1f;
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(pressIcon, plan.drawx(), plan.drawy());
    }

    @Override
    public void load(Block block) {
        press1 = Core.atlas.find(block.name + "-press1");
        press2 = Core.atlas.find(block.name + "-press2");
        pressIcon = Core.atlas.find(block.name + "-press-icon");
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{pressIcon};
    }
}
