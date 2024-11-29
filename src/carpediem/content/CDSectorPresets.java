package carpediem.content;

import mindustry.type.*;

public class CDSectorPresets {
    public static SectorPreset one, two;

    public static void load() {
        one = new SectorPreset("one", CDPlanets.asphodel, 0) {{
            alwaysUnlocked = true;
            addStartingItems = true;
            captureWave = -1;
        }};

        two = new SectorPreset("two", CDPlanets.asphodel, 2) {{
            captureWave = -1;
        }};
    }
}
