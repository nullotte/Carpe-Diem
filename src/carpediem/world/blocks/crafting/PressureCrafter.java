package carpediem.world.blocks.crafting;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.io.*;
import carpediem.world.meta.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

// lalala
public class PressureCrafter extends GenericCrafter {
    public float pressureProduction = 5f;
    public float warmupRate = 0.15f;

    public PressureCrafter(String name) {
        super(name);
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(Stat.output, pressureProduction, CDStat.pressureUnits);
    }

    @Override
    public void setBars() {
        super.setBars();

        addBar("pressure", (PressureCrafterBuild entity) -> new Bar("bar.pressure", Color.violet, () -> entity.pressure / pressureProduction));
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);

        if (rotate) {
            int dx = Geometry.d4x(rotation), dy = Geometry.d4y(rotation);
            float drawx = x * Vars.tilesize + offset, drawy = y * Vars.tilesize + offset;
            float dst = (size + 1) / 2f * Vars.tilesize;
            Drawf.square(drawx + (dx * dst), drawy + (dy * dst), 2f, 45f, Pal.placing);
        }
    }

    public class PressureCrafterBuild extends GenericCrafterBuild {
        public float pressure;

        @Override
        public void updateTile() {
            super.updateTile();

            pressure = Mathf.approachDelta(pressure, pressureProduction * efficiency, warmupRate * delta());
        }

        @Override
        public void drawSelect() {
            if (block.rotate) {
                int dx = Geometry.d4x(rotation), dy = Geometry.d4y(rotation);
                float dst = (this.block.size + 1) / 2f * Vars.tilesize;
                Drawf.square(x + (dx * dst), y + (dy * dst), 2f, 45f, Pal.place);
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(pressure);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            pressure = read.f();
        }
    }
}
