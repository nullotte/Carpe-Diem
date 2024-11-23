package carpediem.world.blocks.campaign;

import arc.scene.ui.layout.*;
import arc.util.io.*;
import carpediem.*;
import carpediem.type.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.world.*;

public class ArchiveBlock extends Block {
    public ArchiveBlock(String name) {
        super(name);
        update = true;
        solid = true;
        configurable = true;

        config(Archive.class, ArchiveBuild::setArchive);
    }

    @Override
    public boolean canBreak(Tile tile) {
        return Vars.state.isEditor();
    }

    public class ArchiveBuild extends Building {
        public Archive archive;

        public void setArchive(Archive archive) {
            this.archive = archive;
        }

        @Override
        public void buildConfiguration(Table table) {
            if (Vars.state.isEditor()) {
                CarpeDiem.archiveDatabase.showSelect(this::configure);
            } else {
                // TODO add a puzzle for deciphering archives
                // also maybe different regions for archive blocks containing unlocked archives?
                Vars.ui.content.show(archive);
                archive.unlock();
            }

            deselect();
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(archive != null ? archive.id : -1);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            int id = read.i();
            if (id >= 0) {
                archive = Vars.content.getByID(ContentType.status, id);
            }
        }
    }
}
