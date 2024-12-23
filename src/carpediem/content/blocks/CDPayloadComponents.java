package carpediem.content.blocks;

import carpediem.world.blocks.storage.*;
import mindustry.world.*;

public class CDPayloadComponents {
    public static Block
    packagedLandingPodT0;

    public static void load() {
        packagedLandingPodT0 = new PackagedCoreBlock("packaged-landing-pod-t0", CDStorage.landingPodT0);
    }
}
