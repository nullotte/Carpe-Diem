package carpediem.world.blocks.campaign;

import arc.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import carpediem.*;
import carpediem.game.CDObjectives.*;
import carpediem.world.blocks.storage.*;
import mindustry.*;
import mindustry.content.TechTree.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.payloads.*;

// TODO should be able to launch item pods
public class LaunchPlatform extends PayloadBlock {
    public LaunchPlatform(String name) {
        super(name);
        acceptsPayload = true;
        configurable = true;

        config(Sector.class, LaunchPlatformBuild::launchTo);
    }

    public class LaunchPlatformBuild extends PayloadBlockBuild<BuildPayload> {
        @Override
        public void updateTile() {
            super.updateTile();

            moveInPayload();
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload) {
            return super.acceptPayload(source, payload) && payload.content() instanceof PackagedCoreBlock;
        }

        // TODO IMPORTANT !!!!! let the player's loadout be the items they loaded the landing pod with
        public void launchTo(Sector sector) {
            if (canLaunch(sector)) {
                consume();
                BuildPayload launched = payload;
                payload = null;

                if (Core.settings.getBool("skipcoreanimation")) {
                    // yeah
                    Vars.control.playSector(Vars.state.rules.sector, sector);
                } else {
                    Time.runTask(5f, () -> {
                        Vars.renderer.showLaunch(((PackagedCoreBlock) launched.content()).coreType);
                        Time.runTask(Vars.coreLandDuration - 8f, () -> Vars.control.playSector(Vars.state.rules.sector, sector));
                    });
                }
            }
        }

        // im crying
        public boolean canLaunch(Sector sector) {
            if (sector != null) {
                if (sector.hasBase()) return false;

                if (sector.planet.generator != null && !sector.planet.generator.allowLanding(sector)) {
                    if (sector.preset != null) {
                        TechNode node = sector.preset.techNode;
                        if (node != null && node.parent != null && (!node.parent.content.unlocked() || (node.parent.content instanceof SectorPreset preset && !preset.sector.hasBase()) || node.objectives.contains(o -> !(o.complete() || (o instanceof LaunchSector launch && payload.content() == launch.requiredCore))))) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }

            return Vars.state.isCampaign() && !Vars.net.client() && payload != null && hasArrived() && efficiency > 0f;
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);

            for (int i = 0; i < 4; i++) {
                if (blends(i)) {
                    Draw.rect(inRegion, x, y, (i * 90) - 180);
                }
            }

            Draw.z(Layer.blockOver);
            drawPayload();
        }

        @Override
        public void buildConfiguration(Table table) {
            if (!canLaunch(null)) {
                deselect();
                return;
            }

            table.button(Icon.play, Styles.cleari, () -> {
                CarpeDiem.launchSelect.show(this, this::configure);
                deselect();
            }).size(40f);
        }
    }
}
