package carpediem.content.blocks;

import arc.graphics.*;
import arc.struct.*;
import carpediem.content.*;
import carpediem.world.blocks.campaign.*;
import carpediem.world.draw.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

public class CDCampaign {
    public static Block
    launchPlatform,
    dataChannel, dataRouter,
    archiveDecoder, archiveScanner, archiveVault;

    public static void load() {
        launchPlatform = new LaunchPlatform("launch-platform") {{
            requirements(Category.effect, BuildVisibility.campaignOnly, ItemStack.with(
                    CDItems.aluminum, 1500,
                    CDItems.aluminumPlate, 1000,
                    CDItems.aluminumRod, 500,
                    CDItems.aluminumCogwheel, 200,
                    CDItems.nickelRod, 400,
                    CDItems.nickelWire, 500,
                    CDItems.powerCell, 250,
                    CDItems.controlCircuit, 100
            ));
            size = 7;
            itemCapacity = 100;
            regionSuffix = "-dark";

            consumeItem(CDItems.sulfur, 100);
        }};

        dataChannel = new DataChannel("data-channel") {{
            requirements(Category.effect, BuildVisibility.campaignOnly, ItemStack.with(
                    CDItems.aluminumPlate, 1,
                    CDItems.nickelWire, 1
            ));
        }};

        dataRouter = new DataRouter("data-router") {{
            requirements(Category.effect, BuildVisibility.campaignOnly, ItemStack.with(
                    CDItems.nickel, 2,
                    CDItems.aluminumPlate, 2,
                    CDItems.nickelWire, 2
            ));
        }};

        archiveDecoder = new ArchiveResearchBlock("archive-decoder") {{
            requirements(Category.effect, BuildVisibility.campaignOnly, ItemStack.with(
                    CDItems.aluminum, 15,
                    CDItems.aluminumPlate, 15,
                    CDItems.nickelWire, 10,
                    CDItems.powerCell, 5,
                    CDItems.controlCircuit, 10
            ));
            size = 4;
            itemCapacity = 50;
            // this sucks
            filter = Seq.with(CDItems.card1, CDItems.card2, CDItems.card3, CDItems.card4);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawWarmupRegion() {{
                        color = Pal.sapBullet;
                    }},
                    new DrawCirclingLines(),
                    new DrawPulseShape(false) {{
                        color = Pal.sapBullet;
                        radiusScl = 0.55f;
                    }},
                    new DrawDefault(),
                    new DrawGlowRegion() {{
                        color = Color.valueOf("79aded");
                    }}
            );

            consumePower(1f);
        }};

        archiveScanner = new Scanner("archive-scanner") {{
            requirements(Category.effect, BuildVisibility.campaignOnly, ItemStack.with(
                    CDItems.aluminum, 20,
                    CDItems.aluminumPlate, 15,
                    CDItems.aluminumWire, 10,
                    CDItems.nickelWire, 5,
                    CDItems.powerCell, 5,
                    CDItems.controlCircuit, 15
            ));
            size = 3;

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawWarmupRegion() {{
                        color = Pal.sapBulletBack;
                    }},
                    new DrawRegion(),
                    new DrawGlowRegion(true) {{
                        color = Color.valueOf("79aded");
                    }},
                    new DrawScanner(),
                    new DrawRotatedRegion(true)
            );

            consumePower(1f);
        }};

        archiveVault = new ArchiveVault("archive-vault") {{
            requirements(Category.effect, BuildVisibility.editorOnly, ItemStack.with());
            size = 5;

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawWarmupRegion() {{
                        color = Pal.sapBullet;
                    }},
                    new DrawDefault(),
                    new DrawTeam()
            );
        }};
    }
}
