package carpediem.maps.planet;

import arc.graphics.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;

public class ModdedPlanetGenerator extends PlanetGenerator {
    @Override
    public float getHeight(Vec3 position) {
        return 0.5f;
    }

    @Override
    public Color getColor(Vec3 position) {
        return Pal.accent;
    }

    @Override
    public void generateSector(Sector sector) {

    }

    @Override
    public float getSizeScl() {
        return 1800;
    }

    protected Block getBlock(Vec3 position) {
        return Blocks.snow;
    }

    @Override
    protected void genTile(Vec3 position, TileGen tile) {
        tile.floor = getBlock(position);
    }

    @Override
    protected void generate() {
        Schematics.placeLaunchLoadout(width / 2, height / 2);
    }
}
