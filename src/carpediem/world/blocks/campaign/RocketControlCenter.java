package carpediem.world.blocks.campaign;

import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import carpediem.*;
import carpediem.content.blocks.*;
import carpediem.world.blocks.campaign.RocketLaunchPad.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.meta.*;

public class RocketControlCenter extends PayloadBlock {
    public Block requiredBlock;

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

    public class RocketControlCenterBuild extends PayloadBlockBuild<BuildPayload> {
        public RocketLaunchPadBuild[] pads = new RocketLaunchPadBuild[8];

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
            if (!Vars.state.isCampaign()) return false;

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
                        //
                    });
                }
                deselect();
            }).size(40f);
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);

            //draw input
            for (int i = 0; i < 4; i++) {
                if (blends(i)) {
                    Draw.rect(inRegion, x, y, (i * 90f) - 180f);
                }
            }

            Draw.z(Layer.blockOver);
            drawPayload();
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

        public void drawRocket(float x, float y) {
            Draw.rect(CDStorage.landingPodT2.fullIcon, x, y);
        }

        @Override
        public void displayConsumption(Table table) {
            super.displayConsumption(table);
            table.table(t -> {
                t.add(new ReqImage(requiredBlock.uiIcon, () -> payload != null)).size(Vars.iconMed);
            });
        }
    }
}
