package carpediem.world.blocks.distribution;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.draw.*;

// i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe i can't breathe
public class Splitter extends DuctRouter {
    public DrawBlock drawer;

    public Splitter(String name) {
        super(name);
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("items");
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

    public class DrawerRouterBuild extends DuctRouterBuild {
        @Override
        public void draw() {
            drawer.draw(this);
        }
    }
}
