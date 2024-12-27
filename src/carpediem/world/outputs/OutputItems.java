package carpediem.world.outputs;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class OutputItems extends Output {
    public final ItemStack[] items;

    public OutputItems(ItemStack[] items) {
        this.items = items;
    }

    @Override
    public void apply(Block block) {
        block.hasItems = true;
    }

    @Override
    public void trigger(Building build) {
        for (ItemStack stack : items) {
            for (int i = 0; i < stack.amount; i++) {
                build.offload(stack.item);
            }
        }
    }

    @Override
    public void dumpTimed(Building build) {
        for (ItemStack stack : items) {
            build.dump(stack.item);
        }
    }

    @Override
    public boolean full(Building build) {
        for (ItemStack stack : items) {
            if (build.items.get(stack.item) + stack.amount > build.getMaximumAccepted(stack.item)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void display(Stats stats) {
        stats.add(Stat.output, stats.timePeriod < 0 ? StatValues.items(items) : StatValues.items(stats.timePeriod, items));
    }
}
