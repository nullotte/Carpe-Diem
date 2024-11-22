package carpediem.world.blocks.payloads;

import mindustry.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;

// constructor that can construct cores because for some reason vanilla constructors just Cant
public class PayloadComponentConstructor extends Constructor {
    public PayloadComponentConstructor(String name) {
        super(name);
    }

    @Override
    public boolean canProduce(Block b) {
        return b.isVisible() && b.size >= minBlockSize && b.size <= maxBlockSize && !Vars.state.rules.isBanned(b) && b.environmentBuildable() && (filter.isEmpty() || filter.contains(b));
    }
}
