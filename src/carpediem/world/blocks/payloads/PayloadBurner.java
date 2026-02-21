package carpediem.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.world.meta.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

// i eata da payload
public class PayloadBurner extends PayloadBlock {
    public Block consumedBlock;
    public float burnDuration = 75f * 60f;

    public DrawBlock topDrawer;

    public PayloadBurner(String name) {
        super(name);
        acceptsPayload = true;
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(Stat.input, StatValues.blocks(Seq.with(consumedBlock)));
        stats.add(Stat.productionTime, burnDuration / 60f, StatUnit.seconds);

        // apparently i never properly implemented the processing types... this is going to come back to kill me in the future
        // anyways this code is ripped straight from the reconstructor
        Seq<ProcessableBlock> processableBlocks = Vars.content.blocks().select(b -> b instanceof ProcessableBlock).as();
        stats.add(CDStat.recipes, table -> {
            table.row();
            for (ProcessableBlock block : processableBlocks) {
                table.table(Styles.grayPanel, t -> {
                    t.left();
                    t.image(block.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit).with(i -> StatValues.withTooltip(i, block));
                    t.table(info -> {
                        info.add(block.localizedName).left();
                        info.row();
                    }).pad(10).left();
                }).fill().padTop(5).padBottom(5);

                table.table(Styles.grayPanel, t -> {
                    t.image(Icon.right).color(Pal.darkishGray).size(40).pad(10f);
                }).fill().padTop(5).padBottom(5);

                table.table(Styles.grayPanel, t -> {
                    t.left();
                    t.image(block.resultBlock.uiIcon).size(40).pad(10f).right().scaling(Scaling.fit).with(i -> StatValues.withTooltip(i, block.resultBlock));
                    t.table(info -> {
                        info.add(block.resultBlock.localizedName).right();
                        info.row();
                    }).pad(10).right();
                }).fill().padTop(5).padBottom(5);

                table.row();
            }
        });
    }

    @Override
    public void load() {
        super.load();
        topDrawer.load(this);
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{region, topRegion};
    }

    public class PayloadBurnerBuild extends PayloadBlockBuild<BuildPayload> {
        public float warmup;
        public float burnTime;

        @Override
        public void updateTile() {
            super.updateTile();

            if (moveInPayload() && burnTime <= 0f) {
                payload = null;

                burnTime = burnDuration;
            }

            if (burnTime > 0f) {
                burnTime -= delta();
                warmup = Mathf.approachDelta(warmup, 1f, 0.02f);
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, 0.02f);
            }
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload) {
            return this.payload == null && payload instanceof BuildPayload buildPayload && buildPayload.block() == consumedBlock;
        }

        @Override
        public boolean shouldConsume() {
            return burnTime > 0f;
        }

        @Override
        public float warmup() {
            return warmup;
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);

            //draw input
            for (int i = 0; i < 4; i++) {
                if (blends(i)) {
                    Draw.rect(inRegion, x, y, (i * 90f) - 180f);
                }
            }

            Draw.z(Layer.blockOver);
            drawPayload();

            Draw.z(Layer.blockOver + 0.1f);
            topDrawer.draw(this);
            Draw.rect(topRegion, x, y);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(burnTime);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            burnTime = read.f();
        }
    }
}
