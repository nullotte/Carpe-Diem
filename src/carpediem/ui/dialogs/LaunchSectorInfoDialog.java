package carpediem.ui.dialogs;

import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;

public class LaunchSectorInfoDialog extends BaseDialog {
    public LaunchSectorInfoDialog() {
        super("@launch.sectorinfo");
    }

    public void show(Sector sector, Runnable run) {
        cont.clear();
        buttons.clear();

        buttons.defaults().size(210f, 64f);
        buttons.button("@back", Icon.left, this::hide);
        addCloseListener();

        cont.add(sector.name()).color(Pal.accent).row();

        if (sector.preset != null) {
            cont.pane(p -> {
                p.add(sector.preset.description).grow().wrap().labelAlign(Align.center);
            }).pad(10f).grow();
        }

        buttons.button("@launch.text", Icon.play, () -> {
            run.run();
            hide();
        });

        show();
    }
}
