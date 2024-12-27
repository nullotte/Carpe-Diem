package carpediem.world.draw;

import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// jesus christ
public class DrawTeam extends DrawBlock {
    @Override
    public void draw(Building build) {
        build.drawTeamTop();
    }

    @Override
    public TextureRegion[] icons(Block block) {
        // sharded ? whos that
        return new TextureRegion[]{block.teamRegions[47]};
    }
}
