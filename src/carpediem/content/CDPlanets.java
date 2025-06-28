package carpediem.content;

import arc.graphics.*;
import carpediem.content.blocks.*;
import carpediem.maps.planet.*;
import carpediem.type.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class CDPlanets {
    public static Planet asphodel;

    public static void load() {
        asphodel = new AtmospherePlanet("asphodel", Planets.sun, 1f, 1) {{
            alwaysUnlocked = true;
            generator = new AsphodelPlanetGenerator();

            meshLoader = () -> new AtmosphereHexMesh(generator, 6);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 11, 0.15f, -0.01f, 5, new Color().set(Pal.darkishGray).mul(0.9f).a(0.3f), 2, 0.45f, 0.9f, 0.62f),
                    new HexSkyMesh(this, 1, 0.15f, 0.02f, 5, Color.white.cpy().lerp(Pal.darkishGray, 0.7f).a(0.3f), 2, 0.45f, 1f, 0.59f),
                    new HexSkyMesh(this, 6, 0.6f, 0.11f, 5, Color.white.cpy().lerp(Pal.lightishGray, 0.3f).a(0.5f), 2, 0.45f, 1.3f, 0.41f)
            );
            atmosphereRadOut = 0.3f;
            atmosphereColor = Color.valueOf("4c5a79");
            iconColor = Color.valueOf("304c92");
            updateLighting = false;
            // i kinda want a day-night cycle but it looks weird with the different light color...
            // maybe i should make env renderer idk

            defaultCore = CDStorage.landingPodT0;
            allowLaunchToNumbered = false;
            prebuildBase = false;
            allowLaunchLoadout = true;
            // how the HELL are you losing sectors on asphodel
            clearSectorOnLose = true;

            ruleSetter = r -> {
                r.defaultTeam = CDTeams.coalition;
                // is this even necessary
                r.waveTeam = CDTeams.triage;
                r.deconstructRefundMultiplier = 1f;
                r.unitAmmo = true;

                r.fog = true;
                r.staticFog = true;

                r.ambientLight = Color.valueOf("4f4f5da6");
            };
            campaignRuleDefaults.fog = true;

            unlockedOnLand.add(CDStorage.landingPodT0);

            // WHOOPSY FUCKING DAISY
            CDItems.lemon.shownPlanets.add(this);
        }};
    }
}
