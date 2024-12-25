package carpediem.maps.planet;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.noise.*;
import carpediem.content.blocks.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;

public class AsphodelPlanetGenerator extends PlanetGenerator {
    public int octaves = 5, riverOctaves = 4;
    public float heightScl = 0.6f, heightMult = 0.3f, coldPow = 6f, coldScl = 0.4f;
    public float riverScl = 0.9f, riverMult = -1.2f, riverLevel = -0.6f, riverOffset = -0.2f;

    public Block[] terrain = {Blocks.redStone, Blocks.crystalFloor, CDEnvironment.blue, Blocks.snow};

    public AsphodelPlanetGenerator() {

    }

    public float rawHeight(Vec3 position) {
        return Ridged.noise3d(seed, position.x, position.y, position.z, octaves, 1f / heightScl);
    }

    public float riverDepth(Vec3 position) {
        return Math.min(0f, Ridged.noise3d(seed + 1, position.x, position.y, position.z, riverOctaves, 1f / riverScl) * riverMult);
    }

    @Override
    public float getHeight(Vec3 position) {
        float depth = riverDepth(position);
        if (depth < riverLevel) {
            depth += riverOffset;
        }

        return (rawHeight(position) + depth) * heightMult;
    }

    @Override
    public Color getColor(Vec3 position) {
        Block block = getBlock(position);
        if (riverDepth(position) < riverLevel) {
            block = Blocks.ice;
        }
        return Tmp.c1.set(block.mapColor).a(1f - block.albedo);
    }

    @Override
    public void generateSector(Sector sector) {

    }

    @Override
    public float getSizeScl() {
        return 1000;
    }

    protected Block getBlock(Vec3 position) {
        float cold = Math.abs(position.y) * 2f;
        float cnoise = Simplex.noise3d(seed, 7, 0.5f, 1f / 6f, position.x, position.y + 999f, position.z);
        cold = Mathf.lerp(cold, cnoise, 0.5f);

        float height = rawHeight(position) + riverDepth(position);
        height = (height + 1f) / 2f;
        height = Mathf.lerp(height, 1f, Mathf.pow(cold, coldPow) * coldScl);

        int heightIndex = Mathf.clamp((int) (height * terrain.length), 0, terrain.length - 1);
        return terrain[heightIndex];
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
