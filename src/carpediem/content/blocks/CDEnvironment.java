package carpediem.content.blocks;

import carpediem.content.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class CDEnvironment {
    public static Block
    oreAluminum, oreSulfur, oreNickel, oreSilver, orePlatinum;

    public static void load() {
        oreAluminum = new OreBlock(CDItems.rawAluminum);
        oreSulfur = new OreBlock(CDItems.sulfur);
        oreNickel = new OreBlock(CDItems.rawNickel);
        oreSilver = new OreBlock(CDItems.rawSilver);
        orePlatinum = new OreBlock(CDItems.rawPlatinum);
    }
}
