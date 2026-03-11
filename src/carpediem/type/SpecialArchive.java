package carpediem.type;

import arc.func.*;
import arc.struct.*;
import carpediem.game.CDObjectives.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.type.*;

public class SpecialArchive extends Archive {
    public SpecialArchive(String name) {
        super(name, ItemStack.empty, new Seq<>());
    }

    @Override
    public void init() {
        super.init();
        if (techNode != null) {
            techNode.objectives.clear().add(new LaunchRocket());
        }
    }

    @Override
    public void getDependencies(Cons<UnlockableContent> cons) {
    }

    @Override
    public void setStats() {
    }

    @Override
    public boolean isHidden() {
        return !Vars.state.isCampaign();
    }
}
