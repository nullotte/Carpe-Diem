package carpediem.world.meta;

import mindustry.gen.*;
import mindustry.world.meta.*;

public class CDStat {
    public static final Stat
    recipes = new Stat("recipes", StatCat.crafting),
    contents = new Stat("contents")
    ;

    public static final StatUnit
    pressureUnits = new StatUnit("pressureUnits", "[violet]" + Iconc.spray + "[]")
    ;
}
