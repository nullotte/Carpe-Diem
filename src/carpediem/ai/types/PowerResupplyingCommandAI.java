package carpediem.ai.types;

import arc.util.*;
import carpediem.entities.abilities.*;
import mindustry.*;
import mindustry.ai.types.*;
import mindustry.world.meta.*;

public class PowerResupplyingCommandAI extends CommandAI {
    @Override
    public void updateUnit() {
        UnpoweredStatusAbility ability = (UnpoweredStatusAbility) Structs.find(unit.abilities, a -> a instanceof UnpoweredStatusAbility);
        if (ability != null && (ability.data / ability.powerCapacity) < 0.2f) {
            moveTo(Vars.indexer.findClosestFlag(unit.x, unit.y, unit.team, BlockFlag.battery), 6f);
            updateVisuals();
        } else {
            super.updateUnit();
        }
    }
}
