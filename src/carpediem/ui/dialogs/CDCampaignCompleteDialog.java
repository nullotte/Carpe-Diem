package carpediem.ui.dialogs;

import arc.*;
import arc.math.*;
import arc.scene.actions.*;
import carpediem.content.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;

import static arc.scene.actions.Actions.*;

public class CDCampaignCompleteDialog extends BaseDialog {
    public CDCampaignCompleteDialog() {
        super("");

        addCloseListener();
        shouldPause = true;

        buttons.defaults().size(210f, 64f);
        buttons.button("@menu", Icon.left, () -> {
            hide();
            Vars.ui.paused.runExitSave();
        });

        buttons.button("@continue", Icon.ok, this::hide);

        if (false) {
            hidden(() -> Vars.ui.showInfo("@carpe-diem-thank-you-for-playing"));
        }
    }

    public void showComplete() {
        cont.clear();

        cont.add(Core.bundle.get("carpe-diem-campaign-complete")).row();

        float playtime = CDPlanets.asphodel.sectors.sumf(s -> s.hasSave() ? s.save.meta.timePlayed : 0) / 1000f;

        cont.add(Core.bundle.format("campaign.playtime", UI.formatTime(playtime))).left().row();

        setTranslation(0f, -Core.graphics.getHeight());
        color.a = 0f;

        show(Core.scene, Actions.sequence(parallel(fadeIn(1.1f, Interp.fade), translateBy(0f, Core.graphics.getHeight(), 6f, Interp.pow5Out))));
    }
}
