package carpediem.world.blocks.campaign;

import arc.*;
import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import carpediem.*;
import carpediem.audio.*;
import carpediem.content.*;
import carpediem.game.CDObjectives.*;
import carpediem.world.blocks.storage.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.entities.*;
import mindustry.game.Objectives.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

// good god
public class LaunchPlatform extends PayloadBlock {
    // these values are copied straight from the accelerator . do these need tweaking at all ? hell if i know
    public float launchDuration = 160f, chargeDuration = 320f;
    public Interp landZoomInterp = Interp.pow4In, chargeZoomInterp = Interp.pow4In;
    public float landZoomFrom = 0.02f, landZoomTo = 4f, chargeZoomTo = 5f;

    public TextureRegion[] tops;
    public Interp extendInterp = Interp.pow2Out;
    public float openLength = 8f, extendTime = 160f;

    public Sound chargeSound = CDSounds.launchPlatformCharge;

    public ObjectMap<CoreBlock, ItemStack[]> launchItemRequirementMap = new ObjectMap<>();
    public ObjectFloatMap<CoreBlock> launchPowerRequirementMap = new ObjectFloatMap<>();

    public LaunchPlatform(String name) {
        super(name);
        acceptsPayload = true;
        configurable = true;
        emitLight = true;
        lightRadius = 120f;

        consume(new ConsumeItemDynamic((LaunchPlatformBuild build) -> {
            CoreBlock requiredType = build.requiredType();
            if (requiredType != null) {
                return launchItemRequirementMap.get(requiredType, ItemStack.empty);
            } else {
                return ItemStack.empty;
            }
        }));
        consumePowerDynamic((LaunchPlatformBuild build) -> launchPowerRequirementMap.get(build.requiredType(), 0f));
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.remove(Stat.itemCapacity);
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

    public class LaunchPlatformBuild extends PayloadBlockBuild<BuildPayload> implements LaunchAnimator {
        public boolean launching;
        public float launchTime;
        public Block launchBlock;
        public float launchHeat;

        public float cloudSeed;

        @Override
        public void updateTile() {
            super.updateTile();

            launchHeat = Mathf.lerpDelta(launchHeat, launching ? 1f : 0f, 0.05f);

            moveInPayload();
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public int getMaximumAccepted(Item item) {
            CoreBlock requiredType = requiredType();
            if (requiredType == null) return 0;
            for (ItemStack stack : launchItemRequirementMap.get(requiredType, ItemStack.empty)) {
                if (stack.item == item) return stack.amount * 2;
            }
            return 0;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload) {
            CoreBlock requiredType = requiredType();

            return super.acceptPayload(source, payload) && requiredType != null && payload.content() instanceof PackagedCoreBlock core && core.coreType == requiredType;
        }

        public SectorPreset destination() {
            if (Vars.state.rules.sector == null) return null;
            SectorPreset preset = Vars.state.rules.sector.preset;

            if (preset != null) {
                TechNode node = preset.techNode.children.find(n -> n.content instanceof SectorPreset);

                if (node != null && node.content instanceof SectorPreset destination && !destination.sector.hasBase()) {
                    return destination;
                }
            }

            return null;
        }

        public CoreBlock requiredType() {
            SectorPreset destination = destination();
            if (destination != null) {
                TechNode node = destination.techNode;

                for (Objective objective : node.objectives) {
                    if (objective instanceof LaunchSector launch) {
                        return launch.requiredCore;
                    }
                }
            }

            return null;
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
            if (launchBlock == null) {
                drawPayload();
            } else {
                Draw.rect(launchBlock.fullIcon, x, y);
            }

            Draw.z(Layer.blockOver + 0.1f);
            float progress = extendInterp.apply(Mathf.clamp(launchTime / extendTime));
            for (int i = 0; i < 4; i++) {
                Draw.rect(tops[i], x + Geometry.d8edge[i].x * progress * openLength, y + Geometry.d8edge[i].y * progress * openLength);
            }
        }

        @Override
        public void drawLight() {
            Drawf.light(x, y, lightRadius, lightColor, launchHeat);
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
                    if (!CarpeDiem.debug && Vars.state.rules.sector == CDSectorPresets.theReserve.sector) {
                        Vars.ui.showInfo("@carpe-diem-end");
                        return;
                    }

                    SectorPreset destination = destination();
                    if (destination != null && !destination.sector.hasBase()) {
                        CarpeDiem.launchSectorInfo.show(this, destination, () -> {
                            if (Vars.state.isCampaign() && efficiency > 0f) {
                                consume();
                                BuildPayload launched = payload;
                                payload = null;

                                Time.runTask(5f, () -> {
                                    launchBlock = ((PackagedCoreBlock) launched.block()).coreType;
                                    Vars.renderer.showLaunch(this);
                                    Time.runTask(launchDuration() - 6f, () -> {
                                        // add resources
                                        ItemSeq resources = new ItemSeq();
                                        launched.build.items.each(resources::add);
                                        Vars.universe.updateLaunchResources(resources);
                                        Vars.control.playSector(Vars.state.rules.sector, destination.sector);
                                    });
                                });
                            }
                        });
                    }
                }
                deselect();
            }).size(40f);
        }

