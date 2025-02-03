package carpediem.world.blocks.storage;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;

// fake core block that tells the launch platform to launch a certain real core block
// TODO add a method to load the crude landing pod into the launch platform without using payload blocks
public class PackagedCoreBlock extends StorageBlock {
    public DrawerCoreBlock coreType;

    public PackagedCoreBlock(String name, DrawerCoreBlock coreType) {
        super(name);
        coreMerge = false;
        this.coreType = coreType;

        size = coreType.size;
        itemCapacity = coreType.itemCapacity;

        requirements(coreType.category, BuildVisibility.campaignOnly, coreType.requirements);
    }

    @Override
    public void load() {
        super.load();
        region = coreType.region;
    }

    @Override
    protected TextureRegion[] icons() {
        return coreType.drawer.finalIcons(coreType);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        coreType.drawer.drawPlan(coreType, plan, list);
    }

    public class PackagedCoreBuild extends StorageBuild {
        @Override
        public void draw() {
            coreType.drawer.draw(this);
        }

        @Override
        public void drawTeamTop() {
            if (coreType.teamRegion.found()) {
                if (coreType.teamRegions[team.id] == coreType.teamRegion) {
                    Draw.color(team.color);
                }

                Draw.rect(coreType.teamRegions[team.id], x, y);
                Draw.color();
            }
        }
    }
}
