package carpediem.content.blocks;

import carpediem.content.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class CDEnvironment {
    /*
    * env notes or whatever. yea im writing these in a block comment i cant be bothered
    * - fog. lots of fog. atmosphere and funky sky mesh.
    * - orange desert/badlands biome
    * - red charred forest-ish biome
    * - purple & black/grey highlands rocky/stony biome
    * - blue volcanic biome
    * - snow
    *
    * - add like. special floor that generates underneath props. that way when they get deconstructed the floor
    * - still looks a bit. not flat i guess
    * */
    public static Block
    // floors
    orange, borange, redsoil, purple, blue, hotCarbon, magmaCarbon,
    // walls
    blueWall,
    // ore
    oreAluminum, oreSulfur, oreNickel, oreSilver, orePlatinum;

    public static void load() {
        blue = new Floor("blue", 5);

        blueWall = new StaticWall("blue-wall") {{
            variants = 3;
        }};

        oreAluminum = new OreBlock(CDItems.rawAluminum);
        oreSulfur = new OreBlock(CDItems.sulfur);
        oreNickel = new OreBlock(CDItems.rawNickel);
        oreSilver = new OreBlock(CDItems.rawSilver);
        orePlatinum = new OreBlock(CDItems.rawPlatinum);
    }
}
