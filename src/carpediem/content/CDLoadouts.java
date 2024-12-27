package carpediem.content;

import arc.struct.*;
import arc.util.*;
import carpediem.content.blocks.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.world.blocks.storage.*;

public class CDLoadouts {
    public static Schematic schemLandingPodT0;

    public static void load() {
        schemLandingPodT0 = Schematics.readBase64("bXNjaAF4nBXKQQoCMQxA0YwWN57AXS5QmCu48w7iIp2GIZAmpQ3I3N4RPm/1IUG6QjJqDKgygw3D8cUU+Czmo5FiOVAO/xLcK89tSA9xA4CbUmGdcHl/FnhsNDrnKtyyklWxPXevOdbzXP6d/AALRx62");

        Vars.schematics.getLoadouts(CDStorage.landingPodT0).add(schemLandingPodT0);
        // lalalalala
        Reflect.<ObjectMap<CoreBlock, Schematic>>get(Vars.schematics, "defaultLoadouts").put(CDStorage.landingPodT0, schemLandingPodT0);
    }
}
