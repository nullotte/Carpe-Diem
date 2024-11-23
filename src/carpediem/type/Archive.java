package carpediem.type;

import arc.*;
import arc.func.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import carpediem.game.CDObjectives.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;

public class Archive extends StatusEffect {
    // content that is locked behind this archive
    public Seq<UnlockableContent> contents = new Seq<>();

    public Archive(String name) {
        super(name);
        show = false;
    }

    @Override
    public void loadIcon() {
        if (Icon.book != null) {
            uiIcon = fullIcon = Icon.book.getRegion();
        }
    }

    @Override
    public void init() {
        super.init();

        contents.each(content -> {
            if (content.techNode != null) {
                content.techNode.objectives.add(new UnlockArchive(this));
            }
        });
    }

    @Override
    public void displayExtra(Table table) {
        OrderedMap<String, Cons<Table>> pages = OrderedMap.of(
                "content", (Cons<Table>) t -> {
                    int cols = Math.max((int) (Core.graphics.getWidth() / Scl.scl(220)), 1);
                    int count = 0;

                    for (UnlockableContent content : contents) {
                        t.button(b -> {
                            b.top();
                            b.add(content.localizedName).labelAlign(Align.center).growX().wrap().color(Pal.accent).row();
                            b.image(content.fullIcon).grow().scaling(Scaling.fit).pad(20f);
                            // TODO maybe display research requirements
                        }, () -> Vars.ui.content.show(content)).size(200f).pad(5f);

                        if (++count % cols == 0) {
                            t.row();
                        }
                    }

                },
                "logs", (Cons<Table>) t -> {
                    t.label(() -> "[lightgray]not implemented yet. come back later lawl");
                }
        );

        Table pageTable = new Table();
        String[] currentPage = {pages.orderedKeys().first()};
        pages.get(currentPage[0]).get(pageTable);

        // tabber
        table.table(tabber -> {
            ButtonGroup<TextButton> tabs = new ButtonGroup<>();

            pages.each((pageName, pageCons) -> {
                tabber.button("@archives." + pageName, Styles.squareTogglet, () -> {
                    currentPage[0] = pageName;

                    pageTable.clear();
                    pages.get(currentPage[0]).get(pageTable);
                }).group(tabs).size(240f, 64f);
            });
        }).pad(10f);
        table.row();

        table.add(pageTable);
    }

    @Override
    public String displayDescription() {
        return description;
    }
}
