package carpediem.world.blocks.units;

import arc.graphics.g2d.*;
import carpediem.content.*;
import mindustry.world.blocks.units.*;

// this shit is so ass
public class CDUnitCargoUnloadPoint extends UnitCargoUnloadPoint {
    public CDUnitCargoUnloadPoint(String name) {
        super(name);
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{region, teamRegions[CDTeams.coalition.id]};
    }
}
