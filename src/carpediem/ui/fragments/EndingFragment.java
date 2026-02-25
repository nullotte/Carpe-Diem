package carpediem.ui.fragments;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.util.*;
import carpediem.*;
import carpediem.content.*;
import carpediem.content.blocks.*;
import carpediem.world.blocks.campaign.RocketControlCenter.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class EndingFragment {
    public float duration = 30f;
    public float moveSpeed = 0.03f;
    public float rotateSpeed = 0.003f;
    public float holdTime = 120f;
    public float rotateWarmupLerpSpeed = 0.002f;
    public float fovLerpSpeed = 0.0007f;
    public Vec3 avoidedPosition = new Vec3(0f, 0f, 40f);

    public Vec3 position = new Vec3();
    public Vec3 moveDirection = new Vec3();
    public float holdDuration;
    public float rotateWarmup;

    public PlanetParams planetParams = new PlanetParams() {{
        planet = CDPlanets.asphodel;
    }};

    public void build(Group parent, RocketControlCenterBuild controlCenterBuild) {
        Sector sector = Vars.state.rules.sector;
        if (sector == null) return;

        Image image = new Image();
        image.color.a = 1f;
        image.touchable = Touchable.disabled;
        image.setFillParent(true);
        image.actions(Actions.fadeOut(35f / 60f), Actions.remove());
        image.update(() -> {
            image.toFront();
            Vars.ui.loadfrag.toFront();
            if (Vars.state.isMenu()) {
                image.remove();
            }
        });
        Core.scene.add(image);

        sector.planet.lookAt(sector, position);
        position.setLength(4f).add(sector.planet.position);

        CarpeDiem.planetRenderer.cam.fov = 2f;
        CarpeDiem.planetRenderer.cam.position.set(position);
        CarpeDiem.planetRenderer.cam.lookAt(sector.planet.position);
        moveDirection.setZero().sub(CarpeDiem.planetRenderer.cam.direction);
        holdDuration = holdTime;
        rotateWarmup = 0f;

        parent.fill(table -> {
            table.add(new Element() {
                @Override
                public void draw() {
                    CarpeDiem.planetRenderer.render(planetParams, cam -> {
                        if (holdDuration > 0f) {
                            holdDuration -= Time.delta;
                        } else {
                            rotateWarmup = Mathf.lerpDelta(rotateWarmup, 1f, rotateWarmupLerpSpeed);
                        }

                        moveDirection.slerp(Tmp.v31.set(position).add(avoidedPosition).nor(), rotateWarmup * rotateSpeed * Time.delta);
                        position.add(Tmp.v31.set(moveDirection).setLength(moveSpeed * Time.delta));

                        cam.fov = Mathf.lerpDelta(cam.fov, 60f, fovLerpSpeed);
                        cam.direction.setZero().sub(moveDirection);
                        cam.position.set(position);

                        return position;
                    });

                    Draw.scl(4f);
                    float x = Core.graphics.getWidth() / 2f;
                    float y = Core.graphics.getHeight() / 2f;

                    controlCenterBuild.drawRocket(x, y);
                    Draw.scl();
                }
            });

            table.actions(Actions.delay(duration), Actions.run(() -> {
                CarpeDiem.campaignComplete.showComplete();
                CarpeDiem.campaignComplete.hidden(table::remove);
            }));
        });
    }
}
