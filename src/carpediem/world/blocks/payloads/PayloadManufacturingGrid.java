package carpediem.world.blocks.payloads;

import arc.*;
import arc.audio.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.struct.IntIntMap.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.graphics.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;

public class PayloadManufacturingGrid extends PayloadBlock {
    public static final Queue<ManufacturingGridBuild> gridQueue = new Queue<>();

    public static long lastTime = 0;
    public static int pitchSeq = 0;

    public Seq<PayloadManufacturingRecipe> recipes = new Seq<>();
    public float craftTime = 60f;
    public float ingredientRadius = 4f * Vars.tilesize;
    public Interp mergeInterp = Interp.pow2In, sizeInterp = a -> 1f - Interp.pow2In.apply(a);
    public Effect mergeEffect = Fx.producesmoke, loadEffect = Fx.producesmoke,
            craftEffect = CDFx.payloadManufacture, failEffect = CDFx.payloadManufactureFail;
    public Sound craftSound = Sounds.place;

    public TextureRegion stackRegion, stackBottomRegion1, stackBottomRegion2;

    public PayloadManufacturingGrid(String name) {
        super(name);
        rotate = true;
        acceptsPayload = true;
        outputsPayload = true;

        ambientSound = Sounds.conveyor;
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(region, plan.drawx(), plan.drawy());
        Draw.rect(outRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
    }


    @Override
    public void load() {
        super.load();

        stackRegion = Core.atlas.find(name + "-stack");
        stackBottomRegion1 = Core.atlas.find(name + "-stack-bottom1");
        stackBottomRegion2 = Core.atlas.find(name + "-stack-bottom2");
    }

    public static float calcPitch() {
        if (Time.timeSinceMillis(lastTime) < 2000) {
            lastTime = Time.millis();
            pitchSeq++;
            if (pitchSeq > 30) {
                pitchSeq = 0;
            }
            return 1f + Mathf.clamp(pitchSeq / 30f) * 1.9f;
        } else {
            pitchSeq = 0;
            lastTime = Time.millis();
            return Mathf.random(0.7f, 1.3f);
        }
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, outRegion};
    }

    public class ManufacturingGridBuild extends PayloadBlockBuild<Payload> {
        public boolean crafting, failed, moveOut, merge, dirty;
        public float progress;
        public IntIntMap ingredients = new IntIntMap();
        public Point2 offset = new Point2();
        public Vec2 center = new Vec2();
        // for checking whether or not to begin crafting
        public Seq<ManufacturingGridBuild> chained = new Seq<>();

