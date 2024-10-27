package carpediem.world.blocks.production;

import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.world.meta.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;

// im so fucking tired
// TODO maybe add fluid support? custom craft times for each recipe?
public class RecipeCrafter extends GenericCrafter {
    public Seq<CraftingRecipe> recipes;

    protected ObjectMap<Seq<Item>, CraftingRecipe> recipeMap;
    protected Seq<Item> inputItems;

    public RecipeCrafter(String name) {
        super(name);

        consume(new ConsumeItemDynamic((RecipeCrafterBuild crafter) -> {
            CraftingRecipe recipe = crafter.getCurrentRecipe();
            return recipe != null ? recipe.inputItems : ItemStack.empty;
        }));
    }

    @Override
    public void init() {
        super.init();

        recipeMap = new ObjectMap<>();
        inputItems = new Seq<>();

        for (CraftingRecipe recipe : recipes) {
            Seq<Item> key = new Seq<>();

            for (ItemStack i : recipe.inputItems) {
                key.add(i.item);
                inputItems.addUnique(i.item);
            }

            key.sort();
            recipeMap.put(key, recipe);
        }
    }

    @Override
    public void setStats() {
        super.setStats();

        // TODO the arrow isnt in the middle but i dont wanna touch arc UI ever again
        stats.add(CDStat.recipes, table -> {
            table.row();

            for (CraftingRecipe recipe : recipes) {
                table.table(Styles.grayPanel, t -> {
                    t.table(input -> {
                        input.left();
                        for (ItemStack stack : recipe.inputItems) {
                            input.add(new ItemDisplay(stack.item, stack.amount, craftTime, true)).pad(5);
                        }
                    }).left().grow().pad(10f);

                    t.table(arrow -> {
                        arrow.image(Icon.right).color(Pal.darkishGray).size(40f);
                    }).pad(10f);

                    t.table(output -> {
                        output.right();
                        for (ItemStack stack : recipe.outputItems) {
                            output.add(new ItemDisplay(stack.item, stack.amount, craftTime, true)).pad(5);
                        }
                    }).right().grow().pad(10f);
                }).growX().pad(5f);

                table.row();
            }
        });
    }

    public class RecipeCrafterBuild extends GenericCrafterBuild {
        public CraftingRecipe current;
        public Seq<Item> key = new Seq<>();

        @Override
        public boolean shouldConsume() {
            CraftingRecipe recipe = getCurrentRecipe();

            if (recipe == null) {
                return false;
            } else {
                for (ItemStack output : recipe.outputItems) {
                    if (items.get(output.item) + output.amount > itemCapacity) {
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

            if (recipe != null) {
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

            if (recipe != null && timer(timerDump, dumpTime / timeScale)) {
                for (Item item : Vars.content.items()) {
                    if (!Structs.contains(recipe.inputItems, i -> i.item == item) && !consumesItem(item)) {
                        dump(item);
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
                for (ItemStack stack : recipe.inputItems) {
                    if (stack.item == item) return super.getMaximumAccepted(item);
                }
                // "else for"????????
            } else for (Item inputItem : inputItems) {
                if (item == inputItem) return super.getMaximumAccepted(item);
            }

            return 0;
        }

        public CraftingRecipe getCurrentRecipe() {
            // dont switch recipes until the crafter is fully emptied
            if (items.empty()) return current = null;

            if (current != null) return current;

            key.clear();
            items.each((item, count) -> {
                if (inputItems.contains(item)) key.add(item);
            });

            return current = recipeMap.get(key);
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.i(recipes.indexOf(current));
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            current = recipes.get(read.i());
        }
    }

    // i could be having fun right now
    public static class CraftingRecipe {
        public ItemStack[] inputItems;
        public ItemStack[] outputItems;

        public CraftingRecipe(ItemStack inputItem, ItemStack outputItem) {
            this(new ItemStack[]{inputItem}, new ItemStack[]{outputItem});
        }

        public CraftingRecipe(ItemStack[] inputItems, ItemStack[] outputItems) {
            this.inputItems = inputItems;
            this.outputItems = outputItems;
        }
    }
}
