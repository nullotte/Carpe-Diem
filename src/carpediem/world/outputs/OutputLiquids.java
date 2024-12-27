package carpediem.world.outputs;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class OutputLiquids extends Output {
    public final LiquidStack[] liquids;

    public OutputLiquids(LiquidStack[] liquids) {
        this.liquids = liquids;
    }

    @Override
    public void apply(Block block) {
        block.hasLiquids = true;
        block.outputsLiquid = true;
    }

    @Override
    public void update(Building build) {
        float inc = build.getProgressIncrease(1f);
        for (LiquidStack stack : liquids) {
            build.handleLiquid(build, stack.liquid, Math.min(stack.amount * inc, build.block.liquidCapacity - build.liquids.get(stack.liquid)));
        }
    }

    @Override
    public void dump(Building build) {
        for (LiquidStack stack : liquids) {
            build.dumpLiquid(stack.liquid, 2f);
        }
    }

    @Override
    public boolean full(Building build) {
        for (LiquidStack stack : liquids) {
            if (build.liquids.get(stack.liquid) >= build.block.liquidCapacity - 0.001f) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void display(Stats stats) {
        stats.add(Stat.output, StatValues.liquids(1f, true, liquids));
    }
}
