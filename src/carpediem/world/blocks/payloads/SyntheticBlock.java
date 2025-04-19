package carpediem.world.blocks.payloads;

import mindustry.world.*;

// its just solid and destructible = true
public class SyntheticBlock extends Block {
    public SyntheticBlock(String name) {
        super(name);
        solid = true;
        destructible = true;
    }
}
