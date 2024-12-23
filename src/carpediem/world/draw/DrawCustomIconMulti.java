package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawCustomIconMulti extends DrawMulti {
    public String suffix;
    public TextureRegion icon;

    public DrawCustomIconMulti(String suffix, Seq<DrawBlock> drawers) {
        this.suffix = suffix;
        this.drawers = drawers.toArray(DrawBlock.class);
    }

    @Override
    public void load(Block block) {
        super.load(block);
        icon = Core.atlas.find(block.name + suffix);
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(icon, plan.drawx(), plan.drawy());
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{icon};
    }
}
