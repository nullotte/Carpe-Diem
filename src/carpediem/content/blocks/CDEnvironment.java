package carpediem.content.blocks;

import arc.graphics.*;
import carpediem.content.*;
import mindustry.content.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

public class CDEnvironment {
    public static Block
    // floors
    arkstone, orangeStone,
    redMoss, meadsoil, scorchedSoil,
    royalstone, crystalrock,
    bluerock, blueCraters, carbonCraters, hotCarbon, magmaCarbon,
    reserveFloor1, reserveFloor2, reserveFloor3,
    // walls
    arkstoneWall, orangeStoneWall,
    meadsoilWall, scorchedWall,
    royalstoneWall, crystalWall,
    bluerockWall,
    reserveWall1, reserveWall2, reserveWall3,
    // decoration
    arkstoneBoulder, orangeStoneBoulder, arkweed,
    redGrowth, redTendrils, meadBush, meadTree, orangeTree, scorchedBush, scorchedTree,
    royalstoneBoulder, crystalBoulder,
    bluerockBoulder,
    // ore
    oreAluminum, oreSulfur, oreNickel, oreSilver, orePlatinum;

    public static void load() {
        // region floors
        arkstone = new Floor("arkstone", 5);

        orangeStone = new Floor("orange-stone", 5);

        redMoss = new Floor("red-moss", 5) {{
            wall = Blocks.redStoneWall;
        }};

        meadsoil = new Floor("meadsoil", 5);

        scorchedSoil = new Floor("scorched-soil", 5);

        royalstone = new Floor("royalstone", 5);

        crystalrock = new Floor("crystalrock", 5);

        bluerock = new Floor("bluerock", 5);

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

        scorchedWall = new StaticWall("scorched-wall") {{
            variants = 4;
            scorchedSoil.asFloor().wall = this;
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
