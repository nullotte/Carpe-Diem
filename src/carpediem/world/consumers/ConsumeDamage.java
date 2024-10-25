package carpediem.world.consumers;

import mindustry.gen.*;
import mindustry.world.consumers.*;

// the concept is baffling.
// technically it could be called "ConsumeHealth" since it . yknow
public class ConsumeDamage extends Consume {
    public float damage;

    public ConsumeDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public void trigger(Building build) {
        build.damage(damage);
    }
}
