package carpediem.world.blocks.storage;

import arc.scene.ui.layout.*;
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

    public class LimitedPlaceCoreBuild extends DrawerCoreBuild {
        public boolean lockDeconstruct = true;

        @Override
        public void buildConfiguration(Table table) {
            table.background(Styles.black6);
            table.check("@lockdeconstruction", lockDeconstruct, b -> lockDeconstruct = b).pad(5f);
        }
    }
}
