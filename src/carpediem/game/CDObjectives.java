package carpediem.game;

import arc.*;
import mindustry.game.Objectives.*;
import mindustry.world.blocks.storage.*;

public class CDObjectives {
    public static class LaunchSector implements Objective {
        public CoreBlock requiredCore;

        public LaunchSector(CoreBlock requiredCore) {
            this.requiredCore = requiredCore;
        }

        @Override
        public boolean complete() {
            // lol
            return false;
        }

        @Override
        public String display() {
            return Core.bundle.format("requirement.launchsector", requiredCore.localizedName);
        }
    }
}
