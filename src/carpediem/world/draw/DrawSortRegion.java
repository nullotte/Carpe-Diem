package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.DuctRouter.*;
import mindustry.world.draw.*;

public class DrawSortRegion extends DrawBlock {
    public TextureRegion region;

    @Override
    public void draw(Building build) {
        Item sortItem = null;

        // this is where id put the other sorter thingys if i actually used them
        if (build instanceof DuctRouterBuild b) {
            sortItem = b.sortItem;
        }

        if (sortItem != null) {
            Draw.color(sortItem.color);
            Draw.rect(region, build.x, build.y);
            Draw.color();
        }
    }

    @Override
    public void load(Block block) {
        region = Core.atlas.find(block + "-sort");
    }
}
