package carpediem.world.blocks.storage;

import arc.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.ConstructBlock.*;
import mindustry.world.blocks.storage.*;

// placeable anywhere, but only Once
public class HubBlock extends DrawerCoreBlock {
    public HubBlock(String name) {
        super(name);
        configurable = true;

        config(Boolean.class, (LimitedPlaceCoreBuild core, Boolean b) -> core.lockDeconstruct = true);
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

    public class LimitedPlaceCoreBuild extends DrawerCoreBuild {
        public boolean lockDeconstruct = true;

        @Override
        public void buildConfiguration(Table table) {
            table.table(Styles.black6, t -> {
                t.check("@lockdeconstruction", lockDeconstruct, b -> lockDeconstruct = b).pad(5f);
            });
        }
    }
}
