package carpediem.world.blocks.campaign;

import arc.*;
import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import carpediem.*;
import carpediem.content.*;
import carpediem.graphics.*;
import carpediem.ui.fragments.*;
import carpediem.world.blocks.campaign.RocketLaunchPad.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.GameState.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;

public class RocketControlCenter extends PayloadBlock {
    public Block requiredBlock;

    public float launchDuration = 160f, chargeDuration = 300f, mergeDuration = 60f;
    public Interp landZoomInterp = Interp.pow4In, chargeZoomInterp = Interp.pow4In;
    public float landZoomFrom = 0.02f, landZoomTo = 1.6f, chargeZoomTo = 2.5f;
    public float dustRadius = 30f, rocketThrusterLength = 48f;
    public float rocketHeatRadius = 80f, rocketHeatScl = 8f, rocketHeatMag = 0.1f, rocketHeatOffset = 0.9f;

    public TextureRegion rocketRegion, rocketThruster1, rocketThruster2;

    public RocketControlCenter(String name) {
        super(name);
        acceptsPayload = true;
        conductivePower = true;
        configurable = true;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.input, StatValues.content(requiredBlock));
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        for (int i = 0; i < 8; i++) {
            Tmp.r1.setCentered(x * Vars.tilesize + offset, y * Vars.tilesize + offset, size * Vars.tilesize);
            Tmp.r1.x += Geometry.d8[i].x * size * Vars.tilesize;
            Tmp.r1.y += Geometry.d8[i].y * size * Vars.tilesize;
            Drawf.dashRect(Pal.accent, Tmp.r1);
        }
    }

    @Override
    public void load() {
        super.load();

        rocketRegion = Core.atlas.find(name + "-rocket");
        rocketThruster1 = Core.atlas.find(name + "-rocket-thruster1");
        rocketThruster2 = Core.atlas.find(name + "-rocket-thruster2");
    }

    public class RocketControlCenterBuild extends PayloadBlockBuild<BuildPayload> implements LaunchAnimator {
        public RocketLaunchPadBuild[] pads = new RocketLaunchPadBuild[8];

        public boolean launching;

        public float cloudSeed;

        @Override
        public void updateTile() {
            super.updateTile();

            moveInPayload();

            for (int i = 0; i < 8; i++) {
                if (pads[i] != null && !pads[i].isValid()) {
                    pads[i] = null;
                }
            }
        }

        public void updatePads() {
            for (int i = 0; i < 8; i++) {
                // invalidate the previous one
                pads[i] = null;

                Point2 p = Geometry.d8[i];
                int checkX = tileX() + (p.x * size), checkY = tileY() + (p.y * size);
                Building build = Vars.world.build(checkX, checkY);
                if (build instanceof RocketLaunchPadBuild pad && (pad.controlCenter == null || pad.controlCenter == this || !pad.controlCenter.isValid()) && build.tileX() == checkX && build.tileY() == checkY) {
                    pad.controlCenter = this;
                    pad.corner = i % 2 != 0;
                    pads[i] = pad;
                }
            }
        }

        @Override
        public void onProximityUpdate() {
            updatePads();
        }

        @Override
        public boolean shouldConsume() {
            return payload != null;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload) {
            return super.acceptPayload(source, payload) && payload.content() == requiredBlock;
        }

        public boolean canLaunchRocket() {
            if (!Vars.state.isCampaign() && !CarpeDiem.debug) return false;

            for (RocketLaunchPadBuild pad : pads) {
                if (pad == null || pad.efficiency != 1f) return false;
            }

            return efficiency == 1f;
        }

        @Override
        public Cursor getCursor() {
            return !canLaunchRocket() ? SystemCursor.arrow : super.getCursor();
        }

        @Override
        public void buildConfiguration(Table table) {
            if (!canLaunchRocket()) {
                deselect();
                return;
            }

            table.button(Icon.play, Styles.cleari, () -> {
                if (canLaunchRocket()) {
                    CarpeDiem.rocketLaunch.show(() -> {
                        Vars.renderer.showLaunch(this);
                        // TODO should clear payloads. also unlock stuff
                    });
                }
                deselect();
            }).size(40f);
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);
            Draw.z(Layer.blockOver);
            if (!launching) {
                drawPayload();
            } else {
                float rawTime = launchDuration() - Vars.renderer.getLandTime();
                if (rawTime > mergeDuration && rawTime < chargeDuration) {
                    Drawf.shadow(x, y, rocketRegion.width * rocketRegion.scl() * 2f);
                    drawRocket(x, y, 1f, 0f, 0f);
                }
            }
        }

        @Override
        public void drawSelect() {
            boolean drewOutline = false;
            for (int i = 0; i < 8; i++) {
                RocketLaunchPadBuild pad = pads[i];
                if (pad != null) {
                    Drawf.selected(pad, Pal.accent);
                    drewOutline = true;
                } else {
                    Tmp.r1.setCentered(x, y, size * Vars.tilesize);
                    Tmp.r1.x += Geometry.d8[i].x * size * Vars.tilesize;
                    Tmp.r1.y += Geometry.d8[i].y * size * Vars.tilesize;
                    Drawf.dashRect(Pal.accent, Tmp.r1);
                }
            }

            if (drewOutline) {
                Drawf.selected(this, Pal.accent);
            }
        }

        public void drawRocket(float x, float y, float scl, float fin, float rotation) {
            if (scl < 0f) {
                scl = landZoomTo;
            }

            float heatSize = rocketHeatRadius * scl * Interp.pow2Out.apply(fin) * (Mathf.absin(Time.globalTime, rocketHeatScl, rocketHeatMag) + rocketHeatOffset);
            Draw.color(Pal.lightTrail);
            Draw.rect("circle-shadow", x, y, heatSize * 4f, heatSize * 4f);
            Draw.color();

            Draw.scl(scl);

            float thrustOpen = 0.1f;
            float thrusterFrame = fin >= thrustOpen ? 1f : fin / thrustOpen;
            float thrusterSize = Interp.pow2Out.apply(Mathf.clamp(fin * 9f));

            // flame
            float strength = (1f + (16) / 2.5f) * scl * thrusterSize * (0.95f + Mathf.absin(Time.globalTime, 2f, 0.1f));
            float offset = (18) * 3f * scl;
            for (int i = 0; i < 4; i++) {
                Tmp.v1.trns(i * 90 + rotation, 1f);

                Tmp.v1.setLength((16 * Vars.tilesize / 2f + 1f) * scl + strength * 2f + offset);
                Draw.color(team.color);
                Fill.circle(Tmp.v1.x + x, Tmp.v1.y + y, 6f * strength);

                Tmp.v1.setLength((16 * Vars.tilesize / 2f + 1f) * scl + strength * 0.5f + offset);
                Draw.color(Color.white);
                Fill.circle(Tmp.v1.x + x, Tmp.v1.y + y, 3.5f * strength);
            }

            drawLandingThrusters(x, y, rotation, thrusterFrame);

            Drawf.spinSprite(rocketRegion, x, y, rotation);

            Draw.scl();
        }

        protected void drawLandingThrusters(float x, float y, float rotation, float frame) {
            float length = rocketThrusterLength * (frame - 1f) - 1f / 4f;
            float alpha = Draw.getColorAlpha();

            //two passes for consistent lighting
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < 4; i++) {
                    var reg = i >= 2 ? rocketThruster2 : rocketThruster1;
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

        public void drawMergingBlock(Block block, float x, float y) {
            float z = Draw.z();
            Draw.z(z - 0.001f);
            Drawf.shadow(x, y, block.size * Vars.tilesize * 2f);
            Draw.z(z);
            Draw.rect(block.fullIcon, x, y);
        }

        @Override
        public void displayConsumption(Table table) {
            super.displayConsumption(table);
            table.table(t -> {
                t.add(new ReqImage(requiredBlock.uiIcon, () -> payload != null)).size(Vars.iconMed);
            });
        }

        @Override
        public void drawLaunch() {
            var clouds = Core.assets.get("sprites/clouds.png", Texture.class);

            float rawFin = Vars.renderer.getLandTimeIn();
            float rawTime = launchDuration() - Vars.renderer.getLandTime();
            float fin = 1f - Mathf.clamp((1f - rawFin) - (chargeDuration / (launchDuration + chargeDuration))) / (1f - (chargeDuration / (launchDuration + chargeDuration)));

            float chargeFin = 1f - Mathf.clamp((1f - rawFin) / (chargeDuration / (launchDuration + chargeDuration)));
            float chargeFout = 1f - chargeFin;

            float cameraScl = Vars.renderer.getDisplayScale();

            float fout = 1f - fin;
            float scl = Scl.scl(4f) / cameraScl;
            float pfin = Interp.pow3Out.apply(fin), pf = Interp.pow2In.apply(fout);

            //draw particles
            Draw.color(Pal.lightTrail);
            Angles.randLenVectors(1, pfin, 100, 800f * scl * pfin, (ax, ay, ffin, ffout) -> {
                Lines.stroke(scl * ffin * pf * 3f);
                Lines.lineAngle(x + ax, y + ay, Mathf.angle(ax, ay), (ffin * 20 + 1f) * scl);
            });
            Draw.color();

            if (rawTime < mergeDuration) {
                drawMergingBlock(requiredBlock, x, y);
                float mergingDistance = requiredBlock.size * Vars.tilesize;
                float mergeLerpAlpha = Interp.pow2In.apply(rawTime / mergeDuration);
                for (int i = 0; i < 8; i++) {
                    RocketLaunchPadBuild pad = pads[i];
                    if (pad != null) {
                        float dx = Geometry.d8[i].x, dy = Geometry.d8[i].y;
                        drawMergingBlock(
                                pad.requiredBlock(),
                                Mathf.lerp(pad.x, x + dx * mergingDistance, mergeLerpAlpha),
                                Mathf.lerp(pad.y, y + dy * mergingDistance, mergeLerpAlpha)
                        );
                    }
                }
            }

            if (rawTime > chargeDuration) {
                drawRocket(x, y, Scl.scl(landZoomTo) / Vars.renderer.getDisplayScale(), fout, Interp.pow2In.apply(fout) * 60f);
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

            Time.run(mergeDuration, () -> {
                CDFx.rocketMerge.at(x, y, 0f, this);
            });

            Time.run(chargeDuration, () -> {
                Effect.shake(10f, 14f, this);
                float spacing = 12f;
                for (int i = 0; i < 13; i++) {
                    int fi = i;
                    Time.run(i * 2f, () -> {
                        float radius = dustRadius + 1 + spacing * fi;
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
            Vars.state.set(State.paused);
            new EndingFragment().build(this, CDSectorPresets.finalRestingPlace.sector);
        }

        @Override
        public void updateLaunch() {
        }

        @Override
        public float launchDuration() {
            return launchDuration + chargeDuration;
        }

        @Override
        public float zoomLaunch() {
            Core.camera.position.set(this);

            float rawTime = launchDuration() - Vars.renderer.getLandTime();
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
