package carpediem.world.blocks.campaign;

import arc.*;
import arc.scene.ui.layout.*;
import arc.util.io.*;
import carpediem.type.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.world.*;

// im not making editor ui dawg that requires Effort
public class ArchiveVault extends Block {
    public ArchiveVault(String name) {
        super(name);
        solid = true;
        destructible = true;
    }

    @Override
    public boolean canBreak(Tile tile) {
        return Vars.state.isEditor() || (tile.build instanceof ArchiveVaultBuild build && (build.archive == null || build.archive.unlockedNow()));
    }

    public class ArchiveVaultBuild extends Building {
        public Archive archive;

        @Override
        public void display(Table table) {
            super.display(table);

            table.row();
            table.label(() -> Core.bundle.format("bar.containsarchive", archive == null ? "[lightgray]" + Core.bundle.get("none") : "[accent]" + archive.localizedName)).pad(4f).wrap().width(200f).left();
        }

        @Override
        public boolean canPickup() {
            return false;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.s(archive == null ? -1 : archive.id);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            archive = Vars.content.getByID(ContentType.status, read.s());
        }
    }
}
