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
import static carpediem.content.CDUnitTypes.*;

public class CDArchives {
    public static Archive
    // the reserve
    automation, outwardExpansion,
    // forward outpost
    fluidProcessing, powerProduction, payloadLogistics, industrialStorage, advancedExpansion,
    // interference
    pressurization, springLaunchers, drones, advancedPowerProduction,
    // sanctuary
    industrialExtraction, advancedPayloadLogistics, fanProcessing, planetaryExpansion;

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
                        card1, 500
                ),
                Seq.with(
                        pump,
                        pipe,
                        valve,
                        pipeBridge,
                        fluidTank,
                        refineryT1,
                        incinerator
                )
        );

        powerProduction = new Archive(
                "power-production",
                ItemStack.with(
                        card1, 1000,
                        card2, 500
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
                        card1, 1500,
                        card2, 1000
                ),
                Seq.with(
                        payloadRail,
                        payloadRailRouter,
                        payloadCrane,
                        payloadAssembler,
                        payloadManufacturingGrid,
                        payloadDepot,
                        payloadLoader,
                        payloadUnloader
                )
        );

        industrialStorage = new Archive(
                "industrial-storage",
                ItemStack.with(
                        card1, 2500,
                        card2, 2000
                ),
                Seq.with(
                        industryHub,
                        storageVault,
                        shippingContainer
                )
        );

        advancedExpansion = new Archive(
                "advanced-expansion",
                ItemStack.with(
                        card1, 5000,
                        card2, 2500
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
        // region interference
        pressurization = new Archive(
                "pressurization",
                ItemStack.with(
                        card1, 5000,
                        card2, 2500
                ),
                Seq.with(
                        pressurizationChamber
                )
        );

        springLaunchers = new Archive(
                "spring-launchers",
                ItemStack.with(
                        card1, 6000,
                        card2, 3000,
                        card3, 1000
                ),
                Seq.with(
                        springLauncher
                )
        );

        drones = new Archive(
                "drones",
                ItemStack.with(
                        card1, 7500,
                        card2, 5000,
                        card3, 5000
                ),
                Seq.with(
                        carver,
                        heap,
                        providerContainer,
                        receiverContainer
                )
        );

        advancedPowerProduction = new Archive(
                "advanced-power-production",
                ItemStack.with(
                        card1, 7500,
                        card2, 5000,
                        card3, 5000
                ),
                Seq.with(
                        compressionEngine
                )
        );
        // endregion
        // region sanctuary
        industrialExtraction = new Archive(
                "industrial-extraction",
                ItemStack.with(
                        card1, 4000,
                        card2, 2000,
                        card3, 1000
                ),
                Seq.with(
                        drillT2
                )
        );

        advancedPayloadLogistics = new Archive(
                "advanced-payload-logistics",
                ItemStack.with(
                        card1, 5000,
                        card2, 2500,
                        card3, 2500,
                        card4, 1000
                ),
                Seq.with(
                        myriad,
                        payloadDisassembler,
                        blockRawAluminum,
                        blockRawNickel,
                        blockRawSilver,
                        blockRawPlatinum,
                        blockUnrefinedAlloy,
                        blockAluminum,
                        blockNickel,
                        blockSilver,
                        blockPlatinum,
                        blockSturdyAlloy,
                        blockSilicon,
                        blockPyratite
                )
        );

        fanProcessing = new Archive(
                "fan-processing",
                ItemStack.with(
                        card1, 10000,
                        card2, 7500,
                        card3, 6000,
                        card4, 2500
                ),
                Seq.with(
                        hydraulicFan,
                        bulkHeater
                )
        );

        planetaryExpansion = new Archive(
                "planetary-expansion",
                ItemStack.with(
                        card1, 15000,
                        card2, 10000,
                        card3, 8000,
                        card4, 3000
                ),
                Seq.with(
                        // TODO !!!!!!!!!!!!!
                )
        );
        // endregion
    }
}
