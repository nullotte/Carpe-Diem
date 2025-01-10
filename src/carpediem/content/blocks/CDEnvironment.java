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
    // walls
    arkstoneWall, orangeStoneWall,
    meadsoilWall, scorchedWall,
    royalstoneWall, crystalWall,
    bluerockWall,
    // decoration
    arkstoneBoulder, orangeStoneBoulder, arkweed,
    redGrowth, redTendrils, meadBush, meadTree, orangeTree, scorchedBush, scorchedTree,
    royalstoneBoulder, crystalBoulder,
    bluerockBoulder,
    // ore
    oreAluminum, oreSulfur, oreNickel, oreSand, oreSilver, orePlatinum;

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
        // endregion
        // region decoration
        // bitter choco decoration
        // endregion
        // region ore
        oreAluminum = new OreBlock("ore-aluminum", CDItems.rawAluminum);
        oreSulfur = new OreBlock("ore-sulfur", CDItems.sulfur);
        oreNickel = new OreBlock("ore-nickel", CDItems.rawNickel);
        oreSand = new OreBlock("ore-sand", Items.sand);
        oreSilver = new OreBlock("ore-silver", CDItems.rawSilver);
        orePlatinum = new OreBlock("ore-platinum", CDItems.rawPlatinum);
        // endregion
    }
}
