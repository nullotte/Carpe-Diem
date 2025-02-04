package carpediem.audio;

import arc.audio.*;
import mindustry.*;

public class CDMusics {
    public static Music reboot, land;

    public static void load() {
        reboot = Vars.tree.loadMusic("reboot");
        land = Vars.tree.loadMusic("cd-land");
    }
}
