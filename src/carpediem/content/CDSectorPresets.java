package carpediem.content;

import arc.*;
import mindustry.*;
import mindustry.type.*;

public class CDSectorPresets {
    public static SectorPreset theReserve, forwardOutpost, interference, sanctuary, finalRestingPlace;

    public static void load() {
        theReserve = new SectorPreset("the-reserve", CDPlanets.asphodel, 5) {{
            alwaysUnlocked = true;
            CDPlanets.asphodel.startSector = sector.id;
        }};
        forwardOutpost = new SectorPreset("forward-outpost", CDPlanets.asphodel, 17);
        interference = new SectorPreset("interference", "sector-placeholder", CDPlanets.asphodel, 20);
        sanctuary = new SectorPreset("sanctuary", "sector-placeholder", CDPlanets.asphodel, 24);
        finalRestingPlace = new SectorPreset("final-resting-place", "sector-placeholder", CDPlanets.asphodel, 12);

        for (SectorPreset preset : new SectorPreset[]{theReserve, forwardOutpost, interference, sanctuary, finalRestingPlace}) {
            preset.showSectorLandInfo = false;
            preset.captureWave = -1;

            // dont show it in the map dialogs
            Vars.maps.all().remove(m -> m.file == preset.generator.map.file);

            Sector sector = new Sector(preset.planet, preset.planet.grid.tiles[preset.sector.id]) {
                @Override
                public String displayThreat() {
                    return "[white]" + Core.bundle.get("threat.none");
                }
            };
            preset.planet.sectors.set(preset.sector.id, sector);
            preset.sector = sector;
            preset.planet.preset(preset.sector.id, preset);
        }
    }
}
