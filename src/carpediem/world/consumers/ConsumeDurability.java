package carpediem.world.consumers;

import carpediem.world.meta.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

// the concept is baffling.
public class ConsumeDurability extends Consume {
    public int durability;

    public ConsumeDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void trigger(Building build) {
        build.damage(build.maxHealth() / durability);
    }

    @Override
    public void display(Stats stats) {
        stats.add(CDStat.uses, durability);
    }
}
