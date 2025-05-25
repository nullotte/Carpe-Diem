package carpediem.content;

import arc.struct.*;
import carpediem.type.*;
import mindustry.type.*;

import static carpediem.content.CDItems.*;
import static carpediem.content.blocks.CDCampaign.*;
import static carpediem.content.blocks.CDCrafting.*;
import static carpediem.content.blocks.CDLiquidBlocks.*;
import static carpediem.content.blocks.CDPayloadBlocks.*;
import static carpediem.content.blocks.CDPayloadComponents.*;
import static carpediem.content.blocks.CDPower.*;
import static carpediem.content.blocks.CDProduction.*;
import static carpediem.content.blocks.CDStorage.*;

public class CDArchives {
    public static Archive
    // the reserve
    automation, outwardExpansion,
    // forward outpost
    fluidProcessing, powerProduction, payloadLogistics, industrialStorage, advancedExpansion;

    public static void load() {
        // region the reserve
        automation = new Archive(
                "automation",
                ItemStack.with(
                        card1, 25
                ),
                Seq.with(
                        drillT1,
                        smelterT1,
                        pressT1,
                        rollingMillT1,
                        assemblerT1
                )
        );

        outwardExpansion = new Archive(
                "outward-expansion",
                ItemStack.with(
                        card1, 200
                ),
                Seq.with(
                        landingPodAssembler,
                        launchPlatform
                )
        );
        // endregion
        // region forward outpost
        fluidProcessing = new Archive(
                "fluid-processing",
                ItemStack.with(
                        card1, 250,
                        card2, 50
                ),
                Seq.with(
                        pump,
                        pipe,
                        valve,
                        pipeBridge,
                        fluidTank,
                        refineryT1
                )
        );

        powerProduction = new Archive(
                "power-production",
                ItemStack.with(
                        card1, 500,
                        card2, 250
                ),
                Seq.with(
                        steamBoiler,
                        cableTower,
                        accumulator
                )
        );

        payloadLogistics = new Archive(
                "payload-logistics",
                ItemStack.with(
                        card1, 500,
                        card2, 500
                ),
                Seq.with(
                        payloadRail,
                        payloadRailRouter,
                        payloadCrane,
                        payloadAssembler,
                        payloadManufacturingGrid,
                        payloadLoader,
                        payloadUnloader
                )
        );

        industrialStorage = new Archive(
                "industrial-storage",
                ItemStack.with(
                        card1, 1000,
                        card2, 750
                ),
                Seq.with(
                        industryHub,
                        storageVault,
                        shippingContainer,
                        incinerator
                )
        );

        advancedExpansion = new Archive(
                "advanced-expansion",
                ItemStack.with(
                        card1, 1500,
                        card2, 1000
                ),
                Seq.with(
                        landingPodFrame,
                        thruster,
                        boosterEngine,
                        storageCompartment,
                        landingPodT1
                )
        );
        // endregion
        Seq<Archive> unfinished = Seq.with();
        for (Archive archive : unfinished) {
            archive.show = false;
        }
    }
}
