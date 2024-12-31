package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawInvert extends DrawBlock {
    public TextureRegion region;
    public Interp interp = a -> {
        float b = Mathf.slope(a);
        float c = Mathf.curve(b, 0.8f, 0.9f);
        return Interp.pow2In.apply(c);
    };

    @Override
    public void draw(Building build) {
        Draw.alpha(interp.apply(build.progress()));
        Draw.rect(region, build.x, build.y);
        Draw.color();
    }

    @Override
    public void load(Block block) {
        region = Core.atlas.find(block.name + "-invert");
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{};
    }
}
