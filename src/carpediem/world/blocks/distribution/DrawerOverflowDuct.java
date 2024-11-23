package carpediem.world.blocks.distribution;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.draw.*;

// i hold my breath i hold my breath i hold my breath i hold my breath i hold my breath i hold my breath i hold my breath i hold my breath i hold my breath i hold my breath i hold my breath i hold my breath i hold my breath
public class DrawerOverflowDuct extends OverflowDuct {
    public DrawBlock drawer;

    public DrawerOverflowDuct(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        drawer.load(this);
    }

    @Override
    public TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    public class DrawerOverflowDuctBuild extends OverflowDuctBuild {
        @Override
        public void draw() {
            drawer.draw(this);
        }
    }
}
