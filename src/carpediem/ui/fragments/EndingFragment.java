package carpediem.ui.fragments;

import arc.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.util.*;
import carpediem.*;
import carpediem.content.*;
import mindustry.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class EndingFragment {
    public float duration = 20f;
    public float moveSpeed = 0.03f;
    public float rotateSpeed = 0.003f;
    public float holdTime = 60f;
    public float rotateWarmupLerpSpeed = 0.005f;
    public float fovLerpSpeed = 0.0007f;

    public Vec3 moveDirection = new Vec3();
    public float holdDuration;
    public float rotateWarmup;

    public PlanetParams planetParams = new PlanetParams() {{
        planet = CDPlanets.asphodel;
    }};

    public void build(Group parent) {
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

        CarpeDiem.planetRenderer.cam.fov = 2f;
        sector.planet.lookAt(sector, CarpeDiem.planetRenderer.cam.position);
        CarpeDiem.planetRenderer.cam.position.setLength(4f).add(sector.planet.position);
        CarpeDiem.planetRenderer.cam.lookAt(sector.planet.position);
        moveDirection.setZero().sub(CarpeDiem.planetRenderer.cam.direction);
        holdDuration = holdTime;
        rotateWarmup = 0f;

        parent.fill(table -> {
            table.add(new Element() {
                @Override
                public void draw() {
                    CarpeDiem.planetRenderer.render(planetParams, cam -> {
                        cam.fov = Mathf.lerpDelta(cam.fov, 60f, fovLerpSpeed);

                        if (holdDuration > 0f) {
                            holdDuration -= Time.delta;
                        } else {
                            rotateWarmup = Mathf.lerpDelta(rotateWarmup, 1f, rotateWarmupLerpSpeed);
                        }

                        moveDirection.slerp(Tmp.v31.set(cam.position).nor(), rotateWarmup * rotateSpeed * Time.delta);

                        cam.position.add(Tmp.v31.set(moveDirection).setLength(moveSpeed * Time.delta));
                        cam.direction.setZero().sub(moveDirection);
                        //planetParams.camDir.set(Vec3.Z).rotate(Vec3.Y, Time.time / 2f);
                    });
                }
            });

            table.actions(Actions.delay(duration), Actions.run(() -> {
                CarpeDiem.campaignComplete.showComplete();
                CarpeDiem.campaignComplete.hidden(table::remove);
            }));
        });
    }
}
