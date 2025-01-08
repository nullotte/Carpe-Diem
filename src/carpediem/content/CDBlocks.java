package carpediem.content;

import carpediem.content.blocks.*;

public class CDBlocks {
    public static void load() {
        CDEnvironment.load();
        CDCrafting.load();
        CDDistribution.load();
        CDLiquidBlocks.load();
        CDPower.load();
        CDProduction.load();
        CDStorage.load();
        CDPayloads.load();
        CDPayloadComponents.load();
        CDCampaign.load();
    }
}
