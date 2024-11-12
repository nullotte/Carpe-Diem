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

            defaultCore = CDStorage.landingPod;
        }};
    }
}
