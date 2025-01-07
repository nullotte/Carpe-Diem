package carpediem.world.consumers;

import carpediem.world.blocks.crafting.PressureCrafter.*;
import carpediem.world.meta.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

public class ConsumePressure extends Consume {
    public float usage = 5f;

    @Override
    public float efficiency(Building build) {
        float sum = 0f;
        for (Building b : build.proximity) {
            // that sure is a check
            if (b instanceof PressureCrafterBuild other && (!other.block.rotate || other.front() == build)) {
                sum += other.pressure;
            }
        }

        return sum / usage;
    }

    @Override
    public float efficiencyMultiplier(Building build) {
        return Math.max(optional ? 1f : 0f, efficiency(build));
    }

    @Override
    public void display(Stats stats) {
        stats.add(booster ? Stat.booster : Stat.input, usage, CDStat.pressureUnits);
    }
}
