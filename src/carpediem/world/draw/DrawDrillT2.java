package carpediem.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// this one gets its own class because i think it'd be easier for me to manually code all this than wrangle the vanilla drawers continuously
// like props to all you json modders out there but i dont think id be able to figure that shit out myself
public class DrawDrillT2 extends DrawBlock {
    public TextureRegion beam, bigRotator, smallRotator, full;
    public float beamLength = 7.5f, rotatorRadius = 4.75f, beamCycleTime = 6f * 60f, bigRotateSpeed = 3f, smallRotateSpeed = -5f;
    public Interp interp = Interp.pow2;

    @Override
    public void draw(Building build) {
        float z = Draw.z();
        Draw.z(Layer.block - 1.5f);

        float fin = (build.totalProgress() / beamCycleTime) % 1f;
        float fin2 = (build.totalProgress() / beamCycleTime + 0.25f) % 1f;

        // look man. i don't know what i'm doing.
        float bx = ((interp.apply(Mathf.clamp((Math.abs(2f * (fin - Mathf.floor(fin + 0.5f))) * 2f - 1f) * 2f, -1f, 1f) / 2f + 0.5f) * 2f) - 1f) * beamLength;
        float by = ((interp.apply(Mathf.clamp((Math.abs(2f * (fin2 - Mathf.floor(fin2 + 0.5f))) * 2f - 1f) * 2f, -1f, 1f) / 2f + 0.5f) * 2f) - 1f) * beamLength;

        for (int i = 0; i < 4; i++) {
            float sx = Angles.trnsx(build.totalProgress() * bigRotateSpeed + (i * 90f), rotatorRadius);
            float sy = Angles.trnsy(build.totalProgress() * bigRotateSpeed + (i * 90f), rotatorRadius);
            Drawf.spinSprite(smallRotator, build.x + bx + sx, build.y + by + sy, build.totalProgress() * smallRotateSpeed);
        }

        Draw.rect(beam, build.x, build.y + by);
        Drawf.spinSprite(bigRotator, build.x + bx, build.y + by, build.totalProgress() * bigRotateSpeed);

        Draw.z(z);
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{full};
    }

    @Override
    public void load(Block block) {
        beam = Core.atlas.find(block.name + "-beam");
        bigRotator = Core.atlas.find(block.name + "-bigrotator");
        smallRotator = Core.atlas.find(block.name + "-smallrotator");
        full = Core.atlas.find(block.name + "-all-yummies");
    }
}
