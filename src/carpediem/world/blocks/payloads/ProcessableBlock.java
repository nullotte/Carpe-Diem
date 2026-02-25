package carpediem.world.blocks.payloads;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

public class ProcessableBlock extends Block {
    public Block resultBlock;
    public Color processColor = Pal.slagOrange;
    public float processAlpha = 0.5f;

    public ProcessableBlock(String name) {
        super(name);
        solid = true;
        destructible = true;
        canOverdrive = false;
        drawDisabled = false;
    }

    public class ProcessableBuild extends Building {
        public float progress;

        @Override
        public void draw() {
            super.draw();
            Draw.alpha(progress);
            Draw.rect(resultBlock.region, x, y);

            Draw.color(processColor);
            Draw.alpha(Mathf.slope(progress) * processAlpha);
            Draw.blend(Blending.additive);
            Fill.rect(x, y, Vars.tilesize * size, Vars.tilesize * size);
            Draw.blend();
            Draw.color();
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(progress);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            progress = read.f();
        }
    }
}
