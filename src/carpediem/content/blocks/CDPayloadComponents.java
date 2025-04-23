package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.payloads.*;
import carpediem.world.blocks.payloads.PayloadManufacturingGrid.*;
import carpediem.world.blocks.storage.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;

public class CDPayloadComponents {
    public static Block
    packagedLandingPodT0, packagedLandingPodT1,
    // yea i guess these go here too
    blockAluminum, blockNickel, blockSilver, blockPlatinum,
    blockSulfur, blockTar, fuelBrick;

    public static ProcessableBlock blockRawAluminum, blockRawNickel, blockRawSilver, blockRawPlatinum;

    public static void load() {
        packagedLandingPodT0 = new PackagedCoreBlock("packaged-landing-pod-t0", CDStorage.landingPodT0);
        ((SingleBlockProducer) CDPayloadBlocks.landingPodAssembler).result = packagedLandingPodT0;
        packagedLandingPodT1 = new PackagedCoreBlock("packaged-landing-pod-t1", CDStorage.landingPodT1);

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

        blockSulfur = new SyntheticBlock("block-sulfur") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.sulfur, 50
            ));
            size = 3;
        }};

        blockTar = new SyntheticBlock("block-tar") {{
            requirements(Category.crafting, ItemStack.with(
                    CDItems.tar, 50
            ));
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

        fuelBrick = new SyntheticBlock("fuel-brick") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;
            ((PayloadBurner) CDPayloadBlocks.bulkHeater).consumedBlock = this;
        }};

        // fuel brick recipe
        ((PayloadManufacturingGrid) CDPayloadBlocks.payloadManufacturingGrid).recipes.add(
                new PayloadManufacturingRecipe(fuelBrick, PayloadStack.with(blockSulfur, 2, blockTar, 2))
        );
    }
}
