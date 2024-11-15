package carpediem.world.consumers;

import arc.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

public class ConsumeItemsUses extends ConsumeItems {
    public int uses;

    public ConsumeItemsUses(int uses, ItemStack[] items) {
        super(items);
        this.uses = uses;
    }

    @Override
    public void build(Building build, Table table) {
        table.table(c -> {
            int i = 0;
            for (ItemStack stack : items) {
                c.add(new ReqImage(new ItemImage(stack.item.uiIcon, Math.round(stack.amount * multiplier.get(build))),
                        () -> (build instanceof UseCounter cbuild && cbuild.getUses() > 0) || build.items.has(stack.item, Math.round(stack.amount * multiplier.get(build))))).padRight(8);
                if (++i % 4 == 0) c.row();
            }
        }).left();
    }

    @Override
    public void trigger(Building build) {
        if (build instanceof UseCounter cbuild) {
            if (cbuild.getUses() > 0) {
                cbuild.removeUses(1);
            } else if (build.items.has(items, multiplier.get(build))) {
                cbuild.addUses(uses);
                super.trigger(build);
            }
        }
    }

    @Override
    public float efficiency(Building build) {
        return build instanceof UseCounter cbuild && cbuild.getUses() > 0 ? 1f : super.efficiency(build);
    }

    // this whole mod is so poorly coded holy fuck
    @Override
    public void display(Stats stats) {
        float timePeriod = stats.timePeriod;
        if (timePeriod > 0f) {
            stats.timePeriod *= (uses + 1);
        }
        super.display(stats);
        stats.timePeriod = timePeriod;
        stats.add(booster ? Stat.booster : Stat.input, "[lightgray]" + Core.bundle.format("stat.consumeuses", uses + 1));
    }

    public interface UseCounter {
        int getUses();

        void addUses(int uses);

        void removeUses(int uses);
    }
}
