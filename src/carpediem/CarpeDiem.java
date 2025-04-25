package carpediem;

import arc.*;
import arc.util.*;
import carpediem.audio.*;
import carpediem.graphics.*;
import carpediem.ui.*;
import carpediem.ui.fragments.*;
import carpediem.world.draw.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import carpediem.content.*;
import carpediem.ui.dialogs.*;
import mindustry.ui.dialogs.*;

public class CarpeDiem extends Mod {
    public static CDHints hints;
    public static LaunchSectorInfoDialog launchSectorInfo;
    // mhm
    public static ContentInfoDialog content;

    public static boolean debug;

    public CarpeDiem() {
        Events.on(MusicRegisterEvent.class, e -> {
            CDMusics.load();
        });

        // hai glenn!
        Events.on(FileTreeInitEvent.class, e -> {
            if (!Vars.headless) {
                Core.app.post(CDShaders::load);
            }
        });

        Events.on(ClientLoadEvent.class, e -> {
            Core.settings.getBoolOnce("carpe-diem-disclaimer", () -> Vars.ui.showInfo("@carpe-diem-disclaimer"));
        });

        debug = Core.settings.getBool("cd-debug");
    }

    @Override
    public void init() {
        hints = new CDHints();
        launchSectorInfo = new LaunchSectorInfoDialog();
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

        // and this is just the Worst
        Events.run(Trigger.update, () -> {
            if (Vars.player.unit() != null) {
                if (Vars.player.unit().item() == CDItems.sulfur && !Vars.net.client()) {
                    CDItems.sulfur.unlock();
                }
            }
        });

        // hi slotterleet !
        Events.run(Trigger.newGame, () -> {
            if (Vars.state.rules.sector != null && Vars.state.rules.sector == CDSectorPresets.theReserve.sector) {
                Reflect.set(Vars.renderer, "landTime", 0f);
                Musics.launch.stop();
                CDMusics.land.stop();
                new IntroFragment().build(Core.scene.root);
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
