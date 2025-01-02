package carpediem.world.blocks.storage;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.util.pooling.Pool.*;
import arc.util.pooling.*;
import carpediem.world.blocks.crafting.*;
import carpediem.world.meta.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.*;
import mindustry.world.consumers.*;

// IT CAN CRAFT ITEMS DEAR FUCKING GOD
public class LandingPod extends DrawerCoreBlock {
    // TODO get rid of this when v8 releases
    public static Building launchBuild;

    public static Recipe selectedRecipe;
    public static int amountCrafting;

    public Seq<Recipe> recipes = new Seq<>();

    public Effect craftEffect = Fx.none;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public float warmupSpeed = 0.019f;

    public float craftingSpeed = 0.5f;

    public LandingPod(String name) {
        super(name);
        configurable = true;
        selectionColumns = 8;
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("items");
    }

    @Override
    public void init() {
        for (Recipe recipe : recipes) {
            recipe.apply(this);
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

    @Override
    public boolean isHidden() {
        return false;
    }

    // really?
    public boolean realIsHidden() {
        return super.isHidden();
    }

    @Override
    public boolean isVisible() {
        return !realIsHidden() && (Vars.state.rules.editor || (!Vars.state.rules.hideBannedBlocks || !Vars.state.rules.isBanned(this)));
    }

    @Override
    public void drawLanding(CoreBuild build, float x, float y) {
        // wawawawawawa
        if (Vars.renderer.isLaunching()) {
            super.drawLanding(build, launchBuild.x, launchBuild.y);
        } else {
            super.drawLanding(build, x, y);
        }
    }

    public class LandingPodBuild extends DrawerCoreBuild {
        public Queue<RecipeRequest> pending = new Queue<>();
        public float warmup;

        public Table pendingTable;

        @Override
        public void updateTile() {
            super.updateTile();

            Recipe currentRecipe = getCurrentRecipe();

            if (currentRecipe != null) {
                RecipeRequest currentRequest = pending.first();

                if (efficiency > 0) {
                    currentRequest.progress += getProgressIncrease(currentRecipe.craftTime * craftingSpeed);
                    warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);

                    // do i even. for consistency's sake sure but also you Cant use liquids in the core !!
                    currentRecipe.update(this);

                    if (wasVisible && Mathf.chanceDelta(updateEffectChance)) {
                        updateEffect.at(x + Mathf.range(block.size * 4f), y + Mathf.range(block.size * 4));
                    }
                } else {
                    warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
                }

                if (currentRequest.progress >= 1f) {
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
            first.progress %= 1f;

            if (first.stack <= 0) {
                // death
                pending.removeFirst();
                Pools.free(first);
                rebuildPending();
            }
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
                        pending.addLast(Pools.obtain(RecipeRequest.class, RecipeRequest::new).set(index, 1, 0f));
                    }

                    for (Consume consume : recipe.consumes) {
                        consume.trigger(this);
                    }
                } else {
                    break;
                }
            }

            rebuildPending();
        }

        public void cancelRequest(RecipeRequest request) {
            for (Consume consume : recipes.get(request.index).consumes) {
                if (consume instanceof ConsumeItems consumeItems) {
                    for (ItemStack stack : consumeItems.items) {
                        items.add(stack.item, stack.amount * request.stack);
                    }
                }
            }

            pending.remove(request, true);
            Pools.free(request);
            rebuildPending();
        }

        @Override
        public void buildConfiguration(Table table) {
            selectedRecipe = null;
            amountCrafting = 1;

            table.table(Styles.black6, t -> {
                t.table(Tex.underline, pendingLabel -> {
                    pendingLabel.add("@crafting").color(Pal.accent).pad(5f).growX().left();
                }).growX();
                t.row();

                t.table(p -> {
                    pendingTable = p;
                    rebuildPending();
                }).growX();
                t.row();

                t.table(Tex.underline, optionsLabel -> {
                    optionsLabel.add("@availablerecipes").color(Pal.accent).pad(5f).growX();
                }).growX();
                t.row();

                Seq<UnlockableContent> prevAvailable = new Seq<>();
                Cons<Table> rebuildOptions = optionsTable -> {
                    optionsTable.clear();
                    Seq<UnlockableContent> available = Seq.with(recipes).retainAll(r -> r.unlockedNow() && r.valid(this)).map(r -> r.primaryOutput);
                    if (available.any()) {
                        Sector sector = Vars.state.rules.sector;
                        Vars.state.rules.sector = null;
                        ItemSelection.buildTable(LandingPod.this, optionsTable, available, () -> selectedRecipe == null ? null : selectedRecipe.primaryOutput, content -> selectedRecipe = recipes.find(r -> r.primaryOutput == content), false, selectionRows, selectionColumns);
                        Vars.state.rules.sector = sector;
                        // oh my goddd
                        ((Table) optionsTable.getCells().peek().left().get()).background(null);
                    } else {
                        optionsTable.add("@none").color(Color.lightGray).pad(10f).growX();
                    }
                };
                t.table(rebuildOptions).update(p -> {
                    Seq<UnlockableContent> available = Seq.with(recipes).retainAll(r -> r.unlockedNow() && r.valid(this)).map(r -> r.primaryOutput);
                    if (!prevAvailable.equals(available)) {
                        // changed, rebuild
                        prevAvailable.set(available);

                        if (selectedRecipe != null && !available.contains(selectedRecipe.primaryOutput)) {
                            selectedRecipe = null;
                        }

                        rebuildOptions.get(p);
                    }
                }).growX();
                t.row();

                t.table(amountTable -> {
                    amountTable.add("@craftamount").color(Color.lightGray);
                    amountTable.field("1", v -> amountCrafting = Strings.parseInt(v)).valid(v -> Strings.parseInt(v) > 0).pad(2f);
                }).growX().pad(10f);
                t.row();

                t.table(buttonTable -> {
                    buttonTable.button("@craft", Styles.flatBordert, () -> {
                        requestRecipe(recipes.indexOf(selectedRecipe), amountCrafting);
                    }).disabled(b -> selectedRecipe == null).height(40f).pad(10f).left().growX();
                }).growX();
            }).minWidth(selectionColumns * 40f);
        }

        public void rebuildPending() {
            if (pendingTable != null) {
                pendingTable.clear();
                int i = 0;

                for (RecipeRequest request : pending) {
                    UnlockableContent output = recipes.get(request.index).primaryOutput;

                    ImageButton button = new ImageButton(Tex.whiteui, Styles.clearNonei);
                    button.resizeImage(24f);
                    button.clicked(() -> {
                        cancelRequest(request);
                    });
                    button.getStyle().imageUp = new TextureRegionDrawable(output.uiIcon);

                    Table progress = new Table(Styles.flatOver).bottom();
                    progress.update(() -> progress.setHeight(request.progress * 40f));

                    Table label = new Table().bottom().left();
                    label.label(() -> request.stack + "").pad(5f).touchable(Touchable.disabled);

                    pendingTable.stack(progress, button, label).size(40f).left();

                    if (i++ % selectionColumns == (selectionColumns - 1)) {
                        pendingTable.row();
                    }
                }

                if (i == 0) {
                    pendingTable.add("@none").color(Color.lightGray).pad(10f).padLeft(20f).growX().left();
                }
            }
        }

        @Override
        public void updateTableAlign(Table table) {
            float offset = size * Vars.tilesize / 2f + 1f;
            Vec2 pos = Core.input.mouseScreen(x - offset, y + offset);
            table.setPosition(pos.x, pos.y, Align.topRight);
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(warmup);

            write.i(pending.size);
            // this is ordered right? i hope it is
            for (RecipeRequest request : pending) {
                write.i(request.index);
                write.i(request.stack);
                write.f(request.progress);
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            warmup = read.f();

            int size = read.i();
            for (int i = 0; i < size; i++) {
                // do i even
                RecipeRequest request = Pools.obtain(RecipeRequest.class, RecipeRequest::new).set(read.i(), read.i(), read.f());
                pending.addLast(request);
            }
        }

        // get rid of this in v8 too AAAAKJLGHKLJKFJKGKJFLGKLJFKLGD
        @Override
        public void updateLandParticles() {
            if (Vars.renderer.isLaunching()) {
                float time = Vars.coreLandDuration - Vars.renderer.getLandTime();
                float tsize = Mathf.sample(thrusterSizes, (time + 35f) / Vars.coreLandDuration);

                Vars.renderer.setLandPTimer(Vars.renderer.getLandPTimer() + tsize * Time.delta);
                if (Vars.renderer.getLandTime() >= 1f) {
                    if (launchBuild != null && launchBuild.tile != null) {
                        launchBuild.tile.getLinkedTiles(t -> {
                            if (Mathf.chance(0.4f)) {
                                Fx.coreLandDust.at(t.worldx(), t.worldy(), angleTo(t.worldx(), t.worldy()) + Mathf.range(30f), Tmp.c1.set(t.floor().mapColor).mul(1.5f + Mathf.range(0.15f)));
                            }
                        });
                    }

                    Vars.renderer.setLandPTimer(0f);
                }
            } else {
                super.updateLandParticles();
            }
        }
    }

    public static class RecipeRequest implements Poolable {
        public int index;
        public int stack;
        public float progress;

        public RecipeRequest set(int index, int stack, float progress) {
            this.index = index;
            this.stack = stack;
            this.progress = progress;
            return this;
        }

        @Override
        public void reset() {
            index = -1;
            stack = 0;
            progress = 0f;
        }
    }
}
