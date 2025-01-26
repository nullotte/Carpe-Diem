package carpediem.ai.types;

import arc.func.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.world.meta.*;

public class ReloadingAI extends AIController {
    public Prov<AIController> provider;

    public ReloadingAI(Prov<AIController> provider) {
        this.provider = provider;
    }

    @Override
    public void updateMovement() {
        moveTo(Vars.indexer.findClosestFlag(unit.x, unit.y, unit.team, BlockFlag.battery), 6f);
    }

    @Override
    public boolean useFallback() {
        return !(Vars.state.rules.unitAmmo && (unit.ammo / unit.type.ammoCapacity) < 0.2f);
    }

    @Override
    public AIController fallback() {
        return provider.get();
    }
}
