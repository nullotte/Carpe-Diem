package carpediem.world.draw;

import arc.graphics.*;
import mindustry.gen.*;

public class DrawAlwaysGlowTeamRegion extends DrawAlwaysGlowRegion {
    @Override
    public Color color(Building build) {
        return build.team.palette[1];
    }
}
