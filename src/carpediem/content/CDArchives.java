package carpediem.content;

import arc.struct.*;
import carpediem.type.*;
import mindustry.type.*;

import static carpediem.content.CDItems.*;
import static carpediem.content.blocks.CDCampaign.*;
import static carpediem.content.blocks.CDCrafting.*;
import static carpediem.content.blocks.CDDistribution.*;
import static carpediem.content.blocks.CDPayloadComponents.*;
import static carpediem.content.blocks.CDPayloads.*;
import static carpediem.content.blocks.CDProduction.*;

public class CDArchives {
    public static Archive
            distribution, automation, payloadLogistics, outwardExpansion;

    public static void load() {
        distribution = new Archive(
                "distribution",
                ItemStack.with(lemon, 39),
                Seq.with(beltMerger, beltSplitter, beltOverflowGate, beltUnderflowGate, beltBridge)
        );

        automation = new Archive(
                "automation",
                ItemStack.with(controlCircuit, 20),
                Seq.with(drillT1, smelterT1, pressT1, rollingMillT1, assemblerT1)
        );

        payloadLogistics = new Archive(
                "payload-logistics",
                ItemStack.with(lemon, 39),
                Seq.with(payloadRail, payloadRailRouter, payloadAssembler)
        );

        outwardExpansion = new Archive(
                "outward-expansion",
                ItemStack.with(lemon, 39),
                Seq.with(packagedLandingPodT0, launchPlatform)
        );
    }
}
