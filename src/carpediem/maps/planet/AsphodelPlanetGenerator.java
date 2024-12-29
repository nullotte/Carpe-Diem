package carpediem.maps.planet;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.noise.*;
import carpediem.content.*;
import carpediem.content.blocks.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;

public class AsphodelPlanetGenerator extends PlanetGenerator {
    public int octaves = 9, noiseOctaves = 8, riverOctaves = 4;
    public float heightScl = 0.6f, heightMult = 0.3f;
    public float coldScl = 1.6f, coldProgress = 0.4f;
    public float noiseScl = 0.3f, noiseFalloff = 0.6f, noisePow = 6f, noiseMult = 0.7f;
    // TODO get rid of this maybe
    public float riverScl = 0.9f, riverMult = -1.2f, riverLevel = -0.7f, riverOffset = -0.2f;

    public Block[] terrain = {
            CDEnvironment.arkstone,
            CDEnvironment.arkstone,
            CDEnvironment.orangeStone,
            Blocks.redStone,
            CDEnvironment.meadsoil,
            CDEnvironment.royalstone,
            Blocks.crystalFloor,
            Blocks.crystalFloor,
            CDEnvironment.crystalrock,
            CDEnvironment.bluerock,
            CDEnvironment.bluerock,
            CDEnvironment.bluerock,
            Blocks.carbonStone,
            Blocks.carbonStone,
            Blocks.snow
    };

    public AsphodelPlanetGenerator() {
        defaultLoadout = CDLoadouts.schemLandingPodT0;
    }

    public float rawHeight(Vec3 position) {
        return Ridged.noise3d(seed, position.x, position.y, position.z, octaves, 1f / heightScl);
    }

    public float riverDepth(Vec3 position) {
        if (true) return 0f;

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
        Tmp.c1.set(block.mapColor);

        if (riverDepth(position) < riverLevel) {
            block = Blocks.ice;
            Tmp.c1.set(block.mapColor);
        }

        // i dont like the vanilla colors WHY are they so dull
        if (block == Blocks.redStone) {
            Color.valueOf(Tmp.c1, "af4753");
        } else if (block == Blocks.crystalFloor) {
            Color.valueOf(Tmp.c1, "60496d");
        }

        return Tmp.c1.a(1f - block.albedo);
    }

    @Override
    public void generateSector(Sector sector) {

    }

    @Override
    public float getSizeScl() {
        return 2200;
    }

    protected Block getBlock(Vec3 position) {
        float cnoise = Simplex.noise3d(seed, noiseOctaves, noiseFalloff, 1f / noiseScl, position.x, position.y + 999f, position.z);

        float height = rawHeight(position) + riverDepth(position);
        height = (height + 1f) / 2f;
        height = Mathf.lerp(height, Math.abs(position.y) * coldScl, coldProgress);
        height = Mathf.lerp(height, 1f, Mathf.pow(cnoise, noisePow) * noiseMult);

        int heightIndex = Mathf.clamp((int) (height * terrain.length), 0, terrain.length - 1);
        return terrain[heightIndex];
    }

    @Override
    protected void genTile(Vec3 position, TileGen tile) {
        tile.floor = getBlock(position);

        // TODO should walls be generated in here?
    }

    @Override
    protected void generate() {
        Schematics.placeLaunchLoadout(width / 2, height / 2);
    }
}
