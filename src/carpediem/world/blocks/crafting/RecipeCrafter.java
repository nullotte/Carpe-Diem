package carpediem.world.blocks.crafting;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.world.consumers.ConsumeItemsUses.*;
import carpediem.world.meta.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

// this is so stupid
public class RecipeCrafter extends Block {
    public Seq<Recipe> recipes = new Seq<>();

    public Effect craftEffect = Fx.none;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public float warmupSpeed = 0.019f;

    public float craftingSpeed = 1f;

    public DrawBlock drawer = new DrawDefault();

    public RecipeCrafter(String name) {
        super(name);
        update = true;
        solid = true;
        hasItems = true;
        ambientSound = Sounds.machine;
        sync = true;
        ambientSoundVolume = 0.03f;
        flags = EnumSet.of(BlockFlag.factory);
    }

    @Override
    public void init() {
        for (Recipe recipe : recipes) {
            recipe.apply(this);
        }

        consume(new Consume() {
            @Override
            public void apply(Block block) {
                boolean[] prevItemFilter = block.itemFilter;
                boolean[] prevLiquidFilter = block.liquidFilter;

                for (Recipe recipe : recipes) {
                    block.itemFilter = new boolean[Vars.content.items().size];
                    block.liquidFilter = new boolean[Vars.content.liquids().size];

                    for (Consume consume : recipe.consumes) {
                        consume.apply(block);
                    }

                    recipe.itemFilter = block.itemFilter;
                    recipe.liquidFilter = block.liquidFilter;
                }

                block.itemFilter = prevItemFilter;
                block.liquidFilter = prevLiquidFilter;
            }

            @Override
            public void trigger(Building build) {
                if (build instanceof RecipeCrafterBuild crafter && crafter.getCurrentRecipe() != null) {
                    for (Consume consume : crafter.getCurrentRecipe().consumes) {
                        consume.trigger(crafter);
                    }
                }
            }

            @Override
            public float efficiency(Building build) {
                if (build instanceof RecipeCrafterBuild crafter && crafter.getCurrentRecipe() != null) {
                    float minEfficiency = 1f;

                    for (Consume consume : crafter.getCurrentRecipe().consumes) {
                        minEfficiency = Math.min(minEfficiency, consume.efficiency(crafter));
                    }

                    return minEfficiency;
                } else {
                    return 0f;
                }
            }

            @Override
            public void build(Building build, Table table) {
                Recipe[] current = {null};

                table.table(cont -> {
                    table.update(() -> {
                        if (build instanceof RecipeCrafterBuild crafter) {
                            Recipe recipe = crafter.getCurrentRecipe();
                            if (current[0] != recipe) {
                                current[0] = recipe;
                                rebuild(build, cont, current[0]);
                            }
                        }
                    });

                    rebuild(build, cont, current[0]);
                });
            }

            public void rebuild(Building build, Table table, Recipe recipe) {
                table.clear();

                if (recipe != null) {
                    for (Consume consume : recipe.consumes) {
                        consume.build(build, table);
                    }
                }
            }
        });

        super.init();
    }

    @Override
    public void load() {
        super.load();

        drawer.load(this);
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

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out) {
        drawer.getRegionsToOutline(this, out);
    }

    @Override
    public TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    public class RecipeCrafterBuild extends Building implements UseCounter {
        public int currentRecipeID = -1;
        public float progress;
        public float totalProgress;
        public float warmup;

        public int uses;

        @Override
        public void updateTile() {
            Recipe currentRecipe = getCurrentRecipe();

            if (currentRecipe != null) {
                if (efficiency > 0) {
                    progress += getProgressIncrease(currentRecipe.craftTime * craftingSpeed);
                    warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);

                    // for liquids
                    currentRecipe.update(this);

                    if (wasVisible && Mathf.chanceDelta(updateEffectChance)) {
                        updateEffect.at(x + Mathf.range(block.size * 4f), y + Mathf.range(block.size * 4));
                    }
                } else {
                    warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
                }
                // uhh yeah whatever anuke said. idk i just copied his code
                totalProgress += warmup * Time.delta;

                if (progress >= 1f) {
                    craft();
                }

                dumpOutputs();
            }
        }

        public void craft() {
            Recipe recipe = getCurrentRecipe();
            consume();

            if (recipe != null) {
                recipe.craft(this);
            }

            if (wasVisible) {
                craftEffect.at(x, y);
            }

            progress %= 1f;
        }

        public void dumpOutputs() {
            Recipe currentRecipe = getCurrentRecipe();
            if (currentRecipe != null) {
                currentRecipe.dumpOutputs(this);

                if (timer(timerDump, dumpTime / timeScale)) {
                    currentRecipe.dumpTimedOutputs(this);
                }
            }
        }

        public Recipe getCurrentRecipe() {
            // auto-detect recipe
            if (!configurable) {
                matchRecipe();
            }

            if (currentRecipeID < 0 || currentRecipeID >= recipes.size) {
                return null;
            }

            return recipes.get(currentRecipeID);
        }

        public void matchRecipe() {
            // dont switch recipes until the crafter is fully emptied
            boolean liquidsEmpty = true;

            for (Liquid liquid : Vars.content.liquids()) {
                if (liquids != null && liquids.get(liquid) > 0.1f) liquidsEmpty = false;
            }

            if (items.sum((item, count) -> consumesItem(item) ? 0f : 1f) == 0f && liquidsEmpty) {
                // reset
                progress = 0f;
                currentRecipeID = -1;
                return;
            }

            if (currentRecipeID >= 0) return;

            for (Recipe recipe : recipes) {
                if (recipe.valid(this)) {
                    currentRecipeID = recipes.indexOf(recipe);
                    return;
                }
            }
        }

        @Override
        public boolean shouldConsume() {
            Recipe currentRecipe = getCurrentRecipe();
            return currentRecipe != null && currentRecipe.shouldConsume(this);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            if (!hasItems) return false;

            Recipe currentRecipe = getCurrentRecipe();
            boolean recipeConsumes = false;

            if (currentRecipe == null) {
                for (Recipe recipe : recipes) {
                    if (recipe.consumesItem(item)) {
                        recipeConsumes = true;
                        break;
                    }
                }
            } else {
                recipeConsumes = currentRecipe.consumesItem(item);
            }

            return (consumesItem(item) || recipeConsumes) && items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            if (!hasLiquids) return false;

            Recipe currentRecipe = getCurrentRecipe();
            boolean recipeConsumes = false;

            if (currentRecipe == null) {
                for (Recipe recipe : recipes) {
                    if (recipe.consumesLiquid(liquid)) {
                        recipeConsumes = true;
                        break;
                    }
                }
            } else {
                recipeConsumes = currentRecipe.consumesLiquid(liquid);
            }

            return (consumesLiquid(liquid) || recipeConsumes);
        }

        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public void drawLight() {
            drawer.drawLight(this);
        }

        @Override
        public int getUses() {
            return uses;
        }

        @Override
        public void setUses(int uses) {
            this.uses = uses;
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.i(currentRecipeID);
            write.f(progress);
            write.f(warmup);

            write.i(uses);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            currentRecipeID = read.i();
            progress = read.f();
            warmup = read.f();

            uses = read.i();
        }
    }
}
