package carpediem.world.draw;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.draw.*;

public class DrawRotatingLiquidTile extends DrawLiquidTile {
    public float[] rotateOrderedPaddings;

    @Override
    public void draw(Building build) {
        Liquid drawn = drawLiquid != null ? drawLiquid : build.liquids.current();
        LiquidBlock.drawTiledFrames(
                build.block.size,
                build.x, build.y,
                rotateOrderedPaddings[(2 + build.rotation) % 4],
                rotateOrderedPaddings[build.rotation],
                rotateOrderedPaddings[(3 + build.rotation) % 4],
                rotateOrderedPaddings[(1 + build.rotation) % 4],
                drawn,
                build.liquids.get(drawn) / build.block.liquidCapacity * alpha
        );
    }

    @Override
    public void load(Block block) {
        super.load(block);
        rotateOrderedPaddings = new float[]{
                padRight,
                padBottom,
                padLeft,
                padTop
        };
    }
}
