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
    orbitalCalculationCore, heatShield,
    // rocket components
    rocketSystemCore, auxiliaryFuelTank, solidRocketBooster,
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
                    CDItems.aluminum, 150,
                    CDItems.aluminumPlate, 100,
                    Items.silicon, 80
            ));
            size = 3;
        }};

        heavyThruster = new SyntheticBlock("heavy-thruster") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 70,
                    CDItems.aluminumPlate, 35,
                    CDItems.nickelWire, 20,
                    CDItems.nickelPlate, 10,
                    Items.silicon, 20
            ));
            size = 3;
        }};

        storageCompartment = new SyntheticBlock("storage-compartment") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 120,
                    CDItems.aluminumPlate, 90,
                    CDItems.aluminumRod, 50
            ));
            size = 3;
        }};

        portableBattery = new SyntheticBlock("portable-battery") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 50,
                    CDItems.aluminumPlate, 30,
                    CDItems.nickelWire, 25,
                    CDItems.nickelPlate, 20,
                    CDItems.powerCell, 100,
                    Items.silicon, 30
            ));
            size = 3;
        }};

        assemblyManifold = new SyntheticBlock("assembly-manifold") {{
            requirements(Category.units, ItemStack.with(
                    CDItems.aluminum, 80,
                    CDItems.aluminumPlate, 50,
                    CDItems.nickelWire, 20,
                    CDItems.powerCell, 20,
                    CDItems.controlCircuit, 50,
                    CDItems.calculationCircuit, 50,
                    Items.silicon, 50
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

        orbitalCalculationCore = new SyntheticBlock("orbital-calculation-core") {{
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
        // region rocket components
        rocketSystemCore = new SyntheticBlock("rocket-system-core") {{
            requirements(Category.units, BuildVisibility.sandboxOnly, ItemStack.with());
            size = 7;
        }};

        auxiliaryFuelTank = new SyntheticBlock("auxiliary-fuel-tank") {{
            requirements(Category.units, BuildVisibility.sandboxOnly, ItemStack.with());
            size = 7;
        }};

        solidRocketBooster = new SyntheticBlock("solid-rocket-booster") {{
            requirements(Category.units, BuildVisibility.sandboxOnly, ItemStack.with());
            size = 7;
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

        // recipe definitions
        {
            Block
                    lpf = landingPodFrame,
                    ht0 = heavyThruster,
                    sc0 = storageCompartment,
                    pb0 = portableBattery,
                    am0 = assemblyManifold,
                    df0 = droneFrame,
                    lt0 = lightThruster,
                    os0 = opticalSensor,
                    pc0 = processingCore,
                    occ = orbitalCalculationCore,
                    hs0 = heatShield;

            ((PayloadManufacturingGrid) CDPayloadBlocks.payloadManufacturingGrid).recipes.addAll(
                    new PayloadManufacturingRecipe(packagedLandingPodT1, new UnlockableContent[][]{
                            {lpf, lpf, ht0, lpf, lpf},
                            {lpf, sc0, pb0, sc0, lpf},
                            {ht0, pb0, am0, pb0, ht0},
                            {lpf, sc0, pb0, sc0, lpf},
                            {lpf, lpf, ht0, lpf, lpf}
                    }),
                    new PayloadManufacturingRecipe(packagedLandingPodT2, new UnlockableContent[][]{
                            {hs0, lpf, ht0, ht0, lpf, hs0},
                            {lpf, sc0, occ, pc0, sc0, lpf},
                            {ht0, occ, am0, pb0, pc0, ht0},
                            {ht0, pc0, pb0, am0, occ, ht0},
                            {lpf, sc0, pc0, occ, sc0, lpf},
                            {hs0, lpf, ht0, ht0, lpf, hs0}
                    }),
                    new PayloadManufacturingRecipe(CDUnitTypes.carver, new UnlockableContent[][]{
                            {am0, os0, am0},
                            {df0, pc0, df0},
                            {df0, pb0, df0},
                            {null, lt0, null}
                    }),
                    new PayloadManufacturingRecipe(CDUnitTypes.heap, new UnlockableContent[][]{
                            {df0, os0, df0},
                            {sc0, pc0, sc0},
                            {df0, pb0, df0},
                            {null, lt0, null}
                    }),
                    new PayloadManufacturingRecipe(blockUnrefinedAlloy, PayloadStack.with(blockAluminum, 2, blockSilicon, 2)),
                    new PayloadManufacturingRecipe(rocketSystemCore, new UnlockableContent[][]{
                            {hs0, hs0, lpf, lpf, lpf, hs0, hs0},
                            {hs0, pb0, pc0, os0, pc0, pb0, hs0},
                            {lpf, pc0, am0, occ, am0, pc0, lpf},
                            {lpf, os0, occ, occ, occ, os0, lpf},
                            {lpf, pc0, am0, occ, am0, pc0, lpf},
                            {hs0, pb0, pc0, os0, pc0, pb0, hs0},
                            {hs0, hs0, lpf, lpf, lpf, hs0, hs0}
                    }),
                    new PayloadManufacturingRecipe(auxiliaryFuelTank, new UnlockableContent[][]{
                            {hs0, hs0, hs0, lpf, hs0, hs0, hs0},
                            {hs0, pb0, pb0, sc0, pb0, pb0, hs0},
                            {hs0, pb0, sc0, sc0, sc0, pb0, hs0},
                            {lpf, sc0, sc0, pc0, sc0, sc0, lpf},
                            {hs0, pb0, sc0, sc0, sc0, pb0, hs0},
                            {hs0, pb0, pb0, sc0, pb0, pb0, hs0},
                            {hs0, hs0, hs0, lpf, hs0, hs0, hs0}
                    }),
                    new PayloadManufacturingRecipe(solidRocketBooster, new UnlockableContent[][]{
                            {hs0, hs0, ht0, ht0, ht0, hs0, hs0},
                            {hs0, sc0, lpf, pb0, lpf, sc0, hs0},
                            {ht0, lpf, pc0, occ, pc0, lpf, ht0},
                            {ht0, pb0, occ, pc0, occ, pb0, ht0},
                            {ht0, lpf, pc0, occ, pc0, lpf, ht0},
                            {hs0, sc0, lpf, pb0, lpf, sc0, hs0},
                            {hs0, hs0, ht0, ht0, ht0, hs0, hs0}
                    })
            );
        }
    }
}
