package carpediem.ui.fragments;

import arc.*;
import arc.func.*;
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
import carpediem.world.blocks.campaign.RocketControlCenter.*;
import mindustry.*;
import mindustry.core.GameState.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class EndingFragment {
    // configuration
    public float duration = 30f;
    public float moveSpeed = 0.03f;
    public float rotateSpeed = 0.003f;
    public float rocketRotateSpeed = 0.44f;
    public float holdTime = 120f;
    public float rotateWarmupLerpSpeed = 0.002f;
    public float fovLerpSpeed = 0.0007f;
    public Vec3 avoidedPosition = new Vec3(0f, 0f, 40f);

    // state
    public Vec3 position = new Vec3();
    public Vec3 moveDirection = new Vec3();
    public float holdDuration;
    public float rotateWarmup;
    public float rocketRotation = 60f;

    public PlanetParams planetParams = new PlanetParams() {{
        planet = CDPlanets.asphodel;
    }};

    public void build(RocketControlCenterBuild controlCenterBuild, Sector sector) {
        // i don't like this.
        Boolp inputLock = () -> true;
        Vars.control.input.addLock(inputLock);

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

        CarpeDiem.rocketLaunchPlanetRenderer.cam.fov = 2f;
        CarpeDiem.rocketLaunchPlanetRenderer.cam.position.set(position);
        CarpeDiem.rocketLaunchPlanetRenderer.cam.lookAt(sector.planet.position);
        moveDirection.setZero().sub(CarpeDiem.rocketLaunchPlanetRenderer.cam.direction);
        holdDuration = holdTime;
        rotateWarmup = 0f;

        Core.scene.root.fill(table -> {
            table.add(new Element() {
                @Override
                public void draw() {
                    CarpeDiem.rocketLaunchPlanetRenderer.render(planetParams, cam -> {
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

                    rocketRotation += rocketRotateSpeed * Time.delta;
                    float x = Core.graphics.getWidth() / 2f;
                    float y = Core.graphics.getHeight() / 2f;
                    Draw.color();
                    controlCenterBuild.drawRocket(x, y, -1f, 1f, rocketRotation);
                }
            });

            table.actions(Actions.delay(duration), Actions.run(() -> {
                CarpeDiem.campaignComplete.showComplete();
                CarpeDiem.campaignComplete.hidden(() -> {
                    table.remove();
                    Vars.control.input.inputLocks.remove(inputLock);
                    Vars.state.set(State.playing);
                });
            }));

            table.update(() -> {
                if (Vars.state.isMenu()) {
                    table.remove();
                    Vars.control.input.inputLocks.remove(inputLock);
                    return;
                }
                Vars.state.set(State.paused);
            });
        });
    }
}
