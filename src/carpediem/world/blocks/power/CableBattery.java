package carpediem.world.blocks.power;

import mindustry.world.blocks.power.*;

public class CableBattery extends Battery implements CableBlock {
    public float topOffset;

    public CableBattery(String name) {
        super(name);
    }

    @Override
    public float topOffset() {
        return topOffset;
    }
}
