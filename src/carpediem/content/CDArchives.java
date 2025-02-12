package carpediem.content;

import arc.struct.*;
import arc.util.serialization.*;
import arc.util.serialization.Json.*;
import carpediem.type.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.world.*;

import static carpediem.content.CDItems.*;
import static carpediem.content.blocks.CDCampaign.*;
import static carpediem.content.blocks.CDCrafting.*;
import static carpediem.content.blocks.CDLiquidBlocks.*;
import static carpediem.content.blocks.CDPayloadComponents.*;
import static carpediem.content.blocks.CDPayloads.*;
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
                        // TODO steamgen
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

        // IT ONLY GETS WORSE YALL THIS IS THE SHIT I HAVE TO DO TO MAKE STATUS EFFECTS LOAD PROPERLY IN MAP OBJECTIVES
        // YOU THOUGHT SETTING THE SECTOR TO NULL AND THEN SETTING IT BACK TO WHAT IT WAS IN ORDER TO MAKE A RECIPE SHOW UP WAS BAD? HOOO FUCKING BOY
        JsonIO.json.setSerializer(UnlockableContent.class, new Serializer<>() {
            @Override
            public void write(Json json, UnlockableContent object, Class knownType) {
                json.writeValue(object == null ? null : object.name);
            }

            @Override
            public UnlockableContent read(Json json, JsonValue jsonData, Class type) {
                if (jsonData.isNull()) return null;
                String str = jsonData.asString();
                Item item = Vars.content.item(str);
                Liquid liquid = Vars.content.liquid(str);
                Block block = Vars.content.block(str);
                UnitType unit = Vars.content.unit(str);
                StatusEffect status = Vars.content.statusEffect(str);
                return
                        item != null ? item :
                        liquid != null ? liquid :
                        block != null ? block :
                        unit != null ? unit :
                        status;
            }
        });
    }
}
