package carpediem.world.meta;

import mindustry.gen.*;
import mindustry.world.meta.*;

public class CDStat {
    public static final Stat
    recipes = new Stat("recipes", StatCat.crafting),
    contents = new Stat("contents"),
    inputEdge = new Stat("input-edge", StatCat.crafting),
    inputCorner = new Stat("input-corner", StatCat.crafting)
    ;

    public static final StatUnit
    pressureUnits = new StatUnit("pressureUnits", "[violet]" + Iconc.move + "[]")
    ;
}
