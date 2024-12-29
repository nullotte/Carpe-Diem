package carpediem.content.blocks;

import arc.graphics.*;
import carpediem.content.*;
import mindustry.content.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

public class CDEnvironment {
    /*
    * - add like. special floor that generates underneath props. that way when they get deconstructed the floor
    * - still looks a bit. not flat i guess
    * */
    public static Block
    // floors
    arksand, arkstone, orangeStone,
    meadsoil, scorchedSoil,
    royalstone, crystalrock,
    bluerock, hotCarbon, magmaCarbon,
    // walls
    arkstoneWall, orangeStoneWall,
    meadsoilWall, scorchedSoilWall,
    royalstoneWall, crystalrockWall,
    bluerockWall,
    // ore
    oreAluminum, oreSulfur, oreNickel, oreSilver, orePlatinum;

    public static void load() {
        arkstone = new Floor("arkstone", 5);

        orangeStone = new Floor("orange-stone", 5);

        meadsoil = new Floor("meadsoil", 5);

        royalstone = new Floor("royalstone", 5);

        crystalrock = new Floor("crystalrock", 5);

        bluerock = new Floor("bluerock", 5);

        hotCarbon = new Floor("hotcarbon", 4) {{
            attributes.set(Attribute.heat, 1f);
            blendGroup = Blocks.carbonStone;

            // lalalalala
            emitLight = true;
            lightRadius = 30f;
            lightColor = Color.orange.cpy().a(0.15f);
        }};

        magmaCarbon = new Floor("magmacarbon", 4) {{
            // functionally the same as hotcarbon, just looks different
            attributes.set(Attribute.heat, 1f);
            blendGroup = Blocks.carbonStone;

            emitLight = true;
            lightRadius = 50f;
            lightColor = Color.orange.cpy().a(0.15f);
        }};

        bluerockWall = new StaticWall("bluerock-wall") {{
            variants = 3;
        }};

        oreAluminum = new OreBlock(CDItems.rawAluminum);
        oreSulfur = new OreBlock(CDItems.sulfur);
        oreNickel = new OreBlock(CDItems.rawNickel);
        oreSilver = new OreBlock(CDItems.rawSilver);
        orePlatinum = new OreBlock(CDItems.rawPlatinum);
    }
}
