package carpediem.world.blocks.production;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;

// wow!!!!!! this mod has so many different custom block classes!!!!!!!! 99% of them are vanilla block + drawer it makes me want to
public class DrawerIncinerator extends Incinerator {
    public DrawBlock drawer;

    public DrawerIncinerator(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        drawer.load(this);
    }

    @Override
    protected TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    public class DrawerIncineratorBuild extends IncineratorBuild {
        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public float warmup() {
            return heat;
        }
    }
}