        @Override
        public void updateTile() {
            super.updateTile();

            if (dirty) {
                Tmp.p1.set(0, 0);
                Tmp.p2.set(0, 0);
                for (Entry entry : ingredients) {
                    int ix = Point2.x(entry.key), iy = Point2.y(entry.key);

                    Tmp.p1.set(Math.min(ix, Tmp.p1.x), Math.min(iy, Tmp.p1.y));
                    Tmp.p2.set(Math.max(ix, Tmp.p2.x), Math.max(iy, Tmp.p2.y));
                }
                offset.set(0, 0).sub(Tmp.p1);
                Tmp.p2.add(offset);
                center.set(Tmp.p2.x / 2f, Tmp.p2.y / 2f).scl(ingredientRadius);

                dirty = false;
            }

            if (payload != null) {
                if (moveOut) {
                    moveOutPayload();
                } else {
                    moveInPayload(crafting);

                    if (crafting) {
                        // wait for inputs to finish first. TODO i dont know how good this code is
                        boolean canManufacture = true;
                        int trns = block.size / 2 + 1;

                        for (int i = 0; i < 4; i++) {
                            if (i == rotation) continue;

                            Building in = nearby(Geometry.d4x(i) * trns, Geometry.d4y(i) * trns);
                            if (in instanceof ManufacturingGridBuild grid && acceptGrid(grid, true)) {
                                if (grid.crafting && !grid.ingredients.isEmpty()) {
                                    canManufacture = false;
                                    break;
                                }
                            }
                        }

                        if (canManufacture && !ingredients.isEmpty()) {
                            progress += getProgressIncrease(craftTime);

                            if (progress >= 1f) {
                                progress = 0f;

                                // merge ingredients
                                if (front() instanceof ManufacturingGridBuild next && merge) {
                                    Point2 add = Geometry.d4(next.relativeTo(this));
                                    for (Entry entry : ingredients) {
                                        int ix = Point2.x(entry.key) + add.x, iy = Point2.y(entry.key) + add.y;
                                        next.ingredients.put(Point2.pack(ix, iy), entry.value);
                                    }
                                    next.dirty = true;

                                    mergeEffect.at(Tmp.v1.trns(rotdeg(), block.size * Vars.tilesize - (ingredientRadius / 2f)).add(this));
                                    craftSound.at(this, calcPitch());
                                } else {
                                    // match recipes
                                    Seq<PayloadManufacturingRecipe> possibleRecipes = recipes.select(r -> {
                                        if (!r.result.unlockedNow()) return false;

                                        if (r.result instanceof Block block && Vars.state.rules.isBanned(block)) {
                                            return false;
                                        }
                                        if (r.result instanceof UnitType unit && Vars.state.rules.isBanned(unit)) {
                                            return false;
                                        }

                                        if (r.shapelessRequirements != null) {
                                            int amount = 0;
                                            for (PayloadStack stack : r.shapelessRequirements) {
                                                amount += stack.amount;
                                            }
                                            return amount == ingredients.size;
                                        }

                                        return r.requirements.size == ingredients.size;
                                    });

                                    Seq<PayloadStack> accumulated = new Seq<>();

                                    for (Entry entry : ingredients) {
                                        Building build = Vars.world.build(entry.value);

                                        if (build instanceof ManufacturingGridBuild grid && grid.payload != null) {
                                            // accumulator thingy for shapeless recipes
                                            PayloadStack stack = accumulated.find(s -> s.item == grid.payload.content());
                                            if (stack != null) {
                                                stack.amount++;
                                            } else {
                                                accumulated.add(new PayloadStack(grid.payload.content(), 1));
                                            }

                                            possibleRecipes.each(recipe -> {
                                                if (recipe.shapelessRequirements != null) return;
                                                UnlockableContent content = grid.payload.content();
                                                int position = Point2.pack(Point2.x(entry.key) + offset.x, Point2.y(entry.key) + offset.y);

                                                if (recipe.requirements.get(position) != content) {
                                                    possibleRecipes.remove(recipe);
                                                }
                                            });
                                        } else {
                                            // how the FUCK
                                            possibleRecipes.clear();
                                            break;
                                        }
                                    }

                                    // filter out the shapeless recipes
                                    for (PayloadManufacturingRecipe recipe : possibleRecipes) {
                                        if (recipe.shapelessRequirements != null && recipe.shapelessRequirements.length == accumulated.size) {
                                            for (PayloadStack stack : recipe.shapelessRequirements) {
                                                boolean found = false;
                                                // this is so ass?
                                                for (PayloadStack aStack : accumulated) {
                                                    if (stack.item == aStack.item && stack.amount == aStack.amount) {
                                                        found = true;
                                                        break;
                                                    }
                                                }
                                                if (found) continue;

                                                possibleRecipes.remove(recipe);
                                                break;
                                            }
                                        }
                                    }

                                    // successful recipe
                                    if (!possibleRecipes.isEmpty()) {
                                        PayloadManufacturingRecipe recipe = possibleRecipes.first();

                                        // clear all
                                        chained.each(b -> b.crafting, b -> {
                                            b.payload = null;
                                            b.crafting = false;
                                        });

                                        // create payload
                                        if (recipe.result instanceof UnitType unitResult) {
                                            Unit unit = unitResult.create(team);
                                            payload = new UnitPayload(unit);
                                            Events.fire(new UnitCreateEvent(unit, this));
                                        } else if (recipe.result instanceof Block blockResult) {
                                            payload = new BuildPayload(blockResult, team);
                                        }

                                        moveOut = true;
                                        craftEffect.at(x, y, payRotation - 90f, payload);
                                    } else {
                                        // failed
                                        failed = true;
                                        chained.each(b -> {
                                            b.crafting = false;
                                            if (b.payload != null) {
                                                failEffect.at(b.x, b.y, b.payRotation - 90f, b.payload);
                                            }
                                        });
                                    }
                                }

                                ingredients.clear();
                                dirty = true;
                            }
                        }
                    } else {
                        payRotation = Angles.moveToward(payRotation, 90f, payloadRotateSpeed * delta());

                        // start crafting once all grid slots are filled
                        if (!chained.contains(b -> !b.canCraft())) {
                            chained.each(ManufacturingGridBuild::initCrafting);

                            // terrible
                            chained.each(grid -> {
                                grid.merge = grid.front() instanceof ManufacturingGridBuild next && next.acceptGrid(grid, true);
                            });
                        }
                    }
                }
            } else {
                moveOut = false;
            }
        }

        public void initCrafting() {
            crafting = true;
            ingredients.put(0, pos());
            dirty = true;
        }

        public boolean canCraft() {
            return !failed && payload != null && hasArrived();
        }

        public boolean acceptGrid(ManufacturingGridBuild other, boolean checkCrafting) {
            return (crafting || !checkCrafting) && other.block == this.block && other.team == this.team && (other.x == x || other.y == y) && other.relativeTo(this) == other.rotation;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload) {
            return this.payload == null && !moveOut;
        }

        @Override
        public void handlePayload(Building source, Payload payload) {
            super.handlePayload(source, payload);
            loadEffect.at(this);
        }

