package carpediem.world.blocks.logic;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.blocks.logic.*;
import mindustry.world.draw.*;

public class DrawerLogicBlock extends LogicBlock {
    public DrawBlock drawer = new DrawDefault();

    public DrawerLogicBlock(String name) {
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

    public class DrawerLogicBuild extends LogicBuild {
        @Override
        public void draw() {
            drawer.draw(this);
        }
    }
}
