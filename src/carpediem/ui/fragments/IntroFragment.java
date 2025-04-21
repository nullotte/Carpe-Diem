package carpediem.ui.fragments;

import arc.*;
import arc.flabel.*;
import arc.graphics.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.util.*;
import carpediem.graphics.*;
import carpediem.ui.*;
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
            Core.app.post(() -> {
                boolean[] actionedc = {false};
                t.table(ctable -> {
                    MovingWarningBar bar1 = ctable.add(new MovingWarningBar()).color(CDColors.coalition).growX().height(24f).get();
                    bar1.speed = 50f;
                    bar1.setTranslation(0f, 50f);
                    bar1.addAction(Actions.translateBy(0f, -50f, 1f, Interp.pow3Out));
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

                    MovingWarningBar bar2 = ctable.add(new MovingWarningBar()).color(CDColors.coalition).growX().height(24f).get();
                    bar2.speed = -50f;
                    bar2.setTranslation(0f, -50f);
                    bar2.addAction(Actions.translateBy(0f, 50f, 1f, Interp.pow3Out));
                    ctable.row();
                    ctable.update(() -> {
                        if (progress >= 1f && !actionedc[0]) {
                            ctable.addAction(Actions.translateBy(0f, Core.graphics.getHeight() / 16f, 0.5f, Interp.pow2In));

                            for (Element element : new Element[]{bar1, bar2, label}) {
                                element.setColor(Color.white);
                                element.addAction(Actions.color(CDColors.coalition, 0.5f));
                            }

                            actionedc[0] = true;
                        }
                    });
                }).grow().center();
            });

            boolean[] actionedt = {false};
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

                        if (progress >= 1f && !actionedt[0]) {
                            t.actions(Actions.fadeOut(0.5f), Actions.remove());
                            actionedt[0] = true;
                        }
                    }
                }
            });
        });

        Vars.player.deathTimer = Player.deathDelay - (duration + spawnDelay);
    }
}
