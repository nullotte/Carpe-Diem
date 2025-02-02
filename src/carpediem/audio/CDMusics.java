package carpediem.audio;

import arc.audio.*;
import mindustry.*;

public class CDMusics {
    public static Music reboot;

    public static void load() {
        reboot = Vars.tree.loadMusic("reboot");
    }
}
