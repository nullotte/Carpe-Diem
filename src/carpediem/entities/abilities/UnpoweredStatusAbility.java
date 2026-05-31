package carpediem.entities.abilities;

import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import carpediem.content.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;

public class UnpoweredStatusAbility extends Ability {
    public static final float resupplyInterval = 10f;

    public StatusEffect statusEffect = CDStatusEffects.unpowered;
    public float statusDuration = 1f;
    public float resupplyRange = 85f; // just copied from the now removed PowerAmmoType
    public float powerDrain = (50f * 60f);
    public float powerCapacity;

    protected float resupplyTimer;

    public UnpoweredStatusAbility(float powerCapacity) {
        this.powerCapacity = powerCapacity;
    }

    @Override
    public void addStats(Table t) {
        super.addStats(t);
        t.add(abilityStat("powercapacity", powerCapacity));
        t.row();
        t.add(abilityStat("powerdrain", powerDrain * 60f));
    }

    @Override
    public void displayBars(Unit unit, Table bars) {
        bars.add(new Bar("bar.power", Pal.powerLight, () -> data / powerCapacity)).row();
    }

    @Override
    public void created(Unit unit) {
        data = powerCapacity;
    }

    @Override
    public void update(Unit unit) {
        if ((resupplyTimer += Time.delta) >= resupplyInterval) {
            resupplyTimer = 0f;
            Building build = Units.closestBuilding(unit.team, unit.x, unit.y, resupplyRange + unit.hitSize, u -> u.block.consPower != null && u.block.consPower.buffered);
            if (build != null) {
                float powerTaken = Math.min(build.power.status * build.block.consPower.capacity, powerCapacity - data);
                if (powerTaken > 0f) {
                    build.power.status -= powerTaken / build.block.consPower.capacity;
                    data += powerTaken;
                    Fx.itemTransfer.at(build.x, build.y, Math.max(powerTaken / 100f, 1f), Pal.power, unit);
                }
            }
        }

        data = Mathf.maxZero(data - (powerDrain * Time.delta));

        if (data <= 0f) {
            unit.apply(statusEffect, statusDuration);
        }
    }
}
