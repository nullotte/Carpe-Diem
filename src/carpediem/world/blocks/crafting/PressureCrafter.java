package carpediem.world.blocks.crafting;

import arc.graphics.*;
import arc.math.*;
import arc.util.io.*;
import carpediem.world.meta.*;
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

    public class PressureCrafterBuild extends GenericCrafterBuild {
        public float pressure;

        @Override
        public void updateTile() {
            super.updateTile();

            pressure = Mathf.approachDelta(pressure, pressureProduction * efficiency, warmupRate * delta());
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
