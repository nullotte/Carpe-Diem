package carpediem.content.blocks;

import carpediem.content.*;
import carpediem.world.blocks.campaign.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class CDCampaign {
    public static Block
    launchPlatformT1, launchPlatformT2,
    dataChannel, dataRouter,
    archiveDecoder;

    public static void load() {
        launchPlatformT1 = new LaunchPlatform("launch-platform-t1") {{
            requirements(Category.effect, BuildVisibility.campaignOnly, ItemStack.with());
            size = 5;
            itemCapacity = 100;
            regionSuffix = "-dark";

            launchBlocks.add(CDStorage.landingPodT0);

            consumeItem(CDItems.sulfur, 100);
        }};

        dataChannel = new DataChannel("data-channel") {{
            requirements(Category.effect, BuildVisibility.shown, ItemStack.with());
        }};

        dataRouter = new DataRouter("data-router") {{
            requirements(Category.effect, BuildVisibility.shown, ItemStack.with());
        }};

        archiveDecoder = new ArchiveResearchBlock("archive-decoder") {{
            requirements(Category.effect, BuildVisibility.shown, ItemStack.with());
            size = 4;

            consumePower(1f);
        }};

        new DataSource("awesome") {{
            requirements(Category.effect, BuildVisibility.sandboxOnly, ItemStack.with());
        }};
    }
}
