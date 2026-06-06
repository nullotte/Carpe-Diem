package carpediem.world.blocks.payloads;

import mindustry.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.storage.*;

public class UnboundedPayloadSource extends PayloadSource {
    public UnboundedPayloadSource(String name) {
        super(name);
    }

    @Override
    public boolean canProduce(Block b) {
        return b.isVisible() && !(b instanceof CoreBlock) && !Vars.state.rules.isBanned(b) && b.environmentBuildable();
    }
}
