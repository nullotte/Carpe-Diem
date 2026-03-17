package carpediem.audio;

import arc.audio.*;
import mindustry.*;

public class CDSounds {
    public static Sound launcherWindup, launcherBoing, launchPlatformLaunch, rocketCountdown;

    public static void load() {
        launcherWindup = Vars.tree.loadSound("launcher-windup");
        launcherBoing = Vars.tree.loadSound("launcher-boing");
        launchPlatformLaunch = Vars.tree.loadSound("launch-platform-launch");
        rocketCountdown = Vars.tree.loadSound("rocket-countdown");
    }
}
