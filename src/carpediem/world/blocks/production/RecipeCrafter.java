package carpediem.world.blocks.production;

import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;

// im so fucking tired
// TODO maybe add fluid support? custom craft times for each recipe?
public class RecipeCrafter extends GenericCrafter {
    public Seq<CraftingRecipe> recipes;

    protected ObjectMap<Seq<Item>, CraftingRecipe> recipeMap;

    public RecipeCrafter(String name) {
        super(name);

        consume(new ConsumeItemDynamic((RecipeCrafterBuild crafter) -> {
            // ok so how it works is that it *accepts* all items, but only *consumes* items if the inventory match recipe
            CraftingRecipe recipe = crafter.getCurrentRecipe();
            return recipe != null ? recipe.inputItems : ItemStack.empty;
        }));
    }

    @Override
    public void init() {
        super.init();

        recipeMap = new ObjectMap<>();
        recipes.each(recipe -> {
            Seq<Item> key = new Seq<>();

            for (ItemStack i : recipe.inputItems) {
                key.add(i.item);
            }

            key.sort();
            recipeMap.put(key, recipe);
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
                    if (!Structs.contains(recipe.inputItems, i -> i.item == item)) {
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
            for (CraftingRecipe recipe : recipes) {
                for (ItemStack stack : recipe.inputItems) {
                    if (stack.item == item) return super.getMaximumAccepted(item);
                }
            }

            return 0;
        }

        public CraftingRecipe getCurrentRecipe() {
            // dont switch recipes until the crafter is fully emptied
            if (items.empty()) return current = null;

            if (current != null) return current;

            key.clear();
            items.each((item, count) -> key.add(item));

            return current = recipeMap.get(key);
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
