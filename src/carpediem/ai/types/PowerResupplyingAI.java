package carpediem.ai.types;

import arc.func.*;
import arc.util.*;
import carpediem.entities.abilities.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.world.meta.*;

public class PowerResupplyingAI extends AIController {
    public Prov<AIController> provider;

    public PowerResupplyingAI(Prov<AIController> provider) {
        this.provider = provider;
    }

    @Override
    public void updateMovement() {
        moveTo(Vars.indexer.findClosestFlag(unit.x, unit.y, unit.team, BlockFlag.battery), 6f);
    }

    @Override
    public boolean useFallback() {
        UnpoweredStatusAbility ability = (UnpoweredStatusAbility) Structs.find(unit.abilities, a -> a instanceof UnpoweredStatusAbility);
        return ability == null || (ability.data / ability.powerCapacity) >= 0.2f;
    }

    @Override
    public AIController fallback() {
        return provider.get();
    }
}
