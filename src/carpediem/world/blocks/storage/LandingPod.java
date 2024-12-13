package carpediem.world.blocks.storage;

import arc.math.*;
import arc.struct.*;
import arc.util.io.*;
import arc.util.pooling.*;
import arc.util.pooling.Pool.*;
import carpediem.world.blocks.crafting.*;
import carpediem.world.meta.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.type.*;
import mindustry.world.consumers.*;

// IT CAN CRAFT ITEMS DEAR FUCKING GOD
public class LandingPod extends DrawerCoreBlock {
    public Seq<Recipe> recipes = new Seq<>();
    // for cancelling requests
    public static ObjectMap<Recipe, ItemStack[]> inputs = new ObjectMap<>();

    public Effect craftEffect = Fx.none;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public float warmupSpeed = 0.019f;

    public float craftingSpeed = 0.5f;

    public LandingPod(String name) {
        super(name);
    }

    @Override
    public void init() {
        for (Recipe recipe : recipes) {
            recipe.apply(this);

            if (!inputs.containsKey(recipe)) {
                ConsumeItems consume = (ConsumeItems) recipe.consumes.find(c -> c instanceof ConsumeItems);

                if (consume != null) {
                    inputs.put(recipe, consume.items);
                }
            }
        }

        super.init();
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(CDStat.recipes, table -> {
            table.row();

            for (Recipe recipe : recipes) {
                recipe.display(table, craftingSpeed);
                table.row();
            }
        });
    }

    public class LandingPodBuild extends DrawerCoreBuild {
        public Queue<RecipeRequest> pending = new Queue<>();
        public float progress;
        public float warmup;

        @Override
        public void updateTile() {
            Recipe currentRecipe = getCurrentRecipe();

            if (currentRecipe != null) {
                if (efficiency > 0) {
                    progress += getProgressIncrease(currentRecipe.craftTime * craftingSpeed);
                    warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);

                    // do i even. for consistency's sake sure but also you Cant use liquids in the core !!
                    currentRecipe.update(this);

                    if (wasVisible && Mathf.chanceDelta(updateEffectChance)) {
                        updateEffect.at(x + Mathf.range(block.size * 4f), y + Mathf.range(block.size * 4));
                    }
                } else {
                    warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
                }

                if (progress >= 1f) {
                    craft();
                }
            }
        }

        public void craft() {
            Recipe recipe = getCurrentRecipe();

            if (recipe != null) {
                recipe.craft(this);
            }

            if (wasVisible) {
                craftEffect.at(x, y);
            }

            RecipeRequest first = pending.first();
            first.stack--;
            if (first.stack <= 0) {
                // death
                pending.removeFirst();
                Pools.free(first);
            }

            progress %= 1f;
        }

        public Recipe getCurrentRecipe() {
            if (!pending.isEmpty()) {
                int currentIndex = pending.first().index;

                if (currentIndex < 0 || currentIndex >= recipes.size) {
                    return null;
                }

                return recipes.get(currentIndex);
            } else {
                return null;
            }
        }

        public void requestRecipe(int index, int amount) {
            if (index < 0 || index >= recipes.size) return;
            Recipe recipe = recipes.get(index);

            for (int i = 0; i < amount; i++) {
                if (recipe.valid(this)) {
                    if (!pending.isEmpty() && pending.last().index == index) {
                        // just add to the stack
                        pending.last().stack++;
                    } else {
                        pending.addLast(Pools.obtain(RecipeRequest.class, RecipeRequest::new).set(index, 1));
                    }

                    for (Consume consume : recipe.consumes) {
                        consume.trigger(this);
                    }
                } else {
                    break;
                }
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(progress);
            write.f(warmup);

            write.i(pending.size);
            // this is ordered right? i hope it is
            for (RecipeRequest request : pending) {
                write.i(request.index);
                write.i(request.stack);
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            progress = read.f();
            warmup = read.f();

            int size = read.i();
            for (int i = 0; i < size; i++) {
                // do i even
                RecipeRequest request = Pools.obtain(RecipeRequest.class, RecipeRequest::new).set(read.i(), read.i());
                pending.addLast(request);
            }
        }
    }

    public static class RecipeRequest implements Poolable {
        public int index;
        public int stack;

        public RecipeRequest set(int index, int stack) {
            this.index = index;
            this.stack = stack;
            return this;
        }

        @Override
        public void reset() {
            index = -1;
            stack = 0;
        }
    }
}
