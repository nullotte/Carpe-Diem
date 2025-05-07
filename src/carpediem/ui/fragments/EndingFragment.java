package carpediem.ui.fragments;

import arc.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.util.*;
import carpediem.content.*;
import carpediem.content.blocks.*;
import mindustry.*;
import mindustry.graphics.g3d.*;
import mindustry.world.blocks.storage.*;

public class EndingFragment {
    public PlanetParams planetParams = new PlanetParams() {{
        planet = CDPlanets.asphodel;
        zoom = 0.04f;
    }};

    public void build(Group parent) {
        CoreBlock coreBlock = CDStorage.landingPodT0;

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

            table.update(() -> {
                //planetParams.zoom += Time.delta * 0.01f;
            });

            table.actions(Actions.delay(10f), Actions.fadeOut(0.5f), Actions.remove());
        });
    }
}
