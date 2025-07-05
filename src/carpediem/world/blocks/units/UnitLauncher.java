package carpediem.world.blocks.units;

import arc.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.graphics.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

// they just be putting classes anywhere atp
public class UnitLauncher extends Block {
    public float range = 240f * 8f;

    public UnitLauncher(String name) {
        super(name);
        update = true;
        configurable = true;
        clearOnDoubleTap = true;

        config(Vec2.class, (UnitLauncherBuild build, Vec2 pos) -> {
            if (pos.within(build, range)) {
                build.target.set(pos);
            }
        });
        configClear((UnitLauncherBuild build) -> build.target.set(build));
    }

    public class UnitLauncherBuild extends Building {
        public Vec2 target = new Vec2();

        @Override
        public void created() {
            target.set(this);
        }

        @Override
        public void unitOnAny(Unit unit) {
            if (target.epsilonEquals(x, y)) return;

            if (unit.team == team && !unit.isFlying()) {
                // TODO launch
            }
        }

        @Override
        public boolean onConfigureTapped(float x, float y) {
            configure(Tmp.v1.set(x, y));
            CDFx.launcherSelect.at(this.x, this.y, size * Vars.tilesize / 2f + 1f, Tmp.v1.cpy());
            return false;
        }

        @Override
        public void drawConfigure() {
            Drawf.limitLine(this, Core.input.mouseWorld(), size * Vars.tilesize / 2f + 1f, 3f);
            Drawf.square(Core.input.mouseWorldX(), Core.input.mouseWorldY(), 3f);
            Drawf.circles(x, y, size * Vars.tilesize / 2f + 1f);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(target.x);
            write.f(target.y);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            target.x = read.f();
            target.y = read.f();
        }
    }
}
