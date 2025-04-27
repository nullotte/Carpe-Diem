package carpediem.world.blocks.power;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.world.blocks.power.*;

public class CableConsumeGenerator extends ConsumeGenerator implements CableBlock {
    public float topOffset;

    public CableConsumeGenerator(String name) {
        super(name);
    }

    @Override
    public float topOffset() {
        return topOffset;
    }

    @Override
    public TextureRegion[] icons() {
        // kinda sucks that this is hardcoded but oh well
        return new TextureRegion[]{Core.atlas.find(name + "-icon")};
    }

    public class CableConsumeGeneratorBuild extends ConsumeGeneratorBuild {
        public float cableWarmup;

        @Override
        public void updateTile() {
            super.updateTile();
            cableWarmup = Mathf.lerpDelta(cableWarmup, power.graph.getSatisfaction(), warmupSpeed);
        }

        @Override
        public float warmup() {
            return cableWarmup;
        }
    }
}
