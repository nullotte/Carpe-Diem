package carpediem.content.blocks;

import arc.graphics.*;
import carpediem.content.*;
import mindustry.content.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

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
