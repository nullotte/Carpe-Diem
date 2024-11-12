package carpediem.world.blocks.storage;

import arc.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;

public class DrawerCoreBlock extends CoreBlock {
    public DrawBlock drawer = new DrawDefault();

    public DrawerCoreBlock(String name) {
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

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out) {
        // dont actually need to do this...
        drawer.getRegionsToOutline(this, out);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        Tile tile = Vars.world.tile(x, y);

        if (tile != null && !canPlaceOn(tile, Vars.player.team(), rotation)) {
            drawPlaceText(Core.bundle.get("bar.alreadyplaced"), x, y, valid);
        }
    }

    public class DrawerCoreBuild extends CoreBuild {
        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public void drawLight() {
            drawer.drawLight(this);
        }
    }
}
