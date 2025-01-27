package carpediem.world.blocks.liquid;

import mindustry.gen.*;
import mindustry.type.*;

public class Valve extends MergingLiquidBlock {
    public Valve(String name) {
        super(name);
    }

    public class ValveBuild extends MergingLiquidBuild {
        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            // it's just that easy
            return false;
        }
    }
}
