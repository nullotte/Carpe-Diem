package carpediem.type;

import arc.*;
import arc.struct.*;
import arc.util.*;
import carpediem.*;
import carpediem.game.CDObjectives.*;
import carpediem.world.meta.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;

public class Archive extends StatusEffect {
    // content that is locked behind this archive
    public Seq<UnlockableContent> contents;
    public ItemStack[] researchCost;

    public Archive(String name, ItemStack[] researchCost, Seq<UnlockableContent> contents) {
        super(name);
        this.researchCost = researchCost;
        this.contents = contents;

        hideDatabase = true;
    }

    @Override
    public void loadIcon() {
        if (Icon.book != null) {
            uiIcon = fullIcon = Core.atlas.find(name, Icon.book.getRegion());
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
                content.techNode.objectives.clear().add(new UnlockArchive(this));
            }
        });
    }

    @Override
    public void setStats() {
        stats.add(CDStat.contents, table -> {
            for (UnlockableContent content : contents) {
                table.row();
                table.table(Styles.grayPanel, b -> {
                    b.image(content.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit);
                    b.table(info -> {
                        info.add(content.localizedName).left();
                    });
                    b.button("?", Styles.flatBordert, () -> CarpeDiem.content.show(content)).size(40f).pad(10).right().grow();
                }).growX().pad(5).row();
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
    public String displayDescription() {
        return description;
    }
}
