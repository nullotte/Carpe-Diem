package carpediem.world.blocks.campaign;

import arc.*;
import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.type.*;
import carpediem.world.blocks.campaign.data.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

public class ArchiveResearchBlock extends Block implements DataBlock {
    public float researchTime = 120f;

    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public float warmupSpeed = 0.019f;

    public DrawBlock drawer;

    public ArchiveResearchBlock(String name) {
        super(name);
        update = true;
        solid = true;
        hasItems = true;
        configurable = true;
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("items");
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.productionTime, researchTime / 60f, StatUnit.seconds);
    }

    @Override
    public void load() {
        super.load();

        drawer.load(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    protected TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public boolean outputsData() {
        return false;
    }

    public class ArchiveResearchBuild extends Building implements DataBuild {
        public ArchiveDataType data;

        public float progress;
        public float totalProgress;
        public float warmup;

        public Building source;
        public DataBuild sourcec;

        @Override
        public void updateTile() {
            if (source == null || !source.isValid() || sourcec.data() == null) {
                data = null;
            }

            if (efficiency > 0f) {
                progress += getProgressIncrease(researchTime);
                warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);

                if (wasVisible && Mathf.chanceDelta(updateEffectChance)) {
                    updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4));
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            totalProgress += warmup * Time.delta;

            if (progress >= 1f) {
                spend();
            }
        }

        public void spend() {
            if (researching()) {
                TechNode node = archive().techNode;

                for (int i = 0; i < node.requirements.length; i++) {
                    ItemStack required = node.requirements[i];
                    ItemStack completed = node.finishedRequirements[i];

                    if (items.has(required.item) && completed.amount < required.amount) {
                        completed.amount++;
                        items.remove(required.item, 1);
                    }
                }

                checkUnlock();
            }

            progress %= 1f;
        }

        @Override
        public boolean shouldConsume() {
            return researching() && items.total() > 0;
        }

        @Override
        public void buildConfiguration(Table table) {
            table.button(Icon.info, Styles.cleari, () -> {
                if (archive() != null && archive().unlockedNow()) {
                    Vars.ui.content.show(archive());
                }
            }).size(40f);
        }

        @Override
        public boolean shouldShowConfigure(Player player) {
            return archive() != null && archive().unlockedNow();
        }

        @Override
        public Cursor getCursor() {
            return !shouldShowConfigure(null) ? SystemCursor.arrow : super.getCursor();
        }

        public boolean researching() {
            return data != null && data.archive.techNode != null && !data.archive.unlockedNow();
        }

        public Archive archive() {
            if (data != null) {
                return data.archive;
            }

            return null;
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            if (researching()) {
                TechNode node = archive().techNode;
                ItemStack required = Structs.find(node.requirements, s -> s.item == item);
                ItemStack completed = Structs.find(node.finishedRequirements, s -> s.item == item);

                return required != null && completed != null && completed.amount + items.get(item) < required.amount;
            }

            return false;
        }

        public void checkUnlock() {
            if (researching()) {
                TechNode node = archive().techNode;

                for (int i = 0; i < node.requirements.length; i++) {
                    ItemStack required = node.requirements[i];
                    ItemStack completed = node.finishedRequirements[i];

                    if (completed.amount < required.amount) return;
                }

                archive().unlock();
            }
        }

        @Override
        public DataType data() {
            return data;
        }

        @Override
        public boolean acceptData(Building source, DataType data) {
            return DataBuild.super.acceptData(source, data) && data instanceof ArchiveDataType;
        }

        @Override
        public void handleData(Building source, DataType data) {
            if (data instanceof ArchiveDataType archiveData) {
                this.data = archiveData;
            }

            if (source instanceof DataBuild build) {
                this.source = source;
                sourcec = build;
            }
        }

        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public void display(Table table) {
            super.display(table);

            if (team != Vars.player.team()) return;

            table.row();
            table.label(() -> Core.bundle.format("bar.decoding", data == null ? "[lightgray]" + Core.bundle.get("none") : "[accent]" + data.archive.localizedName)).pad(4f).wrap().width(200f).left();
            table.row();
            // yeah this rebuilds every single frame . believe me im a very good coder
            // also apparently unlocking a block while hovering over a block just clears the display
            table.table(this::rebuildRequirements).update(this::rebuildRequirements)
                    .visible(() -> data != null).growX().left().margin(3f);
        }

        public void rebuildRequirements(Table table) {
            table.clearChildren();

            if (data != null) {
                table.label(() -> Core.bundle.format(!researching() ? "bar.decodingcomplete" : "bar.requirements")).growX().left();
                table.row();
            }

            if (researching()) {
                table.top().left();
                TechNode node = archive().techNode;

                for (int i = 0; i < node.requirements.length; i++) {
                    ItemStack required = node.requirements[i];
                    ItemStack completed = node.finishedRequirements[i];

                    table.table(line -> {
                        line.left();
                        line.image(required.item.uiIcon).size(8f * 2f);
                        line.add(required.item.localizedName).maxWidth(140f).fillX().color(Color.lightGray).padLeft(2f).left();
                        line.labelWrap(() -> UI.formatAmount(Math.min(completed.amount, required.amount)) + "/" + UI.formatAmount(required.amount)).padLeft(8f);
                    }).left();
                    table.row();
                }
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(progress);
            write.f(warmup);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            progress = read.f();
            warmup = read.f();
        }
    }
}