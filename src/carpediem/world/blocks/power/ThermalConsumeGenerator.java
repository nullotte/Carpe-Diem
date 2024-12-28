package carpediem.world.blocks.power;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.world.blocks.power.*;

public class ThermalConsumeGenerator extends ThermalGenerator implements CableBlock {
    // if v8 thermal generator ends up having this behavior and what ive done is for nothing im going to cry
    // its gonna become like "cablethermalgenerator" or some shit
    public float itemDuration = 120f;

    public float warmupSpeed = 0.05f;
    public Effect consumeEffect = Fx.none;
    public float generateEffectRange = 3f;
    public float topOffset;

    public ThermalConsumeGenerator(String name) {
        super(name);
        // it uses efficiency, which is already affected by whether or not the block is enabled
        noUpdateDisabled = false;
        // AAAAAAGHGHJGJ
        displayEfficiency = false;
    }

    @Override
    public void setStats() {
        // man why doesnt vanilla do this ?
        stats.timePeriod = itemDuration;
        super.setStats();
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);

        drawPlaceText(Core.bundle.formatFloat("bar.efficiency", sumAttribute(attribute, x, y) * 100f * displayEfficiencyScale, 1), x, y, valid);
    }

    @Override
    public float topOffset() {
        return topOffset;
    }

    public class ThermalConsumeGeneratorBuild extends ThermalGeneratorBuild {
        public float warmup, cableWarmup, totalTime;

        @Override
        public void updateTile() {
            boolean valid = efficiency > 0f;

            warmup = Mathf.lerpDelta(warmup, valid ? 1f : 0f, warmupSpeed);
            cableWarmup = Mathf.lerpDelta(cableWarmup, power.graph.getSatisfaction(), warmupSpeed);
            totalTime += warmup * Time.delta;
            productionEfficiency = (sum + attribute.env()) * efficiency;

            if (valid && Mathf.chanceDelta(effectChance)) {
                generateEffect.at(x + Mathf.range(generateEffectRange), y + Mathf.range(generateEffectRange));
            }

            // items
            if (hasItems && valid && generateTime <= 0f) {
                consume();
                consumeEffect.at(x + Mathf.range(generateEffectRange), y + Mathf.range(generateEffectRange));
                generateTime = 1f;
            }

            if (outputLiquid != null) {
                float added = Math.min(productionEfficiency * delta() * outputLiquid.amount, liquidCapacity - liquids.get(outputLiquid.liquid));
                liquids.add(outputLiquid.liquid, added);
                dumpLiquid(outputLiquid.liquid);
            }

            generateTime -= delta() / itemDuration;
        }

        @Override
        public boolean consumeTriggerValid() {
            return generateTime > 0f;
        }

        @Override
        public float warmup() {
            // used for the glow
            return cableWarmup;
        }

        @Override
        public float totalProgress() {
            return totalTime;
        }
    }
}
