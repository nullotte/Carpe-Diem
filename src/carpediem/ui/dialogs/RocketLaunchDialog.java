package carpediem.ui.dialogs;

import arc.*;
import arc.graphics.*;
import arc.util.*;
import carpediem.content.*;
import carpediem.world.blocks.campaign.LaunchPlatform.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;

public class RocketLaunchDialog extends BaseDialog {
    public RocketLaunchDialog() {
        super("@launch.text");
    }

    public void show(Runnable run) {
        cont.clear();
        buttons.clear();

        buttons.defaults().size(210f, 64f);
        buttons.button("@back", Icon.left, this::hide);
        addCloseListener();

        cont.table(t -> {
            t.table(title -> {
                title.add(Core.bundle.format("launch.to", Core.bundle.get("carpe-diem-outer-space")));
            });
            t.row();
            t.add("@carpe-diem-outer-space-description").width(720f).pad(30f).wrap().labelAlign(Align.center);
        });

        cont.row();

        buttons.button("@launch.text", Icon.play, () -> {
            run.run();
            hide();
        });

        show();
    }
}
