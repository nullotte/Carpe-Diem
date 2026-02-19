package carpediem.world.blocks.payloads;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;

public class PayloadDepot extends PayloadBlock {
    public float payloadLimit;

    public PayloadDepot(String name) {
        super(name);
        rotate = true;
        acceptsPayload = true;
        outputsPayload = true;
        configurable = true;

        config(int[].class, (PayloadDepotBuild build, int[] data) -> {
            int conditionOrdinal = data[0];
            int ctype = data[1];
            int filterId = data[2];
            build.condition = conditionOrdinal == -1 ? null : PayloadDepotCondition.values()[conditionOrdinal];
            build.filter = ctype == -1 ? null : Vars.content.getByID(ContentType.all[ctype], filterId);
        });
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(Stat.payloadCapacity, StatValues.squared(payloadLimit, StatUnit.blocksSquared));
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, inRegion, outRegion};
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(region, plan.drawx(), plan.drawy());
        Draw.rect(inRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.rect(outRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
    }

    public static int[] formatConfig(PayloadDepotCondition condition, UnlockableContent filter) {
        return new int[]{
                condition == null ? -1 : condition.ordinal(),
                filter == null ? -1 : filter.getContentType().ordinal(),
                filter == null ? -1 : filter.id
        };
    }

    public boolean canFilter(Block b) {
        return b.isVisible() && b.size <= payloadLimit && !(b instanceof CoreBlock) && !Vars.state.rules.isBanned(b) && b.environmentBuildable();
    }

    public boolean canFilter(UnitType t) {
        return t.hitSize / Vars.tilesize <= payloadLimit && !t.isHidden() && !t.isBanned() && t.supportsEnv(Vars.state.rules.env);
    }

    public class PayloadDepotBuild extends PayloadBlockBuild<Payload> {
        public PayloadDepotCondition condition;
        public UnlockableContent filter;

        @Override
        public void updateTile() {
            super.updateTile();

            if (payload != null) {
                if (condition == null || condition.shouldMove(this)) {
                    moveOutPayload();
                } else {
                    moveInPayload();
                }
            }
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload) {
            return this.payload == null && payload.fits(payloadLimit);
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);

            // draw input
            boolean fallback = true;
            for (int i = 0; i < 4; i++) {
                if (blends(i) && i != rotation) {
                    Draw.rect(inRegion, x, y, (i * 90) - 180);
                    fallback = false;
                }
            }
            if (fallback) Draw.rect(inRegion, x, y, rotation * 90);

            Draw.rect(outRegion, x, y, rotdeg());

            Draw.z(Layer.blockOver);
            drawPayload();
        }

        @Override
        public void drawSelect() {
            if (condition != null) {
                drawPlaceText(condition.getEmoji() + " " + condition.localized(), tileX(), tileY(), true);

                if (condition.useFilter) {
                    drawItemSelection(filter);
                }
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            table.table(Styles.black6, t -> {
                ButtonGroup<ImageButton> group = new ButtonGroup<>();
                group.setMinCheckCount(0);

                for (PayloadDepotCondition item : PayloadDepotCondition.values()) {
                    ImageButton button = t.button(item.getIcon(), Styles.clearNoneTogglei, 40f, () -> {
                        configure(formatConfig(item, null));
                    }).tooltip(item.localized()).group(group).get();
                    button.update(() -> button.setChecked(condition == item));
                }
            });
            table.row();
            table.table(t -> {
                ItemSelection.buildTable(PayloadDepot.this, t,
                        Vars.content.blocks().select(PayloadDepot.this::canFilter).<UnlockableContent>as()
                                .add(Vars.content.units().select(PayloadDepot.this::canFilter).as()),
                        () -> filter, item -> configure(formatConfig(condition, item)), false);
            }).visible(() -> condition != null && condition.useFilter);
        }

        @Override
        public int[] config() {
            return formatConfig(condition, filter);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(condition == null ? -1 : condition.ordinal());
            write.b(filter == null ? -1 : filter.getContentType().ordinal());
            write.s(filter == null ? -1 : filter.id);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            int conditionOrdinal = read.i();
            if (conditionOrdinal != -1) {
                condition = PayloadDepotCondition.values()[conditionOrdinal];
            }

            byte ctype = read.b();
            short filterId = read.s();
            filter = ctype == -1 ? null : Vars.content.getByID(ContentType.all[ctype], filterId);
        }
    }

    public enum PayloadDepotCondition {
        filter(
                build -> build.filter == null || build.payload.content() == build.filter,
                "filter", true
        ),
        full(
                build -> build.payload instanceof BuildPayload buildPayload &&
                        ((buildPayload.block().hasLiquids && buildPayload.build.liquids.currentAmount() >= buildPayload.block().liquidCapacity - 0.001f) ||
                                (buildPayload.block().hasItems && buildPayload.block().separateItemCapacity && Vars.content.items().contains(i -> (buildPayload.build.items.get(i) >= buildPayload.block().itemCapacity))) ||
                                (buildPayload.block().consPower != null && buildPayload.block().consPower.buffered && buildPayload.build.power.status >= 0.999999999f)),
                "download"
        ),
        empty(
                build -> build.payload instanceof BuildPayload buildPayload &&
                        ((!buildPayload.block().hasItems || buildPayload.build.items.empty()) &&
                                (!buildPayload.block().hasLiquids || buildPayload.build.liquids.currentAmount() <= 0.011f) &&
                                (!(buildPayload.block().consPower != null && buildPayload.block().consPower.buffered) || buildPayload.build.power.status <= 0.0000001f)),
                "upload"
        );

        PayloadDepotCondition(Boolf<PayloadDepotBuild> func, String icon) {
            this(func, icon, false);
        }

        PayloadDepotCondition(Boolf<PayloadDepotBuild> func, String icon, boolean useFilter) {
            this.func = func;
            this.icon = icon;
            this.useFilter = useFilter;
        }

        public final Boolf<PayloadDepotBuild> func;
        public final String icon;
        public final boolean useFilter;

        // just to look nice
        public boolean shouldMove(PayloadDepotBuild build) {
            return func.get(build);
        }

        public TextureRegionDrawable getIcon() {
            return Icon.icons.get(icon, Icon.cancel);
        }

        public char getEmoji() {
            return (char) Iconc.codes.get(icon, Iconc.cancel);
        }

        public String localized() {
            return Core.bundle.get("condition." + name());
        }
    }
}
