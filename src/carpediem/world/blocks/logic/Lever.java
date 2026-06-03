package carpediem.world.blocks.logic;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.world.blocks.logic.*;

public class Lever extends SwitchBlock {
    public TextureRegion handleRegion, barRegion;

    public float handleLength = 9f / 4f;
    public float handleSpeed = 0.3f;

    public Lever(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();

        handleRegion = Core.atlas.find(name + "-handle");
        barRegion = Core.atlas.find(name + "-bar");
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{Core.atlas.find(name + "-icon")};
    }

    public class LeverBuild extends SwitchBuild {
        public float handleProgress;

        @Override
        public void updateTile() {
            handleProgress = Mathf.lerpDelta(handleProgress, Mathf.sign(enabled), handleSpeed);
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);

            Draw.rect(
                    barRegion,
                    x,
                    y,
                    barRegion.width * barRegion.scl() * Draw.xscl,
                    (barRegion.height * barRegion.scl() * Draw.yscl) * handleProgress
            );
            Draw.rect(handleRegion, x, y + (handleLength * handleProgress));
        }
    }
}
