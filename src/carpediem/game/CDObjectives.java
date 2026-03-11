package carpediem.game;

import arc.*;
import carpediem.type.*;
import mindustry.game.Objectives.*;
import mindustry.world.blocks.storage.*;

public class CDObjectives {
    public static class DecodeArchive implements Objective {
        @Override
        public boolean complete() {
            return false;
        }

        @Override
        public String display() {
            return Core.bundle.get("requirement.decodearchive");
        }
    }

    public static class UnlockArchive implements Objective {
        public Archive archive;

        public UnlockArchive(Archive archive) {
            this.archive = archive;
        }

        @Override
        public boolean complete() {
            return archive.unlocked();
        }

        @Override
        public String display() {
            return Core.bundle.format("requirement.unlockarchive", archive.localizedName);
        }
    }

    public static class LaunchSector implements Objective {
        public CoreBlock requiredCore;

        public LaunchSector(CoreBlock requiredCore) {
            this.requiredCore = requiredCore;
        }

        @Override
        public boolean complete() {
            return false;
        }

        @Override
        public String display() {
            return Core.bundle.format("requirement.launchsector", requiredCore.localizedName);
        }
    }

    public static class LaunchRocket implements Objective {
        @Override
        public boolean complete() {
            return false;
        }

        @Override
        public String display() {
            return Core.bundle.get("requirement.launchrocket");
        }
    }
}
