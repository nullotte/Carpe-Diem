package carpediem.ui.fragments;

import arc.*;
import arc.flabel.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.util.*;
import carpediem.graphics.*;
import mindustry.*;
import mindustry.core.GameState.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.blocks.storage.CoreBlock.*;

public class IntroFragment {
    public static final float duration = 10f * 60f, spawnDelay = 60f;

    public float progress;
    public boolean removedLanding;

    public void build(Group parent) {
        parent.fill(Styles.black, t -> {
            t.table(ctable -> {
                ctable.add(new WarningBar()).color(CDColors.coalition).growX().height(24f);
                ctable.row();

                FLabel label = new FLabel(Core.bundle.get("carpe-diem-intro"));
                label.setTypingListener(new FListener() {
                    @Override
                    public void onChar(char ch) {
                        Sounds.chatMessage.play();
                    }
                });

                Events.on(StateChangeEvent.class, e -> {
                    if (t.parent != null) {
                        if (e.to == State.paused) {
                            label.pause();
                        } else if (e.from == State.paused) {
                            label.resume();
                        }
                    }
                });

                ctable.add(label).labelAlign(Align.center).color(CDColors.coalition).pad(20f).style(Styles.techLabel);
                ctable.row();

                ctable.add(new WarningBar()).color(CDColors.coalition).growX().height(24f);
                ctable.row();
            }).grow().center();

            t.update(() -> {
                if (!removedLanding && !Core.settings.getBool("skipcoreanimation")) {
                    Image front = (Image) Core.scene.find(e -> e.parent == Core.scene.root && e instanceof Image && e.touchable == Touchable.disabled && e.fillParent);

                    if (front != null) {
                        front.remove();
                        removedLanding = true;
                    }
                }

                CoreBuild core = Vars.player.team().core();
                if (core != null) {
                    Core.camera.position.set(core);
                }

                if (!Vars.state.isPaused()) {
                    if (Vars.state.isMenu()) {
                        t.remove();
                    } else {
                        progress += Time.delta / duration;

                        if (progress >= 1f) {
                            t.actions(Actions.fadeOut(0.5f), Actions.remove());
                        }
                    }
                }
            });
        });

        Vars.player.deathTimer = Player.deathDelay - (duration + spawnDelay);
    }
}
