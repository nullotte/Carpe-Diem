package carpediem.content;

import arc.struct.*;
import carpediem.type.*;
import mindustry.type.*;

import static carpediem.content.CDItems.*;
import static carpediem.content.blocks.CDCampaign.*;
import static carpediem.content.blocks.CDCrafting.*;
import static carpediem.content.blocks.CDLiquidBlocks.*;
import static carpediem.content.blocks.CDPayloadComponents.*;
import static carpediem.content.blocks.CDPayloadBlocks.*;
import static carpediem.content.blocks.CDPower.*;
import static carpediem.content.blocks.CDProduction.*;
import static carpediem.content.blocks.CDStorage.*;

public class CDArchives {
    public static Archive
    // the reserve
    automation, outwardExpansion,
    // forward outpost
    fluidProcessing, powerProduction, payloadLogistics, industrialStorage, manufacturingComponents, navigationSystems;

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
                        packagedLandingPodT0,
                        landingPodAssembler,
                        launchPlatform
                )
        );
        // endregion
        // region forward outpost
        fluidProcessing = new Archive(
                "fluid-processing",
                ItemStack.with(
                        lemon, 39
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
                        lemon, 39
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
                        lemon, 39
                ),
                Seq.with(
                        payloadRail,
                        payloadRailRouter,
                        payloadCrane,
                        payloadAssembler,
                        payloadDisassembler,
                        payloadManufacturingGrid,
                        payloadLoader,
                        payloadUnloader
                )
        );

        industrialStorage = new Archive(
                "industrial-storage",
                ItemStack.with(
                        lemon, 39
                ),
                Seq.with(
                        industryHub,
                        storageVault,
                        shippingContainer
                )
        );

        manufacturingComponents = new Archive(
                "manufacturing-components",
                ItemStack.with(
                        lemon, 39
                ),
                Seq.with(
                        // TODO
                )
        );

        navigationSystems = new Archive(
                "navigation-systems",
                ItemStack.with(
                        lemon, 39
                ),
                Seq.with(
                        // TODO
                )
        );
        // endregion
        Seq<Archive> unfinished = Seq.with(
                fluidProcessing, powerProduction, payloadLogistics, industrialStorage,
                manufacturingComponents, navigationSystems
        );
        for (Archive archive : unfinished) {
            archive.show = false;
        }
    }
}
