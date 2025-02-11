package carpediem.audio;

import arc.audio.*;
import carpediem.content.blocks.*;
import mindustry.*;
import mindustry.world.blocks.storage.*;

public class CDMusics {
    public static Music reboot, land;

    public static void load() {
        reboot = Vars.tree.loadMusic("reboot");
        land = Vars.tree.loadMusic("cd-land");

        for (CoreBlock block : new CoreBlock[]{CDStorage.landingPodT0}) {
            block.landMusic = land;
        }
    }
}
