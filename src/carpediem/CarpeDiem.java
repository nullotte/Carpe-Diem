package carpediem;

import carpediem.ui.*;
import mindustry.*;
import mindustry.mod.*;
import carpediem.content.*;

public class CarpeDiem extends Mod {
    public static LaunchSelectDialog launchSelect;

    @Override
    public void init() {
        launchSelect = new LaunchSelectDialog();

        // this is probably bad
        Vars.ui.planet.update(() -> {
            CDPlanets.planet.sectors.each(sector -> {
                if (sector.preset != null && !sector.hasBase()) {
                    sector.preset.clearUnlock();
                }
            });
        });
    }

    @Override
    public void loadContent() {
        CDItems.load();
        CDLiquids.load();
        CDUnitTypes.load();
        CDBlocks.load();
        CDPlanets.load();
        CDSectorPresets.load();
        CDTechTree.load();
    }
}
