package carpediem.world.blocks.payloads;

import mindustry.*;
import mindustry.world.*;

public class SyntheticBlock extends Block {
    public SyntheticBlock(String name) {
        super(name);
        solid = true;
        destructible = true;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return !super.isHidden() && (Vars.state.rules.editor || (!Vars.state.rules.hideBannedBlocks || !Vars.state.rules.isBanned(this)));
    }
}
