package carpediem.world.blocks.campaign;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import carpediem.world.blocks.campaign.data.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.meta.*;

// OH GOD NO
public class DataChannel extends Block implements Autotiler, DataBlock {
    public TextureRegion[] regions, dataRegions, bottomRegions;

    public DataChannel(String name) {
        super(name);

        group = BlockGroup.transportation;
        instantTransfer = true;
        update = true;
        solid = false;
        conveyorPlacement = true;
        noUpdateDisabled = true;
        underBullets = true;
        rotate = true;
        priority = TargetPriority.transport;
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(Stat.range, DataBuild.baseDataStrength);

        // goodbye
        stats.remove(Stat.maxConsecutive);
    }

    @Override
    public void setBars() {
        super.setBars();

        addBar("datastrength", (DataChannelBuild entity) -> new Bar(
                () -> Core.bundle.get("bar.datastrength"),
                () -> entity.data == null ? Color.clear : entity.data.color(),
                entity::dataStrengthFrac
        ));
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        int[] bits = getTiling(plan, list);

        if (bits == null) return;

        Draw.scl(bits[1], bits[2]);
        Draw.rect(bottomRegions[bits[0]], plan.drawx(), plan.drawy(), plan.rotation * 90f);
        Draw.rect(regions[bits[0]], plan.drawx(), plan.drawy(), plan.rotation * 90f);
        Draw.scl();
    }

    @Override
    public void load() {
        super.load();

        regions = new TextureRegion[5];
        dataRegions = new TextureRegion[5];
        bottomRegions = new TextureRegion[5];

        for (int i = 0; i < 5; i++) {
            regions[i] = Core.atlas.find(name + "-" + i);
            dataRegions[i] = Core.atlas.find(name + "-data-" + i);
            bottomRegions[i] = Core.atlas.find(name + "-bottom-" + i);
        }
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{bottomRegions[0], regions[0]};
    }

    @Override
    public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock) {
        return otherblock instanceof DataBlock dataBlock && ((dataBlock.outputsData() || lookingAt(tile, rotation, otherx, othery, otherblock))
                && lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock));
    }

    public class DataChannelBuild extends Building implements DataBuild {
        public DataType data;
        public int strength;
        // stack over flfow
        public boolean handled;
        public int blendbits, xscl, yscl, blending;
        public Building source, next;
        public DataBuild sourcec, nextc;

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
            return DataBuild.super.acceptData(source, data) && (!handled || !source.block.instantTransfer);
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

            if (strength > 0 && nextc != null && nextc.acceptData(this, data)) {
                nextc.handleData(this, data);
            }
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();

            int[] bits = buildBlending(tile, rotation, null, true);
            blendbits = bits[0];
            xscl = bits[1];
            yscl = bits[2];
            blending = bits[4];

            next = null;
            nextc = null;

            Building front = front();
            if (front != null && front.team == this.team && front instanceof DataBuild next) {
                // confused? me too
                this.next = front;
                nextc = next;
            }
        }

        @Override
        public void draw() {
            float rotation = rotdeg();
            int r = this.rotation;

            // i had a chocloate to day it was very delicious
            for (int i = 0; i < 4; i++) {
                if ((blending & (1 << i)) != 0) {
                    int dir = r - i;
                    float rot = i == 0 ? rotation : (dir) * 90;
                    drawAt(x + Geometry.d4x(dir) * Vars.tilesize * 0.75f, y + Geometry.d4y(dir) * Vars.tilesize * 0.75f, 0, rot, i != 0 ? SliceMode.bottom : SliceMode.top);
                }
            }

            Draw.scl(xscl, yscl);
            drawAt(x, y, blendbits, rotation, SliceMode.none);
            Draw.reset();
        }

        public void drawAt(float x, float y, int bits, float rotation, SliceMode slice) {
            Draw.z(Layer.blockUnder);
            Draw.rect(sliced(bottomRegions[bits], slice), x, y, rotation);
            if (data != null) {
                Draw.color(data.color(), dataStrengthFrac());
                Draw.rect(sliced(dataRegions[bits], slice), x, y, rotation);
                Draw.color();
            }
            Draw.rect(sliced(regions[bits], slice), x, y, rotation);
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
