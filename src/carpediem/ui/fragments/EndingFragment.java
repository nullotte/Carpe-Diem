package carpediem.ui.fragments;

import arc.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.util.*;
import carpediem.content.*;
import mindustry.*;
import mindustry.graphics.g3d.*;

public class EndingFragment {
    public PlanetParams planetParams = new PlanetParams() {{
        planet = CDPlanets.asphodel;
        zoom = 0.1f;
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
                    Vars.renderer.planets.render(planetParams);
                }
            });

            table.table(slider -> {
                slider.image(Core.atlas.find("carpe-diem-title")).pad(50f);
                slider.row();
                slider.add("woahhh im going UP!!!");
                slider.row();
                slider.add("extra text lalalala");
                slider.row();
                slider.row();
                slider.add("this is incredibly unfinished!!!! and the mod's not even finished enough to warrant me doing the end screen");
                slider.row();
                slider.add("but im doing it anyways!!!!! because it's vaguely fun!!!!");

                slider.setTranslation(0f, -Core.graphics.getHeight());
                slider.actions(Actions.translateBy(0f, -slider.translation.y * 2f, 30f));
            });

            table.update(() -> {
                planetParams.zoom += Time.delta * 0.01f;
            });

            table.actions(Actions.delay(30f), Actions.fadeOut(0.5f), Actions.remove());
        });
    }
}
