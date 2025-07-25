package carpediem.maps.planet;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.noise.*;
import carpediem.content.*;
import carpediem.content.blocks.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;

public class AsphodelPlanetGenerator extends PlanetGenerator {
    // THE GREAT WALL OF NUMBERS
    public int octaves = 9, noiseOctaves = 8, airOctaves = 3;
    public float heightScl = 0.6f, heightMult = 0.3f;
    public float coldScl = 1.6f, coldProgress = 0.4f;
    public float noiseScl = 0.3f, noiseFalloff = 0.6f, noisePow = 6f, noiseMult = 0.7f;
    public float airScl = 4f, airThresh = 0.07f;

    public int iceOctaves = 10, blueOctaves = 7, crystalOctaves = 8;
    public float iceScl = 0.1f, iceFalloff = 0.4f, iceThresh = 0.6f;
    public float blueScl = 0.7f, blueFalloff = 0.5f, blueThresh = 0.55f;
    public float crystalScl = 0.2f, crystalFalloff = 0.6f, crystalThresh = 0.5f;

    public Block[] terrain = {
            CDEnvironment.arkstone,
            CDEnvironment.arkstone,
            CDEnvironment.orangeStone,
            Blocks.redStone,
            CDEnvironment.meadsoil,
            CDEnvironment.royalstone,
            CDEnvironment.crystalrock,
            CDEnvironment.crystalrock,
            CDEnvironment.crystalrock,
            CDEnvironment.bluerock,
            CDEnvironment.bluerock,
            CDEnvironment.hotCarbon,
            Blocks.carbonStone,
            Blocks.snow
    };

    public AsphodelPlanetGenerator() {
        defaultLoadout = CDLoadouts.schemLandingPodT0;
    }

    public float rawHeight(Vec3 position) {
        return Ridged.noise3d(seed, position.x, position.y, position.z, octaves, 1f / heightScl);
    }

    @Override
    public float getHeight(Vec3 position) {
        return rawHeight(position) * heightMult;
    }

    @Override
    public void getColor(Vec3 position, Color out) {
        Block block = getBlock(position);
        out.set(block.mapColor);

        // i dont like the vanilla colors WHY are they so dull
        if (block == Blocks.redStone) {
            Color.valueOf(out, "af4753");
        } else if (block == Blocks.crystalFloor || block == CDEnvironment.crystalrock) {
            Color.valueOf(out, "60496d");
        } else if (block == CDEnvironment.hotCarbon) {
            // ok this one isnt because the vanilla colors are ugly its just the hot carbon
            // like how is it gray? i dont understand
            block = Blocks.carbonStone;
            out.set(block.mapColor);
        }

        out.a(1f - block.albedo);
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

        float height = rawHeight(position);
        height = (height + 1f) / 2f;
        height = Mathf.lerp(height, Math.abs(position.y) * coldScl, coldProgress);
        height = Mathf.lerp(height, 1f, Mathf.pow(cnoise, noisePow) * noiseMult);

        int heightIndex = Mathf.clamp((int) (height * terrain.length), 0, terrain.length - 1);
        Block result = terrain[heightIndex];

        if (result == Blocks.snow && Simplex.noise3d(seed, iceOctaves, iceFalloff, 1f / iceScl, position.x + 30f, position.y + 60f, position.z) > iceThresh) {
            result = Blocks.ice;
        }

        if (result == CDEnvironment.crystalrock) {
            if (Simplex.noise3d(seed, blueOctaves, blueFalloff, 1f / blueScl, position.x + 55f, position.y + 55f, position.z) > blueThresh) {
                result = CDEnvironment.bluerock;
            } else if (Simplex.noise3d(seed, crystalOctaves, crystalFalloff, 1f / crystalScl, position.x + 50f, position.y + 55f, position.z) > crystalThresh) {
                result = Blocks.crystalFloor;
            }
        }

        return result;
    }

    @Override
    protected void genTile(Vec3 position, TileGen tile) {
        tile.floor = getBlock(position);

        tile.block = tile.floor.asFloor().wall;

        if (Ridged.noise3d(seed + 1, position.x, position.y, position.z, airOctaves, airScl) < airThresh) {
            tile.block = Blocks.air;
        }
    }

    @Override
    protected void generate() {
        cells(4);
        distort(60f, 100f);
        distort(10f, 12f);

        pass((x, y) -> {
            // we dont do that ugly stuff here
            if (block == Blocks.crystallineStoneWall) block = CDEnvironment.crystalWall;

            if (floor == CDEnvironment.bluerock) {
                if (noise(x, y, 9, 0.5f, 90f) > 0.6f) {
                    floor = Blocks.carbonStone;
                    if (block == CDEnvironment.bluerockWall) block = Blocks.carbonWall;
                }
            }

            if (noise(x, y, 12, 0.6f, 60f) > 0.55f) {
                if (floor == CDEnvironment.royalstone) {
                    floor = Blocks.moss;
                } else if (floor == Blocks.redStone) {
                    floor = CDEnvironment.redMoss;
                }
            }

            // yes i copied this from erekir planetgen
            if (floor == Blocks.redStone && noise(x + 78 - y, y, 4, 0.73f, 19f, 1f) > 0.47f) {
                floor = Blocks.denseRedStone;
            }

            if (floor == CDEnvironment.hotCarbon) {
                // im just copying serpulo's hotrock code because i cannot get good looking hotrocks for the life of me
                if (Math.abs(0.5f - noise(x, y + 60, 8, 0.5f, 85f)) > 0.02f) {
                    floor = Blocks.carbonStone;
                }
            }
        });

        pass((x, y) -> {
            // do i have explain why this is in a second pass
            if (floor == CDEnvironment.hotCarbon) {
                ore = Blocks.air;
                boolean all = true;
                for (Point2 p : Geometry.d4) {
                    Tile other = tiles.get(x + p.x, y + p.y);
                    if (other == null || (other.floor() != CDEnvironment.hotCarbon && other.floor() != CDEnvironment.magmaCarbon)) {
                        all = false;
                    }
                }
                if (all) {
                    floor = CDEnvironment.magmaCarbon;
                }
            }
        });

        trimDark();

        Schematics.placeLaunchLoadout(width / 2, height / 2);
    }
}
