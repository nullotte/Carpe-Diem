package carpediem;

import arc.*;
import carpediem.world.blocks.storage.*;
import carpediem.world.draw.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import carpediem.content.*;
import carpediem.ui.dialogs.*;
import mindustry.ui.dialogs.*;

public class CarpeDiem extends Mod {
    public static ArchiveDatabaseDialog archiveDatabase;
    public static LaunchSelectDialog launchSelect;
    // mhm
    public static ContentInfoDialog content;

    @Override
    public void init() {
        archiveDatabase = new ArchiveDatabaseDialog();
        launchSelect = new LaunchSelectDialog();
        content = new ContentInfoDialog();

        // this is probably bad
        Vars.ui.planet.update(() -> {
            CDPlanets.asphodel.sectors.each(sector -> {
                if (sector.preset != null && !sector.hasBase()) {
                    sector.preset.clearUnlock();
                }
            });
        });

        // this is worse
        Events.run(Trigger.draw, () -> {
            DrawItemSlot.currentDrawn = null;
        });

        // and this is the worst
        Events.run(Trigger.preDraw, () -> {
            if (Vars.renderer.isLaunching() && Vars.renderer.getLaunchCoreType() instanceof LandingPod) {
                Core.camera.position.set(LandingPod.launchBuild);
            }
        });
    }

    @Override
    public void loadContent() {
        CDStatusEffects.load();
        CDTeams.load();
        CDItems.load();
        CDLiquids.load();
        CDRecipes.load();
        CDUnitTypes.load();
        CDBlocks.load();
        CDLoadouts.load();
        CDPlanets.load();
        CDSectorPresets.load();
        CDArchives.load();
        CDTechTree.load();
    }
}
