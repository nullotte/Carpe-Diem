package carpediem;

import mindustry.*;
import mindustry.mod.*;
import carpediem.content.*;
import carpediem.ui.dialogs.*;

public class CarpeDiem extends Mod {
    public static ArchiveDatabaseDialog archiveDatabase;
    public static LaunchSelectDialog launchSelect;

    @Override
    public void init() {
        archiveDatabase = new ArchiveDatabaseDialog();
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
        CDArchives.load();
    }
}
