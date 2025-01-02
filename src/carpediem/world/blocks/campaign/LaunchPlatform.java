package carpediem.world.blocks.campaign;

import arc.*;
import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import carpediem.*;
import carpediem.game.CDObjectives.*;
import carpediem.type.*;
import carpediem.world.blocks.storage.*;
import mindustry.*;
import mindustry.content.TechTree.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.payloads.*;

public class LaunchPlatform extends PayloadBlock {
    public TextureRegion[] tops;
    public float openLength = 6f;

    public LaunchPlatform(String name) {
        super(name);
        acceptsPayload = true;
        configurable = true;

        config(Sector.class, LaunchPlatformBuild::launchTo);
        // PFFT
        config(NonThreateningSector.class, LaunchPlatformBuild::launchTo);
    }

    @Override
    public void load() {
        super.load();

        tops = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            tops[i] = Core.atlas.find(name + "-top" + i);
        }
    }

    @Override
    protected TextureRegion[] icons() {
        // shit
        return new TextureRegion[]{region, tops[0], tops[1], tops[2], tops[3]};
    }

    public class LaunchPlatformBuild extends PayloadBlockBuild<BuildPayload> {
        public float progress;

        @Override
        public void updateTile() {
            super.updateTile();

            moveInPayload();

            // eh.
            progress = Mathf.lerpDelta(progress, efficiency > 0f ? 1f : 0f, 0.05f);
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload) {
            return super.acceptPayload(source, payload) && payload.content() instanceof PackagedCoreBlock;
        }

        public void launchTo(Sector sector) {
            if (Vars.state.isCampaign() && efficiency > 0f && canLaunch(sector)) {
                consume();
                BuildPayload launched = payload;
                payload = null;

                if (!Vars.net.client()) {
                    // add resources
                    ItemSeq resources = new ItemSeq();
                    launched.build.items.each(resources::add);
                    Vars.universe.updateLaunchResources(resources);

                    if (Core.settings.getBool("skipcoreanimation")) {
                        // yeah
                        Vars.control.playSector(Vars.state.rules.sector, sector);
                    } else {
                        Time.runTask(5f, () -> {
                            // TODO it shows the construct effect but whateverrr we cant do anything about that just wait for v8 JAHKJLHJKFGHJKGFKLGHJKJKF
                            LandingPod.launchBuild = this;
                            Vars.renderer.showLaunch(((PackagedCoreBlock) launched.content()).coreType);
                            Time.runTask(Vars.coreLandDuration - 8f, () -> Vars.control.playSector(Vars.state.rules.sector, sector));
                        });
                    }
                }
            }
        }

        public boolean canLaunch(Sector sector) {
            if (sector.hasBase()) return false;

            if (sector.planet.generator != null && !sector.planet.generator.allowLanding(sector)) {
                if (sector.preset != null) {
                    TechNode node = sector.preset.techNode;
                    return node == null || node.parent == null || (node.parent.content.unlocked() && (!(node.parent.content instanceof SectorPreset preset) || preset.sector.hasBase()) && !node.objectives.contains(o -> !(o.complete() || (o instanceof LaunchSector launch && payload.content() instanceof PackagedCoreBlock packaged && packaged.coreType == launch.requiredCore))));
                } else {
                    return false;
                }
            }

            return true;
        }

        @Override
        public boolean shouldConsume() {
            return payload != null;
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

            Draw.z(Layer.blockOver + 0.1f);
            for (int i = 0; i < 4; i++) {
                Draw.rect(tops[i], x + Geometry.d8edge[i].x * progress * openLength, y + Geometry.d8edge[i].y * progress * openLength);
            }
        }

        @Override
        public Cursor getCursor() {
            return !(Vars.state.isCampaign() && efficiency > 0f) ? SystemCursor.arrow : super.getCursor();
        }

        @Override
        public void buildConfiguration(Table table) {
            if (!(Vars.state.isCampaign() && efficiency > 0f)) {
                deselect();
                return;
            }

            table.button(Icon.play, Styles.cleari, () -> {
                if (Vars.state.isCampaign() && efficiency > 0f) {
                    CarpeDiem.launchSelect.show(this, this::configure);
                }
                deselect();
            }).size(40f);
        }
    }
}
