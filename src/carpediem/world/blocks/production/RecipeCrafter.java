package carpediem.world.blocks.production;

import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.world.consumers.ConsumeItemsUses.*;
import carpediem.world.meta.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;

import java.util.*;

// TODO this class is so jank it probably needs a complete rewrite. but it works for now
public class RecipeCrafter extends GenericCrafter {
    public Seq<CraftingRecipe> recipes;

    protected ObjectMap<Seq<UnlockableContent>, CraftingRecipe> recipeMap;
    protected Seq<Item> inputItems;
    protected Seq<Liquid> inputLiquids;

    public boolean outputsItems;

    public RecipeCrafter(String name) {
        super(name);
    }

    @Override
    public void init() {
        recipeMap = new ObjectMap<>();
        inputItems = new Seq<>();
        inputLiquids = new Seq<>();

        for (CraftingRecipe recipe : recipes) {
            Seq<UnlockableContent> key = new Seq<>();

            if (recipe.inputItems != null) {
                hasItems = true;

                for (ItemStack i : recipe.inputItems) {
                    key.add(i.item);
                    inputItems.addUnique(i.item);
                }
            }
            if (recipe.outputItems != null) {
                hasItems = outputsItems = true;
            }

            if (recipe.inputLiquids != null) {
                hasLiquids = true;

                for (LiquidStack l : recipe.inputLiquids) {
                    key.add(l.liquid);
                    inputLiquids.addUnique(l.liquid);
                }
            }
            if (recipe.outputLiquids != null) hasLiquids = true;

            key.sort(Comparator.comparingInt((UnlockableContent c) -> c.getContentType().ordinal()).thenComparingInt(c -> c.id));
            recipeMap.put(key, recipe);
        }

        if (!inputItems.isEmpty()) {
            consume(new ConsumeItemDynamic((RecipeCrafterBuild crafter) -> {
                CraftingRecipe recipe = crafter.getCurrentRecipe();
                return recipe != null && recipe.inputItems != null ? recipe.inputItems : ItemStack.empty;
            }));
        }

        if (!inputLiquids.isEmpty()) {
            consume(new ConsumeLiquidsDynamic((RecipeCrafterBuild crafter) -> {
                CraftingRecipe recipe = crafter.getCurrentRecipe();
                return recipe != null && recipe.inputLiquids != null ? recipe.inputLiquids : LiquidStack.empty;
            }));
        }

        super.init();
    }

    @Override
    public void setStats() {
        super.setStats();

        // TODO this is not ideal.
        stats.add(CDStat.recipes, table -> {
            table.row();

            for (CraftingRecipe recipe : recipes) {
                table.table(Styles.grayPanel, t -> {
                    t.table(input -> {
                        input.left();

                        if (recipe.inputItems != null) {
                            for (ItemStack stack : recipe.inputItems) {
                                input.add(new ItemDisplay(stack.item, stack.amount, craftTime, true)).pad(5f);
                            }
                        }

                        if (recipe.inputLiquids != null) {
                            for (LiquidStack stack : recipe.inputLiquids) {
                                input.add(new LiquidDisplay(stack.liquid, stack.amount * 60f, true)).pad(5f);
                            }
                        }
                    }).left().grow().pad(10f);

                    t.table(arrow -> {
                        arrow.image(Icon.right).color(Pal.darkishGray).size(40f);
                    }).pad(10f);

                    t.table(output -> {
                        output.right();

                        if (recipe.outputItems != null) {
                            for (ItemStack stack : recipe.outputItems) {
                                output.add(new ItemDisplay(stack.item, stack.amount, craftTime, true)).pad(5f);
                            }
                        }

                        if (recipe.outputLiquids != null) {
                            for (LiquidStack stack : recipe.outputLiquids) {
                                output.add(new LiquidDisplay(stack.liquid, stack.amount * 60f, true)).pad(5f);
                            }
                        }
                    }).right().grow().pad(10f);
                }).growX().pad(5f);

                table.row();
            }
        });
    }

    @Override
    public boolean outputsItems() {
        return outputsItems;
    }

    public class RecipeCrafterBuild extends GenericCrafterBuild implements UseCounter {
        public CraftingRecipe current;
        public Seq<UnlockableContent> key = new Seq<>();

        public int uses;

        @Override
        public void updateTile() {
            super.updateTile();

            if (efficiency > 0f) {
                CraftingRecipe recipe = getCurrentRecipe();

                if (recipe != null && recipe.outputLiquids != null) {
                    float inc = getProgressIncrease(1f);
                    for (LiquidStack output : recipe.outputLiquids) {
                        handleLiquid(this, output.liquid, Math.min(output.amount * inc, liquidCapacity - liquids.get(output.liquid)));
                    }
                }
            }
        }

        @Override
        public float getProgressIncrease(float baseTime) {
            // it just works
            CraftingRecipe recipe = getCurrentRecipe();

            float scaling = 1f, max = 1f;
            if (recipe != null && recipe.outputLiquids != null) {
                max = 0f;
                for (LiquidStack s : recipe.outputLiquids) {
                    float value = (liquidCapacity - liquids.get(s.liquid)) / (s.amount * edelta());
                    scaling = Math.min(scaling, value);
                    max = Math.max(max, value);
                }
            }

            return super.getProgressIncrease(baseTime) * (dumpExtraLiquid ? Math.min(max, 1f) : scaling);
        }

