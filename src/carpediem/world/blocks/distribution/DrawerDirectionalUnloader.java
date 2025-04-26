package carpediem.world.blocks.distribution;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.draw.*;

// breaking news lotte has died of asphyxiation
public class DrawerDirectionalUnloader extends DirectionalUnloader {
    public DrawBlock drawer;

    public DrawerDirectionalUnloader(String name) {
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

    @Override
    public void drawPlanConfig(BuildPlan plan, Eachable<BuildPlan> list) {
        // nah
    }

    public class DrawerDirectionalUnloaderBuild extends DirectionalUnloaderBuild {
        @Override
        public void draw() {
            drawer.draw(this);
        }
    }
}
