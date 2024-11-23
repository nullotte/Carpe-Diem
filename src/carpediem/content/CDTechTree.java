package carpediem.content;

import arc.struct.*;
import carpediem.game.CDObjectives.*;
import mindustry.content.*;
import mindustry.world.*;

import static mindustry.content.TechTree.*;
import static carpediem.content.blocks.CDCrafting.*;
import static carpediem.content.blocks.CDDistribution.*;
import static carpediem.content.blocks.CDPayloads.*;
import static carpediem.content.blocks.CDPower.*;
import static carpediem.content.blocks.CDProduction.*;
import static carpediem.content.blocks.CDStorage.*;
import static carpediem.content.blocks.CDCampaign.*;
import static carpediem.content.CDItems.*;

public class CDTechTree {
    public static Seq<Block> blocks = new Seq<>();

    public static void load() {
        TechNode root = nodeRoot("planet", landingPodT0, () -> {
            node(belt, () -> {
                node(beltMerger, () -> {
                    node(beltSplitter);
                    node(beltOverflowGate, () -> {
                        node(beltUnderflowGate);
                    });
                    node(beltBridge);
                });
            });

            node(smelterT0, () -> {
                node(pressT0);
                node(rollingMillT0, () -> {
                    node(cableNode, () -> {
                        node(cableTower);
                    });
                });
                node(assemblerT0, () -> {
                    node(smelterT1);
                    node(pressT1);
                    node(rollingMillT1);
                    node(refineryT1, () -> {
                        node(assemblerT1);
                    });
                });
                node(drillT0, () -> {
                    node(drillT1);
                });
            });

            node(industryHub, () -> {
                node(storageVault);
                node(storageRelay);
            });

            node(launchPlatformT1);

            node(payloadAssembler, () -> {
                node(payloadRail, () -> {
                    node(payloadRailRouter);
                    node(payloadCrane);
                });
                node(payloadManufacturingPlant, () -> {
                    node(payloadManufacturingGrid);
                });
            });

            node(CDSectorPresets.one, () -> {
                node(CDSectorPresets.two, Seq.with(new LaunchSector(landingPodT0)), () -> {});
            });

            nodeProduce(rawAluminum, () -> {
                nodeProduce(waterIce, () -> {
                    nodeProduce(Liquids.water, () -> {
                        nodeProduce(CDLiquids.petroleum, () -> {
                            nodeProduce(Liquids.oil, () -> {
                                nodeProduce(Items.silicon, () -> {
                                    nodeProduce(siliconSheet, () -> {});
                                });
                                nodeProduce(Items.pyratite, () -> {});
                                nodeProduce(sturdyAlloy, () -> {
                                    nodeProduce(alloyPlate, () -> {
                                        nodeProduce(alloyCogwheel, () -> {});
                                    });
                                    nodeProduce(alloyRod, () -> {});
                                });
                                nodeProduce(Items.plastanium, () -> {
                                    nodeProduce(plastaniumSheet, () -> {});
                                });
                            });
                        });
                    });
                });

                nodeProduce(sulfur, () -> {
                    nodeProduce(aluminum, () -> {
                        nodeProduce(aluminumPlate, () -> {
                            nodeProduce(aluminumCogwheel, () -> {});
                        });
                        nodeProduce(aluminumRod, () -> {
                            nodeProduce(aluminumWire, () -> {});
                        });

                        nodeProduce(nickel, () -> {
                            nodeProduce(nickelPlate, () -> {});
                            nodeProduce(nickelRod, () -> {
                                nodeProduce(nickelWire, () -> {
                                    nodeProduce(controlCircuit, () -> {
                                        nodeProduce(calculationCircuit, () -> {
                                            nodeProduce(processingUnit, () -> {});
                                        });
                                    });

                                    nodeProduce(powerCell, () -> {});
                                    nodeProduce(electronicMotor, () -> {});
                                });
                            });

                            nodeProduce(silver, () -> {
                                nodeProduce(silverPlate, () -> {
                                    nodeProduce(silverCogwheel, () -> {});

                                    nodeProduce(liquidCell, () -> {});
                                });
                                nodeProduce(silverRod, () -> {});

                                nodeProduce(platinum, () -> {
                                    nodeProduce(platinumPlate, () -> {});
                                    nodeProduce(platinumRod, () -> {});
                                });
                            });
                        });
                    });
                });

                nodeProduce(Items.sand, () -> {});

                nodeProduce(rawNickel, () -> {});
                nodeProduce(rawSilver, () -> {});
                nodeProduce(rawPlatinum, () -> {});
            });
        });

        CDPlanets.planet.techTree = root;

        root.each(node -> {
            if (node.content instanceof Block block) {
                blocks.add(block);
            }
        });
    }
}