package carpediem.entities.abilities;

import arc.util.*;
import mindustry.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.type.*;

public class AmmoStatusAbility extends Ability {
    public StatusEffect effect;
    public float duration, reload;

    protected float timer;

    public AmmoStatusAbility(StatusEffect effect, float duration, float reload) {
        this.effect = effect;
        this.duration = duration;
        this.reload = reload;
    }

    @Override
    public void update(Unit unit) {
        if ((timer += Time.delta) >= reload && (unit.ammo > 0f || !Vars.state.rules.unitAmmo)) {
            unit.apply(effect, duration);

            if (Vars.state.rules.unitAmmo) {
                unit.ammo--;
            }

            timer = 0f;
        }
    }
}
