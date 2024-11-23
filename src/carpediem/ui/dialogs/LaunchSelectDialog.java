package carpediem.ui.dialogs;

import arc.func.*;
import arc.struct.*;
import arc.util.*;
import carpediem.ui.dialogs.*;
import carpediem.world.blocks.campaign.LaunchPlatform.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;

public class LaunchSelectDialog extends BaseDialog {
    public LaunchSectorInfoDialog info = new LaunchSectorInfoDialog();

    public LaunchPlatformBuild platform;
    public Cons<Sector> listener;

    public LaunchSelectDialog() {
        super("@launch.select");

        shouldPause = true;
        addCloseButton();
        shown(this::rebuild);
        onResize(this::rebuild);
    }

    public void rebuild() {
        cont.clear();

        Seq<Sector> sectors = Vars.state.rules.sector.planet.sectors.select(platform::canLaunch);

        if (sectors.any()) {
            cont.add("@launch.choose").row();

            sectors.each(sector -> {
                cont.button(b -> {
                    b.top();
                    b.add(sector.name()).color(Pal.accent).row();
                    b.image(Icon.terrain.getRegion()).grow().scaling(Scaling.fit).pad(40f);
                }, () -> info.show(sector, () -> {
                    listener.get(sector);
                    hide();
                })).size(240f);
            });
        } else {
            cont.add("@launch.nosectors");
        }
    }

    public void show(LaunchPlatformBuild platform, Cons<Sector> listener) {
        this.platform = platform;
        this.listener = listener;

        show();
    }
}