        @Override
        public boolean shouldConsume() {
            CraftingRecipe recipe = getCurrentRecipe();

            if (recipe == null) {
                return false;
            } else {
                if (recipe.outputItems != null) {
                    for (ItemStack output : recipe.outputItems) {
                        if (items.get(output.item) + output.amount > itemCapacity) {
                            return false;
                        }
                    }
                }

                // sighh
                if (recipe.outputLiquids != null) {
                    boolean allFull = true;
                    for (LiquidStack output : recipe.outputLiquids) {
                        if (liquids.get(output.liquid) >= liquidCapacity - 0.001f) {
                            if (!dumpExtraLiquid) {
                                return false;
                            }
                        } else {
                            allFull = false;
                        }
                    }

                    if (allFull) {
                        return false;
                    }
                }
            }

            return enabled;
        }

        @Override
        public void craft() {
            CraftingRecipe recipe = getCurrentRecipe();
            consume();

            if (recipe != null && recipe.outputItems != null) {
                for (ItemStack output : recipe.outputItems) {
                    for (int i = 0; i < output.amount; i++) {
                        offload(output.item);
                    }
                }
            }

            if (wasVisible) {
                craftEffect.at(x, y);
            }
            progress %= 1f;
        }

        @Override
        public void dumpOutputs() {
            CraftingRecipe recipe = getCurrentRecipe();

            if (recipe != null) {
                if (items != null && timer(timerDump, dumpTime / timeScale)) {
                    for (Item item : Vars.content.items()) {
                        if ((recipe.inputItems == null || !Structs.contains(recipe.inputItems, i -> i.item == item)) && !consumesItem(item)) {
                            dump(item);
                        }
                    }
                }

                if (liquids != null) {
                    for (Liquid liquid : Vars.content.liquids()) {
                        if ((recipe.inputLiquids == null || !Structs.contains(recipe.inputLiquids, i -> i.liquid == liquid)) && !consumesLiquid(liquid)) {
                            dumpLiquid(liquid, 2f);
                        }
                    }
                }
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public int getMaximumAccepted(Item item) {
            // i have trapped myself within a hell of my own making
            if (consumesItem(item)) {
                return super.getMaximumAccepted(item);
            }

            CraftingRecipe recipe = getCurrentRecipe();

            if (recipe != null) {
                if (recipe.inputItems != null) {
                    for (ItemStack stack : recipe.inputItems) {
                        if (stack.item == item) return super.getMaximumAccepted(item);
                    }
                } else {
                    return 0;
                }
                // "else for"????????
            } else for (Item inputItem : inputItems) {
                if (item == inputItem) return super.getMaximumAccepted(item);
            }

            return 0;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            // nobody to blame but myself
            if (consumesLiquid(liquid)) {
                return true;
            }

            CraftingRecipe recipe = getCurrentRecipe();

            if (recipe != null) {
                if (recipe.inputLiquids != null) {
                    for (LiquidStack stack : recipe.inputLiquids) {
                        if (stack.liquid == liquid) return true;
                    }
                } else {
                    return false;
                }
            } else for (Liquid inputLiquid : inputLiquids) {
                if (liquid == inputLiquid) return true;
            }

            return false;
        }

        public CraftingRecipe getCurrentRecipe() {
            // dont switch recipes until the crafter is fully emptied
            boolean liquidsEmpty = true;

            for (Liquid liquid : Vars.content.liquids()) {
                if (liquids != null && liquids.get(liquid) > 0.1f) liquidsEmpty = false;
            }

            if (items.sum((item, count) -> consumesItem(item) ? 0f : 1f) == 0f && liquidsEmpty) {
                // reset
                progress = 0f;
                return current = null;
            };

            if (current != null) return current;

            key.clear();

            if (items != null) {
                items.each((item, count) -> {
                    if (inputItems.contains(item)) key.add(item);
                });
            }
            if (liquids != null) {
                liquids.each((liquid, count) -> {
                    if (count > 0.1f && inputLiquids.contains(liquid)) key.add(liquid);
                });
            }

            return current = recipeMap.get(key);
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            int id = current != null ? recipes.indexOf(current) : -1;
            write.i(id);
            write.i(uses);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            int id = read.i();
            if (id >= 0) current = recipes.get(id);
            uses = read.i();
        }

        @Override
        public int getUses() {
            return uses;
        }

        @Override
        public void addUses(int uses) {
            this.uses += uses;
        }

        @Override
        public void removeUses(int uses) {
            this.uses -= uses;
        }
    }

    public static class CraftingRecipe {
        public ItemStack[] inputItems;
        public ItemStack[] outputItems;

        public LiquidStack[] inputLiquids;
        public LiquidStack[] outputLiquids;

        public CraftingRecipe(ItemStack inputItem, ItemStack outputItem) {
            this(new ItemStack[]{inputItem}, new ItemStack[]{outputItem});
        }

        public CraftingRecipe(ItemStack[] inputItems, ItemStack[] outputItems) {
            this.inputItems = inputItems;
            this.outputItems = outputItems;
        }

        // the k stands for kill me right now
        public CraftingRecipe() {
        }
    }
}
