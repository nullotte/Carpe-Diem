package carpediem.world.draw;

import arc.graphics.g2d.*;
import arc.math.*;
import carpediem.world.blocks.crafting.RecipeCrafter.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.draw.*;

// ??? ?? ??????? ?? ??????
public class DrawPressItem extends DrawBlock {
    public Interp interp = Interp.pow2Out, squishInterp = Interp.pow2In;
    public float fromIn = 0.4f, toIn = 0.5f, fromOut = 0.6f, toOut = 0.7f;
    public float squishPoint = 0.96f;

    @Override
    public void draw(Building build) {
        /*
        if (build instanceof RecipeCrafterBuild crafter) {
            if (crafter.lastProduced != null) {
                float size = Vars.itemSize * interp.apply(Mathf.curve(1f - build.progress(), fromIn, toIn));
                Draw.rect(
                        crafter.lastProduced.fullIcon,
                        build.x, build.y,
                        size, size
                );
            }

            if (crafter.current != null) {
                float size = Vars.itemSize * interp.apply(Mathf.curve(build.progress(), fromOut, toOut));
                Draw.rect(
                        crafter.current.inputItems[0].item.fullIcon,
                        build.x, build.y,
                        size, size * squishInterp.apply(1f - Mathf.curve(build.progress(), squishPoint))
                );
            }
        }
         */
    }
}
