package carpediem.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.draw.*;

// i eata da payload
public class PayloadBurner extends PayloadBlock {
    public Block consumedBlock;
    public float burnDuration = 60f * 60f;

    public DrawBlock topDrawer;

    public PayloadBurner(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        topDrawer.load(this);
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{region, inRegion, topRegion};
    }

    public class PayloadBurnerBuild extends PayloadBlockBuild<BuildPayload> {
        public float warmup;
        public float burnTime;

        @Override
        public void updateTile() {
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
