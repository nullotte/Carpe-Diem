package carpediem.world.blocks.storage;

import arc.*;
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
import carpediem.content.*;
import carpediem.ui.*;
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
import mindustry.world.consumers.*;

// IT CAN CRAFT ITEMS DEAR FUCKING GOD
public class LandingPod extends DrawerCoreBlock {
    // are you kidding me
    public static Recipe selectedRecipe;
    public static int amountCrafting;
    public static TextField search;
    public static int rowCount;

    public Seq<Recipe> recipes = new Seq<>();

    public Effect craftEffect = Fx.none;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public float warmupSpeed = 0.019f;

    public float craftingSpeed = 4f;

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

    public class LandingPodBuild extends DrawerCoreBuild {
        public Queue<RecipeRequest> pending = new Queue<>();
        public float warmup;

        @Override
        public void updateTile() {
            super.updateTile();

            Recipe currentRecipe = getCurrentRecipe();

            if (currentRecipe != null) {
                RecipeRequest currentRequest = pending.first();

                if (efficiency > 0) {
                    currentRequest.progress += getProgressIncrease(currentRecipe.craftTime / craftingSpeed);
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
        }

        @Override
        public void buildConfiguration(Table table) {
            table.table(Styles.black6, t -> {
                t.table(left -> {
                    left.add("@stat.recipes").color(Pal.accent).growX().pad(10f).top().left();
                    left.row();

                    if (search != null) search.clearText();

                    ButtonGroup<ImageButton> group = new ButtonGroup<>();
                    group.setMinCheckCount(0);
                    Table cont = new Table().top();
                    Runnable rebuildRecipes = () -> {
                        group.clear();
                        cont.clearChildren();

                        String text = search != null ? search.getText() : "";
                        int i = 0;
                        rowCount = 0;
                        Seq<Recipe> available = Seq.with(recipes).retainAll(Recipe::unlockedNow).retainAll(r -> text.isEmpty() || r.primaryOutput.localizedName.toLowerCase().contains(text.toLowerCase())).sort(r -> r.valid(this) ? 0f : 1f);
                        for (Recipe recipe : available) {
                            ImageButton button = cont.button(Tex.whiteui, Styles.clearNoneTogglei, 24f, () -> {}).size(40f).tooltip(recipe.primaryOutput.localizedName).group(group).get();
                            button.clicked(() -> selectedRecipe = recipe);
                            button.getStyle().imageUp = new TextureRegionDrawable(recipe.primaryOutput.uiIcon).tint(recipe.valid(this) ? Color.white : Color.gray);
                            button.update(() -> button.setChecked(selectedRecipe == recipe));

                            if (i++ % 4 == 3) {
                                cont.row();
                                rowCount++;
                            }
                        }
                    };
                    rebuildRecipes.run();

                    left.table(s -> {
                        s.image(Icon.zoom).padLeft(4f);
                        search = s.field(null, text -> rebuildRecipes.run()).padBottom(4).left().growX().get();
                        search.setMessageText("@players.search");
                    }).fillX().row();

                    ScrollPane pane = new ScrollPane(cont, Styles.smallPane);
                    pane.setScrollingDisabled(true, false);
                    pane.exited(() -> {
                        if (pane.hasScroll()) {
                            Core.scene.setScrollFocus(null);
                        }
                    });
                    pane.setScrollYForce(selectScroll);
                    Seq<Recipe> prevAvailable = new Seq<>();
                    pane.update(() -> {
                        selectScroll = pane.getScrollY();
                        Seq<Recipe> available = Seq.with(recipes).retainAll(r -> r.valid(this));
                        if (!prevAvailable.equals(available)) {
                            prevAvailable.set(available);
                            rebuildRecipes.run();
                        }
                    });
                    pane.setOverscroll(false, false);
                    left.add(pane).growX().maxHeight(40f * 6f);
                }).growX().top();

                t.table(right -> {
                    right.add("@currentlycrafting").color(Pal.accent).growX().pad(10f).top().left();
                    right.row();

                    Table pendingTable = new Table();
                    Runnable rebuildPending = () -> {
                        pendingTable.clearChildren();

                        int i = 0;
                        for (RecipeRequest request : pending) {
                            if (i++ > 5) {
                                pendingTable.add("...").color(Color.lightGray);
                                break;
                            }

                            UnlockableContent output = recipes.get(request.index).primaryOutput;

                            ImageButton button = new ImageButton(Tex.whiteui, Styles.clearNonei);
                            button.resizeImage(24f);
                            button.clicked(() -> {
                                cancelRequest(request);
                            });
                            button.getStyle().imageUp = new TextureRegionDrawable(output.uiIcon);

                            Table progress = new Table(Styles.flatOver).bottom();
                            progress.setHeight(0f);
                            progress.update(() -> progress.setHeight(request.progress * 40f));

                            Table label = new Table().bottom().left();
                            label.label(() -> request.stack + "").pad(5f).touchable(Touchable.disabled);

                            pendingTable.stack(progress, button, label).size(40f).left();
                        }

                        if (i == 0) {
                            pendingTable.add("@none").color(Color.lightGray);
                        }
                    };
                    rebuildPending.run();
                    int[] pendingSize = {pending.size};
                    right.add(pendingTable).update(p -> {
                        if (pendingSize[0] != pending.size) {
                            rebuildPending.run();
                            pendingSize[0] = pending.size;
                        }
                    }).growX().height(40f);
                    right.row();

                    right.add("@requirements").color(Pal.accent).growX().pad(10f).top().left();
                    right.row();

                    Table requirementsTable = new Table(Styles.grayPanel);
                    Runnable rebuildRequirements = () -> {
                        requirementsTable.clearChildren();

                        if (selectedRecipe != null) {
                            for (Consume consume : selectedRecipe.consumes) {
                                if (consume instanceof ConsumeItems consumeItems) {
                                    for (ItemStack stack : consumeItems.items) {
                                        requirementsTable.stack(
                                                new Table(o -> {
                                                    o.left();
                                                    o.add(new Image(stack.item.uiIcon)).size(32f).scaling(Scaling.fit);
                                                }),
                                                new Table(a -> {
                                                    a.left().bottom();
                                                    a.label(() -> {
                                                        int requiredAmount = stack.amount * amountCrafting;
                                                        int fulfilledAmount = items.get(stack.item);
                                                        return (fulfilledAmount < requiredAmount ? "[scarlet]" : "[white]") + fulfilledAmount + "[white]/" + requiredAmount;
                                                    }).style(Styles.outlineLabel);
                                                    a.pack();
                                                })
                                        ).pad(10f);
                                    }
                                }
                            }
                        } else {
                            requirementsTable.add("@none").color(Color.lightGray);
                        }
                    };
                    rebuildRequirements.run();
                    Recipe[] previousRecipe = {selectedRecipe};
                    right.add(requirementsTable).update(r -> {
                        if (previousRecipe[0] != selectedRecipe) {
                            rebuildRequirements.run();
                            previousRecipe[0] = selectedRecipe;
                        }
                    }).growX().height(60f).pad(5f);
                    right.row();

                    right.table(amountTable -> {
                        amountTable.add("@craftamount").color(Color.lightGray);
                        amountTable.field("1", v -> amountCrafting = Strings.parseInt(v)).valid(v -> Strings.parseInt(v) > 0).pad(2f);
                        amountCrafting = 1;
                    }).growX().pad(10f);
                    right.row();

                    right.table(buttonTable -> {
                        buttonTable.button("@craft", Styles.flatBordert, () -> {
                            requestRecipe(recipes.indexOf(selectedRecipe), amountCrafting);
                        }).disabled(b -> {
                            if (selectedRecipe == null) return true;

                            for (Consume consume : selectedRecipe.consumes) {
                                if (consume instanceof ConsumeItems consumeItems) {
                                    for (ItemStack stack : consumeItems.items) {
                                        if (!items.has(stack.item, stack.amount * amountCrafting)) return true;
                                    }
                                }
                            }

                            return false;
                        }).height(40f).pad(10f).left().growX();
                    }).growX();
                }).width(360f).grow();
            });
        }

        @Override
        public void updateTableAlign(Table table) {
            float offset = size * Vars.tilesize / 2f + 1f;
            Vec2 pos = Core.input.mouseScreen(x - offset, y + offset);
            table.pack();
            table.setPosition(pos.x, pos.y, Align.topRight);
        }

        @Override
        public void beginLaunch(boolean launching) {
            super.beginLaunch(launching);
            // custom sector landing info
            if (!Vars.headless) {
                if (!Vars.renderer.isLaunching()) {
                    Time.run(launchDuration(), () -> {
                        if (Vars.state.isCampaign() && Vars.showSectorLandInfo && Vars.state.rules.planet == CDPlanets.asphodel && Vars.state.rules.sector != CDSectorPresets.theReserve.sector) {
                            CDUI.showCustomLandInfo(Vars.state.rules.sector);
                        }
                    });
                }
            }
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
