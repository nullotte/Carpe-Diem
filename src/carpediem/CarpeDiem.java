package carpediem;

import arc.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import carpediem.audio.*;
import carpediem.content.blocks.*;
import carpediem.graphics.*;
import carpediem.type.*;
import carpediem.ui.*;
import carpediem.ui.fragments.*;
import carpediem.world.blocks.campaign.RocketControlCenter.*;
import carpediem.world.draw.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.mod.*;
import carpediem.content.*;
import carpediem.ui.dialogs.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class CarpeDiem extends Mod {
    public static CDHints hints;
    public static LaunchSectorInfoDialog launchSectorInfo;
    public static CDCampaignCompleteDialog campaignComplete;
    // it's an extra content info dialog because like. nesting and stuff
    public static ContentInfoDialog content;

    public static CableGlowRenderer cableGlowRenderer;
    public static CDPlanetRenderer planetRenderer;

    public static boolean debug;

    public CarpeDiem() {
        Events.on(MusicRegisterEvent.class, e -> {
            CDMusics.load();
        });

        // hai glenn!
        Events.on(FileTreeInitEvent.class, e -> {
            CDSounds.load();
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
        campaignComplete = new CDCampaignCompleteDialog();
        content = new ContentInfoDialog();

        cableGlowRenderer = new CableGlowRenderer();
        planetRenderer = new CDPlanetRenderer();

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
                Sounds.coreLand.stop();
                new IntroFragment().build(Core.scene.root);
            }
        });

        Vars.renderer.addEnvRenderer(Env.none, () -> {
            Draw.draw(Layer.power + 0.02f, cableGlowRenderer::draw);
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

        // hide unfinished content
        if (!debug) {
            CDUnitTypes.myriad.hidden = true;

            for (SectorPreset preset : new SectorPreset[]{
                    CDSectorPresets.interference,
                    CDSectorPresets.sanctuary,
                    CDSectorPresets.finalRestingPlace
            }) {
                preset.description = null;
            }
        }
    }

    public void testEnding() {
        if (Vars.player.buildOn() instanceof RocketControlCenterBuild controlCenterBuild) {
            new EndingFragment().build(Core.scene.root, controlCenterBuild);
        }
    }
}
