package carpediem.content.blocks;

import arc.graphics.*;
import carpediem.content.*;
import carpediem.world.blocks.environment.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

public class CDEnvironment {
    public static Block
    // floors
    asphodelWater, deepAsphodelWater, petroleumPatch,
    arkstone, orangeStone,
    redMoss, redSand, redSandWater, meadsoil,
    royalstone, crystalrock,
    bluerock, blueCraters, carbonCraters, carbonicSand, carbonicSandWater, hotCarbon, magmaCarbon,
    reserveFloor1, reserveFloor2, reserveFloor3,
    // walls
    arkstoneWall, orangeStoneWall, meadsoilWall,
    royalstoneWall, crystalWall,
    bluerockWall,
    reserveWall1, reserveWall2, reserveWall3,
    // decoration
    arkstoneBoulder, orangeStoneBoulder, meadBush,
    royalstoneBoulder, crystalBoulder, bluerockBoulder,
    arkweed, meadTree, redTree, deadTree,
    // ore
    oreAluminum, oreSulfur, oreNickel, oreSilver, orePlatinum;

    public static void load() {
        // region floors
        asphodelWater = new Floor("asphodel-water") {{
            speedMultiplier = 0.5f;
            variants = 0;
            status = StatusEffects.wet;
            statusDuration = 90f;
            liquidDrop = Liquids.water;
            isLiquid = true;
            cacheLayer = CacheLayer.water;
            albedo = 0.9f;
            supportsOverlay = true;
        }};

        deepAsphodelWater = new Floor("deep-asphodel-water") {{
            speedMultiplier = 0.2f;
            variants = 0;
            liquidDrop = Liquids.water;
            liquidMultiplier = 1.5f;
            isLiquid = true;
            status = StatusEffects.wet;
            statusDuration = 120f;
            drownTime = 200f;
            cacheLayer = CacheLayer.water;
            albedo = 0.9f;
            supportsOverlay = true;
        }};

        petroleumPatch = new Floor("petroleum-patch") {{
            drownTime = 230f;
            status = StatusEffects.tarred;
            statusDuration = 240f;
            speedMultiplier = 0.19f;
            variants = 0;
            liquidDrop = CDLiquids.petroleum;
            isLiquid = true;
            cacheLayer = CacheLayer.tar;
        }};

        arkstone = new Floor("arkstone", 5);

        orangeStone = new Floor("orange-stone", 5);

        redMoss = new Floor("red-moss", 5) {{
            wall = Blocks.redStoneWall;
        }};

        redSand = new Floor("red-sand", 5) {{
            itemDrop = Items.sand;
            playerUnmineable = true;
        }};

        redSandWater = new CDShallowLiquid("red-sand-water") {{
            speedMultiplier = 0.8f;
            statusDuration = 50f;
            albedo = 0.9f;
            liquidOpacity = 0.5f;
            supportsOverlay = true;
        }};
        ((CDShallowLiquid) redSandWater).set(asphodelWater, redSand);

        meadsoil = new Floor("meadsoil", 5);

        royalstone = new Floor("royalstone", 5);

        crystalrock = new Floor("crystalrock", 5);

        bluerock = new Floor("bluerock", 5);

        carbonicSand = new Floor("carbonic-sand", 5) {{
            itemDrop = Items.sand;
            playerUnmineable = true;
        }};

        carbonicSandWater = new CDShallowLiquid("carbonic-sand-water") {{
            speedMultiplier = 0.8f;
            statusDuration = 50f;
            albedo = 0.9f;
            supportsOverlay = true;
        }};
        ((CDShallowLiquid) carbonicSandWater).set(asphodelWater, carbonicSand);

        hotCarbon = new Floor("hotcarbon", 4) {{
            attributes.set(Attribute.heat, 1f);
            blendGroup = Blocks.carbonStone;
            wall = Blocks.carbonWall;

            // lalalalala
            emitLight = true;
            lightRadius = 30f;
            lightColor = Color.orange.cpy().a(0.15f);
        }};

        magmaCarbon = new Floor("magmacarbon", 4) {{
            // functionally the same as hotcarbon, just looks different
            attributes.set(Attribute.heat, 1f);
            blendGroup = Blocks.carbonStone;
            wall = Blocks.carbonWall;

            emitLight = true;
            lightRadius = 50f;
            lightColor = Color.orange.cpy().a(0.15f);
        }};

        reserveFloor1 = new Floor("reserve-floor-1", 0);
        reserveFloor2 = new Floor("reserve-floor-2", 0);
        reserveFloor3 = new Floor("reserve-floor-3", 0);
        // endregion
        // region walls
        arkstoneWall = new StaticWall("arkstone-wall") {{
            variants = 4;
        }};

        orangeStoneWall = new StaticWall("orange-stone-wall") {{
            variants = 4;
        }};

        meadsoilWall = new StaticWall("meadsoil-wall") {{
            variants = 4;
        }};

        royalstoneWall = new StaticWall("royalstone-wall") {{
            variants = 4;
        }};

        crystalWall = new StaticWall("crystal-wall") {{
            variants = 4;
            crystalrock.asFloor().wall = this;
        }};

        bluerockWall = new StaticWall("bluerock-wall") {{
            variants = 4;
        }};

        reserveWall1 = new StaticWall("reserve-wall-1") {{
            variants = 0;
        }};

        reserveWall2 = new StaticWall("reserve-wall-2") {{
            variants = 0;
        }};

        reserveWall3 = new StaticWall("reserve-wall-3") {{
            variants = 0;
        }};
        // endregion
        // region decoration
        arkstoneBoulder = new Prop("arkstone-boulder") {{
            variants = 2;
        }};

        orangeStoneBoulder = new Prop("orange-stone-boulder") {{
            variants = 2;
        }};

        meadBush = new Prop("mead-bush") {{
            variants = 2;
            breakSound = Sounds.plantBreak;
        }};

        royalstoneBoulder = new Prop("royalstone-boulder") {{
            variants = 2;
        }};

        crystalBoulder = new Prop("crystal-boulder") {{
            variants = 2;
        }};

        bluerockBoulder = new Prop("bluerock-boulder") {{
            variants = 2;
        }};
        // endregion
        // region ore
        oreAluminum = new OreBlock("ore-aluminum", CDItems.rawAluminum) {{
            variants = 4;
        }};

        oreSulfur = new OreBlock("ore-sulfur", CDItems.sulfur) {{
            variants = 4;
        }};

        oreNickel = new OreBlock("ore-nickel", CDItems.rawNickel) {{
            variants = 4;
        }};

        oreSilver = new OreBlock("ore-silver", CDItems.rawSilver) {{
            variants = 4;
        }};

        orePlatinum = new OreBlock("ore-platinum", CDItems.rawPlatinum) {{
            variants = 4;
        }};
        // endregion
    }
}
