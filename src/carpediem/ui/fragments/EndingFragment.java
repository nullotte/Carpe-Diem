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
    public float duration = 5f;

    public PlanetParams planetParams = new PlanetParams() {{
        planet = CDPlanets.asphodel;
    }};

    public void build(Group parent) {
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

        parent.fill(table -> {
            table.add(new Element() {
                @Override
                public void draw() {
                    Sector sector = Vars.state.rules.sector;
                    if (sector == null) return;
                    CarpeDiem.planetRenderer.render(planetParams, cam -> {
                        cam.fov = 2f;
                        sector.planet.lookAt(sector, cam.position);
                        cam.position.setLength(4f).add(CDPlanets.asphodel.position);
                        cam.lookAt(CDPlanets.asphodel.position);
                        //planetParams.camDir.set(Vec3.Z).rotate(Vec3.Y, Time.time / 2f);
                    });
                }
            });

            table.actions(Actions.delay(duration), Actions.fadeOut(0.5f), Actions.remove());
        });
    }
}
