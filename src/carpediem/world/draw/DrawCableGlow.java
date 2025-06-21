package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import carpediem.graphics.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawCableGlow extends DrawBlock {
    public TextureRegion region;

    @Override
    public void draw(Building build) {
        DrawCD.addCableGlowDraw(build.warmup(), () -> {
            Draw.rect(region, build.x, build.y);
        });
    }

    @Override
    public void load(Block block) {
        region = Core.atlas.find(block.name + "-glow");
    }
}
