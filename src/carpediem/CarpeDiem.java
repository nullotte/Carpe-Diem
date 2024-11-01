package carpediem;

import mindustry.mod.*;
import carpediem.content.*;

public class CarpeDiem extends Mod {
    @Override
    public void loadContent() {
        CDItems.load();
        CDLiquids.load();
        CDBlocks.load();
        CDPlanets.load();
    }
}