        public void drawLanding(float fin, float x, float y) {
            float rawTime = launchDuration() - Vars.renderer.getLandTime();
            float fout = 1f - fin;

            float scl = rawTime < chargeDuration ? 1f : Scl.scl(4f) / Vars.renderer.getDisplayScale();
            float shake = 0f;
            float s = launchBlock.region.width * launchBlock.region.scl() * scl * 3.6f * Interp.pow2Out.apply(fout);
            float rotation = Interp.pow2In.apply(fout) * 135f;
            x += Mathf.range(shake);
            y += Mathf.range(shake);
            float thrustOpen = 0.25f;
            float thrusterFrame = fin >= thrustOpen ? 1f : fin / thrustOpen;
            float thrusterSize = Mathf.sample(CoreBlock.thrusterSizes, fin);

            //when launching, thrusters stay out the entire time.
            if (Vars.renderer.isLaunching()) {
                Interp i = Interp.pow2Out;
                thrusterFrame = i.apply(Mathf.clamp(fout * 13f));
                thrusterSize = i.apply(Mathf.clamp(fout * 9f));
            }

            Draw.color(Pal.lightTrail);
            Draw.rect("circle-shadow", x, y, s, s);

            Draw.scl(scl);

            //draw thruster flame
            float strength = (1f + (launchBlock.size - 3) / 2.5f) * scl * thrusterSize * (0.95f + Mathf.absin(2f, 0.1f));
            float offset = (launchBlock.size - 3) * 3f * scl;

            for (int i = 0; i < 4; i++) {
                Tmp.v1.trns(i * 90 + rotation, 1f);

                Tmp.v1.setLength((launchBlock.size * Vars.tilesize / 2f + 1f) * scl + strength * 2f + offset);
                Draw.color(team.color);
                Fill.circle(Tmp.v1.x + x, Tmp.v1.y + y, 6f * strength);

                Tmp.v1.setLength((launchBlock.size * Vars.tilesize / 2f + 1f) * scl + strength * 0.5f + offset);
                Draw.color(Color.white);
                Fill.circle(Tmp.v1.x + x, Tmp.v1.y + y, 3.5f * strength);
            }

            drawLandingThrusters(x, y, rotation, thrusterFrame);

            Draw.rect(launchBlock.fullIcon, x, y, rotation);
            Drawf.spinSprite(launchBlock.region, x, y, rotation);

            Draw.alpha(Interp.pow4In.apply(thrusterFrame));
            drawLandingThrusters(x, y, rotation, thrusterFrame);
            Draw.alpha(1f);

            if (launchBlock.teamRegions[team.id] == launchBlock.teamRegion) Draw.color(team.color);

            Drawf.spinSprite(launchBlock.teamRegions[team.id], x, y, rotation);

            Draw.color();
            Draw.scl();
            Draw.reset();
        }

