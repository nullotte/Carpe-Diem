package carpediem.world.draw;

import arc.graphics.g2d.*;
import arc.util.*;
import carpediem.content.blocks.*;
import carpediem.world.blocks.distribution.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.Autotiler.*;
import mindustry.world.draw.*;

public class DrawBeltUnder extends DrawBlock {
    public Belt belt;

    public DrawBeltUnder(Block block) {
        if (block instanceof Belt belt) {
            this.belt = belt;
        }
    }

    @Override
    public void draw(Building build) {
        if (build instanceof BeltUnderBlending blend) {
            int frame = (int) (((Time.time / (belt.moveTime * belt.itemCapacity) * 8f * build.timeScale())) % 4);

            for (int i = 0; i < 4; i++) {
                if (blend.drawInput() && blend.blendInputs()[i] > 0) {
                    belt.drawSliced(build.x, build.y, i, SliceMode.bottom, frame, false);
                    if (blend.blendInputs()[i] == 2) {
                        belt.drawSliced(build.x, build.y, i, SliceMode.bottom, frame, true);
                    }
                } else if (blend.drawOutput() && blend.blendOutputs()[i] > 0) {
                    belt.drawSliced(build.x, build.y, i, SliceMode.top, frame, false);
                    if (blend.blendOutputs()[i] == 2) {
                        belt.drawSliced(build.x, build.y, i, SliceMode.top, frame, true);
                    }
                }
            }
        }
    }

    public interface BeltUnderBlending {
        int[] blendInputs();
        int[] blendOutputs();

        default Belt belt() {
            return (Belt) CDDistribution.belt;
        }

        default boolean drawInput() {
            return true;
        }

        default boolean drawOutput() {
            return true;
        }

        default void buildBlending(Building build) {
            buildBlending(build, -1, -1);
        }

        default void buildBlending(Building build, int input, int output) {
            for (int i = 0; i < 4; i++) {
                blendInputs()[i] = blendOutputs()[i] = 0;
                Building other = build.nearby(i);

                if (other != null) {
                    int result = other.block.squareSprite || other instanceof BeltUnderBlending ? 1 : 2;
                    if ((input < 0 && belt().input(build.tile, build.rotation, other.tile, other.rotation, other.block)) || i == input) {
                        blendInputs()[i] = result;
                    } else if ((output < 0 && belt().output(build.tile, build.rotation, other.tile, other.rotation, other.block)) || i == output) {
                        blendOutputs()[i] = result;
                    }
                }
            }
        }
    }
}
