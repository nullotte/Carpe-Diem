package carpediem.content;

import arc.util.serialization.*;
import arc.util.serialization.Json.*;
import carpediem.type.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.io.*;
import mindustry.type.*;

public class CDSectorPresets {
    public static SectorPreset one, two;

    public static void load() {
        one = new SectorPreset("one", CDPlanets.asphodel, 5) {{
            alwaysUnlocked = true;
            addStartingItems = true;
            showSectorLandInfo = false;
            captureWave = -1;

            CDPlanets.asphodel.startSector = sector.id;
        }};

        two = new SectorPreset("two", CDPlanets.asphodel, 2) {{
            captureWave = -1;
        }};

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

        for (SectorPreset preset : new SectorPreset[]{one, two}) {
            // dont show it in the map dialogs
            Vars.maps.all().remove(m -> m.file == preset.generator.map.file);

            NonThreateningSector sector = new NonThreateningSector(preset.planet, preset.planet.grid.tiles[preset.sector.id]);
            preset.planet.sectors.set(preset.sector.id, sector);
            preset.sector = sector;

            preset.planet.preset(preset.sector.id, preset);
        }
    }
}
