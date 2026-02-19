package carpediem.ui;

import arc.*;
import arc.graphics.*;
import arc.input.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.scene.utils.*;
import arc.struct.*;
import arc.util.*;
import carpediem.content.*;
import carpediem.type.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;

public class CDUI {
    public static void showCustomLandInfo(Sector sector) {
        Table t = new Table(Styles.black3);
        t.touchable = Touchable.disabled;
        t.margin(8f);
        t.add(sector.name()).color(Pal.accent).style(Styles.outlineLabel).labelAlign(Align.center);
        t.row();
        t.table(resources -> {
            resources.add("@sectors.resources").style(Styles.outlineLabel).color(Color.lightGray).padRight(3f);
            for (UnlockableContent resource : sector.info.resources) {
                resources.image(resource.uiIcon).scaling(Scaling.fit).size(24f).padRight(3f);
            }
        }).center();
        t.update(() -> t.setPosition(Core.graphics.getWidth() / 2f, Core.graphics.getHeight() / 2f, Align.center));
        t.actions(Actions.fadeOut(7f, Interp.pow4In), Actions.remove());
        t.pack();
        t.act(0.1f);
        Core.scene.add(t);
    }
}
