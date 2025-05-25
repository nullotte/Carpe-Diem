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
    public static void load() {
        Vars.ui.database.shown(CDUI::modifyDatabase);

        Element w2 = Vars.ui.database.getChildren().get(1);
        w2 = ((Group) w2).getChildren().get(0);
        w2 = ((Group) w2).getChildren().get(1);
        TextField search = (TextField) w2;
        search.changed(() -> {
            if (search.isValid()) {
                modifyDatabase();
            }
        });
    }

    public static void showCustomLandInfo(Sector sector) {
        Table t = new Table(Styles.black3);
        t.touchable = Touchable.disabled;
        t.margin(8f);
        t.add(sector.name()).color(Pal.accent).style(Styles.outlineLabel).labelAlign(Align.center);
        t.row();
        t.table(resources -> {
            resources.add("@sectors.resources").color(Color.lightGray).padRight(3f);
            for (UnlockableContent resource : sector.info.resources) {
                resources.image(resource.uiIcon).size(24f).padRight(3f);
            }
        }).center();
        t.update(() -> t.setPosition(Core.graphics.getWidth() / 2f, Core.graphics.getHeight() / 2f, Align.center));
        t.actions(Actions.fadeOut(7f, Interp.pow4In), Actions.remove());
        t.pack();
        t.act(0.1f);
        Core.scene.add(t);
    }

    // yea this is also partially yoinked from FOS . hello slotterleet
    public static void modifyDatabase() {
        Element w = Vars.ui.database.getChildren().get(1);
        w = ((Group) w).getChildren().get(1);
        w = ((Group) w).getChildren().get(0);
        Table all = (Table) w;

        Element w2 = Vars.ui.database.getChildren().get(1);
        w2 = ((Group) w2).getChildren().get(0);
        w2 = ((Group) w2).getChildren().get(1);
        TextField search = (TextField) w2;

        Table tabList = (Table) all.getChildren().get(0);
        for (Element element : tabList.getChildren()) {
            // remove and modify the existing listener, since it rebuilds the ui and prevents the later listeners from firing
            ClickListener listener = (ClickListener) element.getListeners().get(2);
            element.removeListener(listener);

            element.addListener(new ClickListener() {
                {
                    setButton(KeyCode.mouseLeft);
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    listener.clicked(event, x, y);
                    if (!(element instanceof Disableable && ((Disableable) element).isDisabled())) {
                        modifyDatabase();
                    }
                }
            });
        }

        String text = search.getText();
        UnlockableContent tab = Reflect.get(Vars.ui.database, "tab");
        Seq<StatusEffect> array = Vars.content.statusEffects().select(s -> s instanceof Archive && !s.isHidden() && (tab == Planets.sun || tab == CDPlanets.asphodel) && (text.isEmpty() || s.localizedName.toLowerCase().contains(text)));

        if (array.size == 0) return;

        all.add("@content.archives.name").growX().left().color(Pal.accent);
        all.row();
        all.image().growX().pad(5).padLeft(0).padRight(0).height(3).color(Pal.accent);
        all.row();
        all.table(list -> {
            list.left();

            int cols = (int) Mathf.clamp((Core.graphics.getWidth() - Scl.scl(30)) / Scl.scl(32 + 12), 1, 22);
            int count = 0;

            for (int i = 0; i < array.size; i++) {
                UnlockableContent unlock = array.get(i);

                Image image = databaseUnlocked(unlock) ? new Image(new TextureRegionDrawable(unlock.uiIcon), Vars.mobile ? Color.white : Color.lightGray).setScaling(Scaling.fit) : new Image(Icon.lock, Pal.gray);

                list.add(image).size(8 * 4).pad(3);

                ClickListener listener = new ClickListener();
                image.addListener(listener);
                if (!Vars.mobile && databaseUnlocked(unlock)) {
                    image.addListener(new HandCursorListener());
                    image.update(() -> image.color.lerp(!listener.isOver() ? Color.lightGray : Color.white, Mathf.clamp(0.4f * Time.delta)));
                }

                if (databaseUnlocked(unlock)) {
                    image.clicked(() -> {
                        if (Core.input.keyDown(KeyCode.shiftLeft) && Fonts.getUnicode(unlock.name) != 0) {
                            Core.app.setClipboardText((char) Fonts.getUnicode(unlock.name) + "");
                            Vars.ui.showInfoFade("@copied");
                        } else {
                            Vars.ui.content.show(unlock);
                        }
                    });
                    image.addListener(new Tooltip(t -> t.background(Tex.button).add(unlock.localizedName + (Core.settings.getBool("console") ? "\n[gray]" + unlock.name : ""))));
                }

                if ((++count) % cols == 0) {
                    list.row();
                }
            }
        }).growX().left().padBottom(10);
        all.row();
    }

    public static boolean databaseUnlocked(UnlockableContent content) {
        return (!Vars.state.isCampaign() && !Vars.state.isMenu()) || content.unlocked();
    }
}
