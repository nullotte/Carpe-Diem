package carpediem.world.blocks.crafting;

import arc.scene.ui.layout.*;
import arc.struct.*;
import carpediem.world.outputs.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

public class Recipe {
    public float craftTime = 120f;

    public boolean[] itemFilter, liquidFilter;

    public Seq<Consume> consumes = new Seq<>();
    public Seq<Output> outputs = new Seq<>();

    public Recipe() {}

    public Recipe(float craftTime) {
        this.craftTime = craftTime;
    }

    public void apply(Block block) {
        for (Output output : outputs) {
            output.apply(block);
        }
    }

    public void craft(Building build) {
        for (Output output : outputs) {
            output.trigger(build);
        }
    }

    public void update(Building build) {
        for (Output output : outputs) {
            output.update(build);
        }
    }

    public void dumpOutputs(Building build) {
        for (Output output : outputs) {
            output.dump(build);
        }
    }

    public void dumpTimedOutputs(Building build) {
        for (Output output : outputs) {
            output.dumpTimed(build);
        }
    }

    // TODO stinkyy
    public void display(Table table, float craftingSpeed) {
        Stats recipeStats = new Stats();
        recipeStats.timePeriod = craftTime / craftingSpeed;

        for (Consume consume : consumes) {
            consume.display(recipeStats);
        }
        for (Output output : outputs) {
            output.display(recipeStats);
        }

        table.table(Styles.grayPanel, t -> {
            t.table(input -> {
                input.left();

                OrderedMap<Stat, Seq<StatValue>> map = recipeStats.toMap().get(StatCat.crafting);
                Seq<StatValue> arr = map.get(Stat.input);
                for (StatValue value : arr) {
                    value.display(input);
                }
            }).left().grow().pad(10f);

            t.table(arrow -> {
                arrow.image(Icon.right).color(Pal.darkishGray).size(40f);
            }).pad(10f);

            t.table(output -> {
                output.right();

                OrderedMap<Stat, Seq<StatValue>> map = recipeStats.toMap().get(StatCat.crafting);
                Seq<StatValue> arr = map.get(Stat.output);
                for (StatValue value : arr) {
                    value.display(output);
                }
            }).right().grow().pad(10f);
        }).growX().pad(5f);
    }

    public boolean consumesItem(Item item) {
        return itemFilter[item.id];
    }

    public boolean consumesLiquid(Liquid liquid) {
        return liquidFilter[liquid.id];
    }

    public boolean valid(Building build) {
        for (Consume consume : consumes) {
            // honestly,
            if (!(consume.efficiency(build) > 0f)) {
                return false;
            }
        }

        return true;
    }

    public boolean shouldConsume(Building build) {
        for (Output output : outputs) {
            if (output.full(build)) return false;
        }

        return true;
    }

    // builder stuff
    public Recipe consume(Consume consume) {
        consumes.add(consume);
        return this;
    }

    public Recipe output(Output output) {
        outputs.add(output);
        return this;
    }

    public Recipe consumeItem(Item item) {
        return consumeItem(item, 1);
    }

    public Recipe consumeItem(Item item, int amount) {
        return consume(new ConsumeItems(new ItemStack[]{new ItemStack(item, amount)}));
    }

    public Recipe consumeItems(ItemStack... items) {
        return consume(new ConsumeItems(items));
    }

    public Recipe consumeLiquid(Liquid liquid, float amount) {
        return consume(new ConsumeLiquids(new LiquidStack[]{new LiquidStack(liquid, amount)}));
    }

    public Recipe outputItem(Item item) {
        return outputItem(item, 1);
    }

    public Recipe outputItem(Item item, int amount) {
        return output(new OutputItems(new ItemStack[]{new ItemStack(item, amount)}));
    }

    public Recipe outputLiquid(Liquid liquid, float amount) {
        return output(new OutputLiquids(new LiquidStack[]{new LiquidStack(liquid, amount)}));
    }
}
