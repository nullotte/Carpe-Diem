package carpediem.world.blocks.storage;

import arc.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.world.*;

// just a core thats visible but cant be built
public class LandingPod extends DrawerCoreBlock {
    public LandingPod(String name) {
        super(name);
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        return Vars.state.isEditor();
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        Tile tile = Vars.world.tile(x, y);

        if (tile != null && !canPlaceOn(tile, Vars.player.team(), rotation)) {
            drawPlaceText(Core.bundle.get("bar.useassembler"), x, y, valid);
        }
    }
}
