package carpediem.ai.types;

import mindustry.*;
import mindustry.ai.types.*;
import mindustry.world.meta.*;

public class ReloadingCommandAI extends CommandAI {
    @Override
    public void updateUnit() {
        if (Vars.state.rules.unitAmmo && (unit.ammo / unit.type.ammoCapacity) < 0.2f) {
            moveTo(Vars.indexer.findClosestFlag(unit.x, unit.y, unit.team, BlockFlag.battery), 6f);
            updateVisuals();
        } else {
            super.updateUnit();
        }
    }
}
