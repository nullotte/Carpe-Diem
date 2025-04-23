package carpediem.content;

import arc.struct.*;
import carpediem.content.blocks.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class CDBlocks {
    public static void load() {
        CDEnvironment.load();
        CDCrafting.load();
        CDDistribution.load();
        CDLiquidBlocks.load();
        CDPower.load();
        CDProduction.load();
        CDStorage.load();
        CDPayloadBlocks.load();
        CDPayloadComponents.load();
        CDCampaign.load();

        // unfinished stuff
        Seq<Block> unfinished = Seq.with(
                CDPayloadBlocks.payloadDisassembler,
                CDPayloadBlocks.hydraulicFan,
                CDPayloadBlocks.bulkHeater,
                CDPayloadBlocks.payloadLoader,
                CDPayloadBlocks.payloadUnloader,
                // GOD
                CDPayloadComponents.blockRawAluminum,
                CDPayloadComponents.blockRawNickel,
                CDPayloadComponents.blockRawSilver,
                CDPayloadComponents.blockRawPlatinum,
                CDPayloadComponents.blockAluminum,
                CDPayloadComponents.blockNickel,
                CDPayloadComponents.blockSilver,
                CDPayloadComponents.blockPlatinum,
                CDPayloadComponents.blockSulfur,
                CDPayloadComponents.blockTar,
                CDPayloadComponents.fuelBrick
        );
        for (Block block : unfinished) {
            block.buildVisibility = BuildVisibility.hidden;
        }
    }
}
