package carpediem.audio;

import arc.audio.*;
import mindustry.*;

public class CDSounds {
    public static Sound launcherWindup, launcherBoing, launchPlatformLaunch, rocketCountdown, rocketLoop;

    public static void load() {
        launcherWindup = Vars.tree.loadSound("launcher-windup");
        launcherBoing = Vars.tree.loadSound("launcher-boing");
        launchPlatformLaunch = Vars.tree.loadSound("launch-platform-launch");
        rocketCountdown = Vars.tree.loadSound("rocket-countdown");
        rocketLoop = Vars.tree.loadSound("rocket-loop");

        // this is definitely bad but it works to allow the sound to play while paused
        rocketLoop.bus = Vars.control.sound.uiBus;
    }
}
