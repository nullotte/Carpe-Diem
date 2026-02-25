package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.payloads.*;
import carpediem.world.blocks.payloads.PayloadManufacturingGrid.*;
import carpediem.world.blocks.storage.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

public class CDPayloadComponents {
    public static Block
    packagedLandingPodT0, packagedLandingPodT1, packagedLandingPodT2,
    // crafting ingredients
    landingPodFrame, heavyThruster, storageCompartment, portableBattery, assemblyManifold,
    droneFrame, lightThruster, opticalSensor, processingCore,
    orbitCalculationCore, heatShield,
    // yea i guess these go here too
    blockAluminum, blockNickel, blockSilver, blockPlatinum, blockSturdyAlloy,
    blockSilicon, blockPyratite;

    public static ProcessableBlock blockRawAluminum, blockRawNickel, blockRawSilver, blockRawPlatinum, blockUnrefinedAlloy;

    public static void load() {
        packagedLandingPodT0 = new PackagedCoreBlock("packaged-landing-pod-t0", CDStorage.landingPodT0);
        ((SingleBlockProducer) CDPayloadBlocks.landingPodAssembler).result = packagedLandingPodT0;
        packagedLandingPodT1 = new PackagedCoreBlock("packaged-landing-pod-t1", CDStorage.landingPodT1);
        packagedLandingPodT2 = new PackagedCoreBlock("packaged-landing-pod-t2", CDStorage.landingPodT2);

        // region crafting ingredients
        landingPodFrame = new SyntheticBlock("landing-pod-frame") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};

        heavyThruster = new SyntheticBlock("heavy-thruster") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};

        storageCompartment = new SyntheticBlock("storage-compartment") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};

        portableBattery = new SyntheticBlock("portable-battery") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};

        assemblyManifold = new SyntheticBlock("assembly-manifold") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};

        droneFrame = new SyntheticBlock("drone-frame") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};

        lightThruster = new SyntheticBlock("light-thruster") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};

        opticalSensor = new SyntheticBlock("optical-sensor") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};

        processingCore = new SyntheticBlock("processing-core") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};

        orbitCalculationCore = new SyntheticBlock("orbital-calculation-core") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};

        heatShield = new SyntheticBlock("heat-shield") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.lemon, 39
            ));
            size = 3;
        }};
        // endregion
        // region material blocks
        blockRawAluminum = new ProcessableBlock("block-raw-aluminum") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.rawAluminum, 50
            ));
            size = 3;
        }};

        blockRawNickel = new ProcessableBlock("block-raw-nickel") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.rawNickel, 50
            ));
            size = 3;
        }};

        blockRawSilver = new ProcessableBlock("block-raw-silver") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.rawSilver, 50
            ));
            size = 3;
        }};

        blockRawPlatinum = new ProcessableBlock("block-raw-platinum") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.rawPlatinum, 50
            ));
            size = 3;
        }};

        blockUnrefinedAlloy = new ProcessableBlock("block-unrefined-alloy") {{
            requirements(Category.crafting, BuildVisibility.sandboxOnly, ItemStack.with());
            size = 3;
        }};

        blockAluminum = new SyntheticBlock("block-aluminum") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.aluminum, 50
            ));
            size = 3;
            blockRawAluminum.resultBlock = this;
        }};

        blockNickel = new SyntheticBlock("block-nickel") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.nickel, 50
            ));
            size = 3;
            blockRawNickel.resultBlock = this;
        }};

        blockSilver = new SyntheticBlock("block-silver") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.silver, 50
            ));
            size = 3;
            blockRawSilver.resultBlock = this;
        }};

        blockPlatinum = new SyntheticBlock("block-platinum") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.platinum, 50
            ));
            size = 3;
            blockRawPlatinum.resultBlock = this;
        }};

        blockSturdyAlloy = new SyntheticBlock("block-sturdy-alloy") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.sturdyAlloy, 50
            ));
            size = 3;
            blockUnrefinedAlloy.resultBlock = this;
        }};

        blockSilicon = new SyntheticBlock("block-silicon") {{
            requirements(Category.crafting, ItemStack.with(
                    Items.silicon, 50
            ));
            size = 3;
        }};

        blockPyratite = new SyntheticBlock("block-pyratite") {{
            requirements(Category.crafting, ItemStack.with(
                    Items.pyratite, 50
            ));
            size = 3;
            ((PayloadBurner) CDPayloadBlocks.bulkHeater).consumedBlock = this;
        }};
        // endregion

        ((PayloadManufacturingGrid) CDPayloadBlocks.payloadManufacturingGrid).recipes.addAll(
                new PayloadManufacturingRecipe(packagedLandingPodT1, r -> {
                    Block
                            lpf = landingPodFrame,
                            ht = heavyThruster,
                            sc = storageCompartment,
                            pb = portableBattery,
                            am = assemblyManifold;
                    r.mapRequirements(new UnlockableContent[][]{
                            {lpf, lpf, ht, lpf, lpf},
                            {lpf, sc, pb, sc, lpf},
                            {ht, pb, am, pb, ht},
                            {lpf, sc, pb, sc, lpf},
                            {lpf, lpf, ht, lpf, lpf}
                    });
                }),
                new PayloadManufacturingRecipe(packagedLandingPodT2, r -> {
                    Block
                            lpf = landingPodFrame,
                            ht = heavyThruster,
                            sc = storageCompartment,
                            pb = portableBattery,
                            am = assemblyManifold,
                            pc = processingCore,
                            occ = orbitCalculationCore,
                            hs = heatShield;
                    r.mapRequirements(new UnlockableContent[][]{
                            {hs, lpf, ht, ht, lpf, hs},
                            {lpf, sc, occ, pc, sc, lpf},
                            {ht, occ, am, pb, pc, ht},
                            {ht, pc, pb, am, occ, ht},
                            {lpf, sc, pc, occ, sc, lpf},
                            {hs, lpf, ht, ht, lpf, hs}
                    });
                }),
                new PayloadManufacturingRecipe(CDUnitTypes.carver, r -> {
                    Block
                            lt = lightThruster,
                            pb = portableBattery,
                            am = assemblyManifold,
                            df = droneFrame,
                            os = opticalSensor,
                            pc = processingCore;
                    r.mapRequirements(new UnlockableContent[][]{
                            {am, os, am},
                            {df, pc, df},
                            {df, pb, df},
                            {null, lt, null}
                    });
                }),
                new PayloadManufacturingRecipe(CDUnitTypes.heap, r -> {
                    Block
                            lt = lightThruster,
                            sc = storageCompartment,
                            pb = portableBattery,
                            df = droneFrame,
                            os = opticalSensor,
                            pc = processingCore;
                    r.mapRequirements(new UnlockableContent[][]{
                            {df, os, df},
                            {sc, pc, sc},
                            {df, pb, df},
                            {null, lt, null}
                    });
                }),
                new PayloadManufacturingRecipe(blockUnrefinedAlloy, PayloadStack.with(blockAluminum, 2, blockSilicon, 2))
        );
    }
}
