package carpediem.maps.planet;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;

public class AsphodelPlanetGenerator extends PlanetGenerator {
    public int octaves = 5, riverOctaves = 4;
    public float heightScl = 0.6f, heightMult = 0.25f, riverLevel = -0.6f;
    public float riverScl = 0.9f, riverMult = -1.2f;

    // "temperature" and height
    public Block[][] terrain = {
            {Blocks.darksand, Blocks.stone, Blocks.stone, Blocks.snow},
            {Blocks.sand, Blocks.shale, Blocks.stone, Blocks.snow},
            {Blocks.sand, Blocks.shale, Blocks.shale, Blocks.snow},
            {Blocks.sand, Blocks.shale, Blocks.shale, Blocks.snow},
            {Blocks.grass, Blocks.shale, Blocks.snow, Blocks.snow}
    };

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
        return (rawHeight(position) + riverDepth(position)) * heightMult;
    }

    @Override
    public Color getColor(Vec3 position) {
        Block block = getBlock(position);
        if (riverDepth(position) < riverLevel) {
            block = Blocks.redStone;
        }
        return Tmp.c1.set(block.mapColor).a(1f - block.albedo);
    }

    @Override
    public void generateSector(Sector sector) {

    }

    @Override
    public float getSizeScl() {
        return 1800;
    }

    protected Block getBlock(Vec3 position) {
        float temp = Math.abs(position.y) * 2f;
        float tnoise = Simplex.noise3d(seed, 7, 0.5f, 1f / 6f, position.x, position.y + 999f, position.z);
        temp = Mathf.lerp(temp, tnoise, 0.5f);

        float height = rawHeight(position) + riverDepth(position);
        height = (height + 1f) / 2f;

        int tempIndex = Mathf.clamp((int) (temp * terrain.length), 0, terrain.length - 1);
        int heightIndex = Mathf.clamp((int) (height * terrain[tempIndex].length), 0, terrain[tempIndex].length - 1);

        return terrain[tempIndex][heightIndex];
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
