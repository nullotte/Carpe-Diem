package carpediem.world.blocks.storage;

import arc.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.ConstructBlock.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;

// originally just a core block that could only be placed once
// but theres so much stuff tacked on that now idk what to call it
public class HubBlock extends CoreBlock {
    public DrawBlock drawer = new DrawDefault();

    public HubBlock(String name) {
        super(name);
        configurable = true;

        config(Boolean.class, (LimitedPlaceCoreBuild core, Boolean b) -> core.lockDeconstruct = true);
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
    public void setBars() {
        super.setBars();
        removeBar("items");
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        return !team.data().buildings.contains(build -> build.block == this || (build instanceof ConstructBuild construct && construct.current == this));
    }

    @Override
    public boolean canBreak(Tile tile) {
        return tile.build instanceof LimitedPlaceCoreBuild core && !core.lockDeconstruct;
    }

    @Override
    public boolean canReplace(Block other) {
        // dont try to replace other cores
        return super.canReplace(other) && !(other instanceof CoreBlock);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        Tile tile = Vars.world.tile(x, y);

        if (tile != null && !canPlaceOn(tile, Vars.player.team(), rotation)) {
            drawPlaceText(Core.bundle.get("bar.alreadyplaced"), x, y, valid);
        }
    }


    public class LimitedPlaceCoreBuild extends CoreBuild {
        public boolean lockDeconstruct = true;

        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public void drawLight() {
            drawer.drawLight(this);
        }

        @Override
        public void buildConfiguration(Table table) {
            table.background(Styles.black6);
            table.check("@lockdeconstruction", lockDeconstruct, b -> lockDeconstruct = b).pad(5f);
        }
    }
}
