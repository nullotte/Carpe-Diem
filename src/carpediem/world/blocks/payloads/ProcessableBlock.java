package carpediem.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.world.*;

public class ProcessableBlock extends Block {
    public Block resultBlock;

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