        public void drawIngredient(float x, float y, Payload payload, boolean bottom) {
            float z = Draw.z();

            if (bottom) {
                Draw.z(z - 0.02f);
                Draw.rect(rotation == 0 || rotation == 3 ? stackBottomRegion1 : stackBottomRegion2, x, y, rotdeg());
            }

            Draw.z(z - 0.03f);
            Drawf.shadow(x, y, ingredientRadius * 2f);
            Draw.z(z - 0.01f);
            Draw.rect(stackRegion, x, y);
            if (payload != null) {
                Draw.z(z - 0.001f);
                Drawf.shadow(x, y, payload.size() * 2f);
                Draw.z(z);
                Draw.rect(payload.icon(), x, y);
            }

            Draw.z(z);
        }

        public void drawIngredients(Vec2 offset, float scl) {
            Draw.scl(scl);
            offset.scl(scl);
            float z = Draw.z();

            Draw.z(z);
            for (Entry entry : ingredients) {
                Building build = Vars.world.build(entry.value);

                if (build instanceof ManufacturingGridBuild grid && grid.payload != null) {
                    float dx = x + offset.x + (Point2.x(entry.key) * ingredientRadius * scl),
                            dy = y + offset.y + (Point2.y(entry.key) * ingredientRadius * scl);

                    drawIngredient(dx, dy, grid.payload, entry.key == 0 && merge);
                }
            }
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);
            for (int i = 0; i < 4; i++) {
                if (blends(i) && i != rotation) {
                    Draw.rect(inRegion, x, y, (i * 90f) - 180f);
                }
            }
            Draw.rect(outRegion, x, y, rotdeg());

            Draw.z(Layer.blockOver);
            if (crafting) {
                if (merge) {
                    Tmp.v1.trns(rotdeg(), mergeInterp.apply(progress) * (block.size * Vars.tilesize - ingredientRadius));
                    drawIngredients(Tmp.v1, 1f);
                } else {
                    Tmp.v1.setZero().lerp(
                            offset.x * ingredientRadius - center.x,
                            offset.y * ingredientRadius - center.y,
                            progress
                    );
                    drawIngredients(Tmp.v1, sizeInterp.apply(progress));
                }
            } else if (payload != null) {
                if (!moveOut) {
                    drawIngredient(x, y, null, true);
                }

                payload.draw();
            }
        }

        @Override
        public boolean shouldAmbientSound() {
            return crafting && efficiency > 0f;
        }

        @Override
        public float ambientVolume() {
            return super.ambientVolume();
        }

        @Override
        public void onProximityAdded() {
            super.onProximityAdded();
            updateChained();
        }

        @Override
        public void onProximityRemoved() {
            super.onProximityRemoved();

            for (Building build : proximity) {
                if (build instanceof ManufacturingGridBuild grid) {
                    grid.updateChained();
                }
            }
        }

        public void updateChained() {
            chained = new Seq<>();
            gridQueue.clear();
            gridQueue.add(this);

            while (!gridQueue.isEmpty()) {
                ManufacturingGridBuild next = gridQueue.removeLast();
                next.failed = false;
                chained.add(next);

                for (Building build : next.proximity) {
                    if (build instanceof ManufacturingGridBuild grid && (next.acceptGrid(grid, false) || grid.acceptGrid(next, false)) && grid.chained != chained) {
                        grid.chained = chained;
                        gridQueue.addFirst(grid);
                    }
                }
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.bool(crafting);
            write.bool(moveOut);
            write.bool(merge);
            write.f(progress);

            write.i(ingredients.size);

            for (Entry entry : ingredients) {
                write.i(entry.key);
                write.i(entry.value);
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            crafting = read.bool();
            moveOut = read.bool();
            merge = read.bool();
            progress = read.f();

            int size = read.i();

            for (int i = 0; i < size; i++) {
                int key = read.i();
                int pos = read.i();
                ingredients.put(key, pos);
            }

            // TODO put somewhere else?
            dirty = true;
        }
    }

    public static class PayloadManufacturingRecipe {
        public IntMap<UnlockableContent> requirements = new IntMap<>();
        // if not null then this recipe is shapeless
        public PayloadStack[] shapelessRequirements;
        public UnlockableContent result;

        public PayloadManufacturingRecipe(UnlockableContent result, Cons<PayloadManufacturingRecipe> run) {
            this.result = result;
            run.get(this);
        }

        public PayloadManufacturingRecipe(UnlockableContent result, PayloadStack[] shapelessRequirements) {
            this.result = result;
            this.shapelessRequirements = shapelessRequirements;
        }

        public void mapRequirements(UnlockableContent[][] array) {
            for (int y = 0; y < array.length; y++) {
                UnlockableContent[] row = array[array.length - 1 - y];

                for (int x = 0; x < row.length; x++) {
                    requirements.put(Point2.pack(x, y), row[x]);
                }
            }
        }
    }
}
