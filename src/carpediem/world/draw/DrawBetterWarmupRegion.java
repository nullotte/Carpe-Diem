package carpediem.world.draw;

import arc.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// this is my fifteenth reason
public class DrawBetterWarmupRegion extends DrawWarmupRegion {
    @Override
    public void load(Block block) {
        region = Core.atlas.find(block.name + "-warmup");
    }
}
