package carpediem.world.blocks.storage;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;

// if i had a dollar for every custom class that was just "vanilla block + drawer"
public class DrawerStorageBlock extends StorageBlock {
    public DrawBlock drawer = new DrawDefault();

    public DrawerStorageBlock(String name) {
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

    public class DrawerStorageBuild extends StorageBuild {
        @Override
        public void draw() {
            drawer.draw(this);
        }
    }
}
