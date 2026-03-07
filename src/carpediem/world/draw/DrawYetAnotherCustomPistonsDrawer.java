package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// im genuinely so horrible at implementing things
public class DrawYetAnotherCustomPistonsDrawer extends DrawBlock {
    public int pistonCount = 8;
    public TextureRegion[] pistons, pistonShadows;
    public float sinMag = 1.5f, sinScl = 2.5f;

    @Override
    public void draw(Building build) {
        for (int i = 0; i < pistonCount; i++) {
            float angle = (-360f / pistonCount) * i;
            int lengthIndex = i % 2 == 0 ? i : (pistonCount - i);
            float len = -Mathf.absin(build.totalProgress() + sinScl * (Mathf.PI * ((float) lengthIndex / pistonCount)), sinScl, sinMag);
            float px = Angles.trnsx(angle, len), py = Angles.trnsy(angle, len);
            Draw.rect(pistonShadows[i], build.x + px, build.y + py);
            Draw.rect(pistons[i], build.x + px, build.y + py);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        for (int i = 0; i < pistonCount; i++) {
            Draw.rect(pistonShadows[i], plan.drawx(), plan.drawy());
            Draw.rect(pistons[i], plan.drawx(), plan.drawy());
        }
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new Seq<TextureRegion>().add(pistonShadows).add(pistons).toArray(TextureRegion.class);
    }

    @Override
    public void load(Block block) {
        pistons = new TextureRegion[pistonCount];
        pistonShadows = new TextureRegion[pistonCount];

        for (int i = 0; i < pistonCount; i++) {
            pistons[i] = Core.atlas.find(block.name + "-piston" + i);
            pistonShadows[i] = Core.atlas.find(block.name + "-piston-shadow" + i);
        }
    }
}
