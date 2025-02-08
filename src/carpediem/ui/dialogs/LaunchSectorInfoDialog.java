package carpediem.ui.dialogs;

import arc.*;
import arc.graphics.*;
import arc.util.*;
import carpediem.world.blocks.campaign.LaunchPlatform.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;

public class LaunchSectorInfoDialog extends BaseDialog {
    public LaunchSectorInfoDialog() {
        super("@launch.text");
    }

    public void show(LaunchPlatformBuild platform, SectorPreset sector, Runnable run) {
        cont.clear();
        buttons.clear();

        buttons.defaults().size(210f, 64f);
        buttons.button("@back", Icon.left, this::hide);
        addCloseListener();

        cont.table(t -> {
            t.add(Core.bundle.format("launch.to", sector.localizedName)).row();
            t.add(sector.description).width(720f).pad(30f).wrap().labelAlign(Align.center);
        });

        cont.row();

        cont.table(t -> {
            t.add(Core.bundle.get("launch.with")).row();

            if (platform.payload != null && platform.payload.build.items.any()) {
                int[] i = {0};

                t.table(items -> {
                    platform.payload.build.items.each((item, amount) -> {
                        items.image(item.uiIcon).left().size(Vars.iconSmall);
                        items.add("" + amount).padLeft(2f).left().padRight(4f);

                        if (++i[0] % 8 == 0) {
                            items.row();
                        }
                    });
                });
            } else {
                t.add("@none").color(Color.lightGray);
            }
        });

        buttons.button("@launch.text", Icon.play, () -> {
            run.run();
            hide();
        });

        show();
    }
}
