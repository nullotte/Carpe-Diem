package carpediem.content;

import carpediem.content.blocks.*;
import carpediem.maps.planet.*;
import mindustry.content.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class CDPlanets {
    public static Planet planet;

    public static void load() {
        planet = new Planet("planet", Planets.sun, 2f, 1) {{
            alwaysUnlocked = true;
            generator = new ModdedPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 6);

            defaultCore = CDStorage.landingPodT0;
            allowLaunchToNumbered = false;

            ruleSetter = r -> {
                r.fog = true;
                r.staticFog = true;
                r.deconstructRefundMultiplier = 1f;

                r.hideBannedBlocks = true;
                r.blockWhitelist = true;

                // TODO ugly giant wall of crosses
                r.bannedBlocks.addAll(CDTechTree.blocks);
                r.bannedBlocks.addAll(Blocks.itemSource, Blocks.liquidSource, Blocks.payloadSource);
            };

            unlockedOnLand.add(CDStorage.landingPodT0);
        }};
    }
}
