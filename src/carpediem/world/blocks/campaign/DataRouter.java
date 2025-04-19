package carpediem.world.blocks.campaign;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import carpediem.world.blocks.campaign.data.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class DataRouter extends Block implements DataBlock {
    public TextureRegion dataRegion, bottomRegion, topRegion;

    public DataRouter(String name) {
        super(name);

        group = BlockGroup.transportation;
        instantTransfer = true;
        update = true;
        solid = false;
        underBullets = true;
        rotate = true;
        priority = TargetPriority.transport;
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(Stat.range, DataBuild.baseDataStrength);
        stats.remove(Stat.maxConsecutive);
    }

    @Override
    public void setBars() {
        super.setBars();

        addBar("datastrength", (DataRouterBuild entity) -> new Bar(
                () -> Core.bundle.get("bar.datastrength"),
                () -> entity.data == null ? Color.clear : entity.data.color(),
                entity::dataStrengthFrac
        ));
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(bottomRegion, plan.drawx(), plan.drawy());
        Draw.rect(region, plan.drawx(), plan.drawy());
        Draw.rect(topRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
    }

    @Override
    public void load() {
        super.load();

        dataRegion = Core.atlas.find(name + "-data");
        bottomRegion = Core.atlas.find(name + "-bottom");
        topRegion = Core.atlas.find(name + "-top");
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{bottomRegion, region, topRegion};
    }

    @Override
    public boolean rotatedOutput(int x, int y) {
        return false;
    }

    public class DataRouterBuild extends Building implements DataBuild {
        public DataType data;
        public int strength;
        public boolean handled;
        public Building source;
        public DataBuild sourcec;

        @Override
        public void updateTile() {
            super.updateTile();

            handled = false;

            if (source == null || !source.isValid() || sourcec.data() == null) {
                data = null;
            }
        }

        @Override
        public DataType data() {
            return data;
        }

        @Override
        public int dataStrength() {
            return strength;
        }

        @Override
        public boolean acceptData(Building source, DataType data) {
            return DataBuild.super.acceptData(source, data) && (!handled || !source.block.instantTransfer) && (Edges.getFacingEdge(source.tile, tile).relativeTo(tile) == rotation);
        }

        @Override
        public void handleData(Building source, DataType data) {
            handled = true;
            this.data = data;

            if (source instanceof DataBuild build) {
                this.source = source;
                sourcec = build;
                strength = build.dataStrength() - 1;
            }

            if (strength > 0) {
                for (Building build : proximity) {
                    if (relativeTo(build) != (rotation + 2) % 4 && build instanceof DataBuild next && next.acceptData(this, data)) {
                        next.handleData(this, data);
                    }
                }
            }
        }

        @Override
        public void draw() {
            Draw.rect(bottomRegion, x, y);
            if (data != null) {
                Draw.color(data.color(), dataStrengthFrac());
                Draw.rect(dataRegion, x, y);
                Draw.color();
            }
            Draw.rect(region, x, y);
            Draw.rect(topRegion, x, y, rotation * 90f);
        }

        @Override
        public void display(Table table) {
            super.display(table);

            if (team != Vars.player.team()) return;

            table.row();
            table.label(() -> Core.bundle.format("bar.transmitting", data == null ? "[lightgray]" + Core.bundle.get("none") : data.name())).pad(4).wrap().width(200f).left();
        }
    }
}
