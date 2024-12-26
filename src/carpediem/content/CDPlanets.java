package carpediem.content;

import carpediem.content.blocks.*;
import carpediem.maps.planet.*;
import mindustry.content.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class CDPlanets {
    public static Planet asphodel;

    public static void load() {
        asphodel = new Planet("asphodel", Planets.sun, 2f, 1) {{
            alwaysUnlocked = true;
            generator = new AsphodelPlanetGenerator();

            meshLoader = () -> new HexMesh(this, 6);
            // i cant be bothered . maybe make a custom atmosphere shader one day (never)
            // the vanilla one has a size limit or something. also clips with the planet itself
            hasAtmosphere = false;

            defaultCore = CDStorage.landingPodT0;
            allowLaunchToNumbered = false;
            prebuildBase = false;

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
