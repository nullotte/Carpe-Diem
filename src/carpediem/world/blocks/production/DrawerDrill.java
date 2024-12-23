package carpediem.world.blocks.production;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;

// something's approaching something's approaching something's approaching something's approaching something's approaching something's approaching something's approaching something's approaching
public class DrawerDrill extends Drill {
    public DrawBlock drawer;

    public DrawerDrill(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        drawer.load(this);
        itemRegion = Core.atlas.find("clear");
    }

    @Override
    public TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    public class DrawerDrillBuild extends DrillBuild {
        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public float totalProgress() {
            return timeDrilled;
        }
    }
}
