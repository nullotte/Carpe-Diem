package carpediem.content;

import arc.util.serialization.*;
import arc.util.serialization.Json.*;
import carpediem.type.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.io.*;
import mindustry.type.*;

public class CDSectorPresets {
    public static SectorPreset theReserve, forwardOutpost, interference, sanctuary, finalRestingPlace;

    public static void load() {
        theReserve = new SectorPreset("the-reserve", CDPlanets.asphodel, 5) {{
            alwaysUnlocked = true;
            CDPlanets.asphodel.startSector = sector.id;
        }};
        forwardOutpost = new SectorPreset("forward-outpost", "sector-placeholder", CDPlanets.asphodel, 17);
        interference = new SectorPreset("interference", "sector-placeholder", CDPlanets.asphodel, 20);
        sanctuary = new SectorPreset("sanctuary", "sector-placeholder", CDPlanets.asphodel, 24);
        finalRestingPlace = new SectorPreset("final-resting-place", "sector-placeholder", CDPlanets.asphodel, 12);

        JsonIO.json.setSerializer(NonThreateningSector.class, new Serializer<>(){
            @Override
            public void write(Json json, NonThreateningSector object, Class knownType){
                json.writeValue(object.planet.name + "-" + object.id);
            }

            @Override
            public NonThreateningSector read(Json json, JsonValue jsonData, Class type){
                String name = jsonData.asString();
                int idx = name.lastIndexOf('-');
                return ((NonThreateningSector) Vars.content.<Planet>getByName(ContentType.planet, name.substring(0, idx)).sectors.get(Integer.parseInt(name.substring(idx + 1))));
            }
        });

        for (SectorPreset preset : new SectorPreset[]{theReserve, forwardOutpost, interference, sanctuary, finalRestingPlace}) {
            preset.showSectorLandInfo = false;
            preset.captureWave = -1;

            // dont show it in the map dialogs
            Vars.maps.all().remove(m -> m.file == preset.generator.map.file);

            NonThreateningSector sector = new NonThreateningSector(preset.planet, preset.planet.grid.tiles[preset.sector.id]);
            preset.planet.sectors.set(preset.sector.id, sector);
            preset.sector = sector;
            preset.planet.preset(preset.sector.id, preset);
        }
    }
}
