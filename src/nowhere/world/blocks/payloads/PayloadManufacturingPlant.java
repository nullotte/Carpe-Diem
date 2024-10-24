package nowhere.world.blocks.payloads;

import arc.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import nowhere.world.blocks.payloads.PayloadManufacturingComponent.*;

public class PayloadManufacturingPlant extends PayloadBlock {
    public Seq<PayloadManufacturingRecipe> recipes = new Seq<>();

    public PayloadManufacturingPlant(String name) {
        super(name);
        rotate = true;
        outputsPayload = true;
    }

    public class ManufacturingPlantBuild extends PayloadBlockBuild<Payload> {
        public Seq<ManufacturingComponentBuild> chained = new Seq<>();
        public Seq<PayloadManufacturingRecipe> possibleRecipes = Seq.with(recipes);
        public boolean failedManufacturing;

        public void updateChained() {
            chained.clear();
            checkChain(back(), this);
        }

        public void checkChain(Building build, Building checker) {
            if (build instanceof ManufacturingComponentBuild other && build.front() == checker && !chained.contains(other)) {
                chained.add(other);

                for (Building b : other.proximity) {
                    checkChain(b, other);
                }
            }
        }

        @Override
        public void updateTile() {
            super.updateTile();
            moveOutPayload();

            // crafting logic, absolutely terrible one at that. TODO buggy as shit
            boolean full = !chained.isEmpty();
            Tmp.p1.set(tileX(), tileY());
            Tmp.p2.set(tileX(), tileY());

            for (ManufacturingComponentBuild b : chained) {
                if (!b.filled()) {
                    full = false;
                    break;
                }

                if (b.tileX() <= Tmp.p1.x && b.tileY() >= Tmp.p1.y) Tmp.p1.set(b.tileX(), b.tileY());
                if (b.tileX() >= Tmp.p2.x && b.tileY() <= Tmp.p2.y) Tmp.p2.set(b.tileX(), b.tileY());
            }

            if (full) {
                // dont attempt to assemble the exact same set of blocks repeatedly
                if (!failedManufacturing) {
                    // iterate through the whole assembly chain, hopefully
                    for (int i = Tmp.p2.y; i >= Tmp.p1.y; i -= size) {
                        for (int j = Tmp.p1.x; j <= Tmp.p2.x; j += size) {
                            int sx = (j - Tmp.p1.x) / size;
                            int sy = (i - Tmp.p2.y) / size;
                            Building b = Vars.world.build(j, i);

                            // found one! match it with the recipes
                            if (b instanceof ManufacturingComponentBuild component && chained.contains(component)) {
                                possibleRecipes.each(recipe -> {
                                    char key = recipe.pattern[sy].charAt(sx);

                                    if (recipe.keys.get(key) != component.payload.content()) possibleRecipes.remove(recipe);
                                });
                            }
                        }
                    }

                    // if there's any valid recipe, assemble it
                    if (!possibleRecipes.isEmpty()) {
                        PayloadManufacturingRecipe recipe = possibleRecipes.first();
                        chained.each(component -> component.payload = null);

                        Unit unit = recipe.result.create(team);
                        payload = new UnitPayload(unit);
                        payVector.setZero();
                        Events.fire(new UnitCreateEvent(unit, this));
                    } else {
                        failedManufacturing = true;
                    }
                }
            } else {
                // reset state
                failedManufacturing = false;
                possibleRecipes.set(recipes);
            }
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);
            Draw.rect(inRegion, x, y, rotdeg());
            Draw.rect(outRegion, x, y, rotdeg());

            drawPayload();

            Draw.z(Layer.blockOver + 0.1f);
            Draw.rect(topRegion, x, y);
        }
    }

    // if you're looking at this code and you think "hmm that could be done in a better way" PLEASE tell me the better way im literally stupid rn
    public static class PayloadManufacturingRecipe {
        public String[] pattern;
        public ObjectMap<Character, UnlockableContent> keys;
        public UnitType result;

        public PayloadManufacturingRecipe(String[] pattern, ObjectMap<Character, UnlockableContent> keys, UnitType result) {
            this.pattern = pattern;
            this.keys = keys;
            this.result = result;
        }
    }
}
