package carpediem.world.blocks.campaign;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.type.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// im not making editor ui dawg that requires Effort
public class ArchiveVault extends Block {
    public float warmupSpeed = 0.019f;

    public DrawBlock drawer;

    public ArchiveVault(String name) {
        super(name);
        solid = true;
        update = true;
    }

    @Override
    public void load() {
        super.load();

        drawer.load(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    protected TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public boolean canBreak(Tile tile) {
        return Vars.state.isEditor() || (tile.build instanceof ArchiveVaultBuild build && (build.archive == null || build.archive.unlockedNow()));
    }

    public class ArchiveVaultBuild extends Building {
        public Archive archive;
        public float warmup;

        @Override
        public void updateTile() {
            if (archive != null && !archive.unlockedNow()) {
                warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }
        }

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
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public float warmup() {
            return warmup;
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
