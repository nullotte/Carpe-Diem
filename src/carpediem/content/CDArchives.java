package carpediem.content;

import arc.struct.*;
import carpediem.type.*;
import mindustry.type.*;

import static carpediem.content.CDItems.*;
import static carpediem.content.blocks.CDCampaign.*;
import static carpediem.content.blocks.CDCrafting.*;
import static carpediem.content.blocks.CDDistribution.*;
import static carpediem.content.blocks.CDLiquidBlocks.*;
import static carpediem.content.blocks.CDPayloadComponents.*;
import static carpediem.content.blocks.CDPayloads.*;
import static carpediem.content.blocks.CDPower.*;
import static carpediem.content.blocks.CDProduction.*;
import static carpediem.content.blocks.CDStorage.*;

public class CDArchives {
    public static Archive
    // the reserve
    distribution, automation, outwardExpansion,
    // forward outpost
    fluidProcessing, powerProduction, payloadLogistics, industrialStorage, manufacturingComponents, navigationSystems;

    public static void load() {
        // region the reserve
        distribution = new Archive(
                "distribution",
                ItemStack.with(
                        aluminumPlate, 50,
                        aluminumCogwheel, 50
                ),
                Seq.with(
                        beltSplitter,
                        beltOverflowGate,
                        beltUnderflowGate,
                        beltBridge
                )
        );

        automation = new Archive(
                "automation",
                ItemStack.with(
                        controlCircuit, 25
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
                        lemon, 39
                ),
                Seq.with(
                        packagedLandingPodT0,
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
                        pump
                        // TODO everything
                )
        );

        powerProduction = new Archive(
                "power-production",
                ItemStack.with(
                        lemon, 39
                ),
                Seq.with(
                        // TODO steamgen
                        cableTower,
                        accumulator
                )
        );

        payloadLogistics = new Archive(
                "payload-logistics",
                ItemStack.with(lemon, 39),
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
                        lemon, 39
                ),
                Seq.with(
                        industryHub,
                        storageVault
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
    }
}
