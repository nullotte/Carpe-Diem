package carpediem.content;

import carpediem.content.blocks.*;

public class CDBlocks {
    /*
     * hi block comment
     * - compacted item blocks like how the unit assemblers use walls
     * - add uranium.
     * - "database server" block that you have to supply power and items to . and then it opens up some puzzle dialog
     * - after completing the puzzle you unlock its research and some other stuff stored inside
     * */

    public static void load() {
        // CDEnvironment.load();
        CDCrafting.load();
        CDDistribution.load();
        // CDLiquidBlocks.load();
        CDPower.load();
        CDProduction.load();
        CDStorage.load();
        CDPayloads.load();
        // CDCampaign.load();
    }
}