        public void drawLandingThrusters(float x, float y, float rotation, float frame) {
            CoreBlock core = (CoreBlock) launchBlock;
            float length = core.thrusterLength * (frame - 1f) - 1f / 4f;
            float alpha = Draw.getColorAlpha();

            //two passes for consistent lighting
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < 4; i++) {
                    var reg = i >= 2 ? core.thruster2 : core.thruster1;
                    float rot = (i * 90) + rotation % 90f;
                    Tmp.v1.trns(rot, length * Draw.xscl);

                    //second pass applies extra layer of shading
                    if (j == 1) {
                        Tmp.v1.rotate(-90f);
                        Draw.alpha((rotation % 90f) / 90f * alpha);
                        rot -= 90f;
                        Draw.rect(reg, x + Tmp.v1.x, y + Tmp.v1.y, rot);
                    } else {
                        Draw.alpha(alpha);
                        Draw.rect(reg, x + Tmp.v1.x, y + Tmp.v1.y, rot);
                    }
                }
            }
            Draw.alpha(1f);
        }

        @Override
        public void drawLaunch() {
            Texture clouds = Core.assets.get("sprites/clouds.png", Texture.class);

            float rawFin = Vars.renderer.getLandTimeIn();
            float rawTime = launchDuration() - Vars.renderer.getLandTime();
            float fin = 1f - Mathf.clamp((1f - rawFin) - (chargeDuration / (launchDuration + chargeDuration))) / (1f - (chargeDuration / (launchDuration + chargeDuration)));

            //float chargeFin = 1f - Mathf.clamp((1f - rawFin) / (chargeDuration / (launchDuration + chargeDuration)));
            //float chargeFout = 1f - chargeFin;

            float cameraScl = Vars.renderer.getDisplayScale();

            float fout = 1f - fin;
            float scl = Scl.scl(4f) / cameraScl;
            float pfin = Interp.pow3Out.apply(fin), pf = Interp.pow2In.apply(fout);

            //draw particles
            Draw.color(Pal.lightTrail);
            Angles.randLenVectors(1, pfin, 100, 800f * scl * pfin, (ax, ay, ffin, ffout) -> {
                Lines.stroke(scl * ffin * pf * 3f);
                Lines.lineAngle(x + ax, y + ay, Mathf.angle(ax, ay), (ffin * 20f + 1f) * scl);
            });
            Draw.color();

            if (rawTime >= chargeDuration) {
                drawLanding(fin, x, y);
            }

            Draw.color();
            Draw.mixcol(Color.white, Interp.pow5In.apply(fout));

            //draw clouds
            if (Vars.state.rules.cloudColor.a > 0.0001f) {
                float scaling = CoreBlock.cloudScaling;
                float sscl = Math.max(1f + Mathf.clamp(fin + CoreBlock.cfinOffset) * CoreBlock.cfinScl, 0f) * cameraScl;

                Tmp.tr1.set(clouds);
                Tmp.tr1.set(
                        (Core.camera.position.x - Core.camera.width / 2f * sscl) / scaling,
                        (Core.camera.position.y - Core.camera.height / 2f * sscl) / scaling,
                        (Core.camera.position.x + Core.camera.width / 2f * sscl) / scaling,
                        (Core.camera.position.y + Core.camera.height / 2f * sscl) / scaling);

                Tmp.tr1.scroll(10f * cloudSeed, 10f * cloudSeed);

                Draw.alpha(Mathf.sample(CoreBlock.cloudAlphas, fin + CoreBlock.calphaFinOffset) * CoreBlock.cloudAlpha);
                Draw.mixcol(Vars.state.rules.cloudColor, Vars.state.rules.cloudColor.a);
                Draw.rect(Tmp.tr1, Core.camera.position.x, Core.camera.position.y, Core.camera.width, Core.camera.height);
                Draw.reset();
            }
        }

        @Override
        public void beginLaunch(boolean launching) {
            if (!launching) return;

            this.launching = true;

            cloudSeed = Mathf.random(1f);
            float margin = 30f;

            Image image = new Image();
            image.color.a = 0f;
            image.touchable = Touchable.disabled;
            image.setFillParent(true);
            image.actions(Actions.delay((launchDuration() - margin) / 60f), Actions.fadeIn(margin / 60f, Interp.pow2In), Actions.delay(6f / 60f), Actions.remove());
            image.update(() -> {
                image.toFront();
                Vars.ui.loadfrag.toFront();
                if (Vars.state.isMenu()) {
                    image.remove();
                }
            });
            Core.scene.add(image);
            chargeSound.at(this);

            Time.run(chargeDuration, () -> {
                Sounds.coreLaunch.at(Core.camera.position, 1f, 1f);

                Effect.shake(10f, 14f, this);

                float spacing = 12f;
                for (int i = 0; i < 13; i++) {
                    int fi = i;
                    Time.run(i * 1.1f, () -> {
                        float radius = block.size / 2f + 1 + spacing * fi;
                        int rays = Mathf.ceil(radius * Mathf.PI * 2f / 6f);
                        for (int r = 0; r < rays; r++) {
                            if (Mathf.chance(0.7f - fi * 0.02f)) {
                                float angle = r * 360f / (float) rays;
                                float ox = Angles.trnsx(angle, radius), oy = Angles.trnsy(angle, radius);
                                Tile t = Vars.world.tileWorld(x + ox, y + oy);
                                if (t != null) {
                                    Fx.coreLandDust.at(t.worldx(), t.worldy(), angle + Mathf.range(30f), Tmp.c1.set(t.floor().mapColor).mul(1.7f + Mathf.range(0.15f)));
                                }
                            }
                        }
                    });
                }
            });
        }

        @Override
        public void endLaunch() {
            launching = false;
            launchTime = 0f;
        }

        @Override
        public void updateLaunch() {
            float in = Vars.renderer.getLandTimeIn() * launchDuration();
            launchTime = launchDuration() - in;
        }

        @Override
        public float launchDuration() {
            return launchDuration + chargeDuration;
        }

        // TODO launch music...?

        @Override
        public float zoomLaunch() {
            float rawTime = launchDuration() - Vars.renderer.getLandTime();
            float shake = rawTime < chargeDuration ? Interp.pow10In.apply(Mathf.clamp(rawTime / chargeDuration)) : 0f;

            Core.camera.position.set(x, y).add(Tmp.v1.setToRandomDirection().scl(shake * 2f));

            if (rawTime < chargeDuration) {
                float fin = rawTime / chargeDuration;

                return chargeZoomInterp.apply(Scl.scl(landZoomTo), Scl.scl(chargeZoomTo), fin);
            } else {
                float rawFin = Vars.renderer.getLandTimeIn();
                float fin = 1f - Mathf.clamp((1f - rawFin) - (chargeDuration / (launchDuration + chargeDuration))) / (1f - (chargeDuration / (launchDuration + chargeDuration)));

                return landZoomInterp.apply(Scl.scl(landZoomFrom), Scl.scl(landZoomTo), fin);
            }
        }
    }
}
