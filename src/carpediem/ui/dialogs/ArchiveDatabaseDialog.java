package carpediem.ui.dialogs;

import arc.*;
import arc.func.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import carpediem.type.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.dialogs.*;

public class ArchiveDatabaseDialog extends BaseDialog {
    public TextField search;
    public Table all = new Table();
    public Cons<Archive> cons;

    // ahahah
    public ContentInfoDialog archiveInfo;

    public ArchiveDatabaseDialog() {
        super("@archives.database");
        archiveInfo = new ContentInfoDialog();

        addCloseButton();
        shown(this::rebuild);
        onResize(this::rebuild);

        all.margin(20).marginTop(0f);

        cont.top();
        cont.table(s -> {
            s.image(Icon.zoom).padRight(8);
            search = s.field(null, text -> rebuild()).growX().get();
            search.setMessageText("@players.search");
        }).fillX().padBottom(4).row();
        cont.pane(all).scrollX(false);

        Vars.ui.database.buttons.button("@archives.view", Icon.bookOpen, this::show);
    }

    public void rebuild() {
        all.clear();
        String text = search.getText();
        Seq<Archive> array = Vars.content.statusEffects().select(a -> a instanceof Archive && ((!Vars.state.isCampaign() && !Vars.state.isMenu()) || Vars.state.isEditor() || a.unlocked()) && (text.isEmpty() || a.localizedName.toLowerCase().contains(text.toLowerCase()))).as();
        int cols = Math.max((int) (Core.graphics.getWidth() / Scl.scl(220)), 1);
        int count = 0;

        for (Archive archive : array) {
            all.button(b -> {
                b.top();
                b.add(archive.localizedName).labelAlign(Align.center).growX().wrap().color(Pal.accent).row();
                b.image(archive.fullIcon).grow().scaling(Scaling.fit).pad(40f);
            }, () -> {
                if (cons != null) {
                    cons.get(archive);
                    cons = null;
                    hide();
                } else {
                    archiveInfo.show(archive);
                }
            }).size(200f).pad(5f);

            if (++count % cols == 0) {
                all.row();
            }
        }

        if (all.getChildren().isEmpty()) {
            all.add("@none.found");
        }
    }

    public void showSelect(Cons<Archive> cons) {
        this.cons = cons;
        show();
    }
}
