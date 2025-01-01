package carpediem.type;

import arc.*;
import arc.func.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import carpediem.*;
import carpediem.game.CDObjectives.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;

public class Archive extends StatusEffect {
    // content that is locked behind this archive
    public Seq<UnlockableContent> contents;
    public ItemStack[] researchCost;

    public Archive(String name, ItemStack[] researchCost, Seq<UnlockableContent> contents) {
        super(name);
        this.researchCost = researchCost;
        this.contents = contents;
        show = false;

        contents.each(content -> {
            // should automatically be unlocked upon unlocking this archive
            if (content instanceof Block block) {
                block.researchCost = ItemStack.empty;
            }
        });
    }

    @Override
    public void loadIcon() {
        // TODO custom archive icons
        if (Icon.book != null) {
            uiIcon = fullIcon = Icon.book.getRegion();
        }
    }

    @Override
    public void init() {
        super.init();

        if (techNode != null) {
            techNode.objectives.add(new DecodeArchive());
        }

        contents.each(content -> {
            if (content.techNode != null) {
                content.techNode.objectives.add(new UnlockArchive(this));
            }
        });
    }

    @Override
    public ItemStack[] researchRequirements() {
        return researchCost;
    }

    @Override
    public boolean showUnlock() {
        return true;
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
                        }, () -> CarpeDiem.content.show(content)).size(200f).pad(5f);

                        if (++count % cols == 0) {
                            t.row();
                        }
                    }

                },
                "logs", (Cons<Table>) t -> {
                    t.add("[gray]" + (unlocked() ? Core.bundle.get(getContentType() + "." + name + ".logs") : Iconc.lock + " " + Core.bundle.get("unlock.incampaign"))).pad(6).padTop(20).width(400f).wrap().fillX();
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
