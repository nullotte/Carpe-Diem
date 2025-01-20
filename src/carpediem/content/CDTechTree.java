package carpediem.content;

import arc.struct.*;
import carpediem.game.CDObjectives.*;
import mindustry.content.*;
import mindustry.world.*;

import static carpediem.content.CDArchives.*;
import static carpediem.content.CDItems.*;
import static carpediem.content.blocks.CDCampaign.*;
import static carpediem.content.blocks.CDCrafting.*;
import static carpediem.content.blocks.CDDistribution.*;
import static carpediem.content.blocks.CDPayloads.*;
import static carpediem.content.blocks.CDPower.*;
import static carpediem.content.blocks.CDProduction.*;
import static carpediem.content.blocks.CDStorage.*;
import static mindustry.content.TechTree.*;

public class CDTechTree {
    public static Seq<Block> blocks = new Seq<>();

    public static void load() {
        TechNode root = nodeRoot(CDPlanets.asphodel.name, landingPodT0, () -> {
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
                node(drillT0, () -> {
                    node(drillT1);
                });

                node(geothermalBurner, () -> {
                    node(cableNode, () -> {
                        node(accumulator);
                        node(cableTower);
                    });
                });

                node(smelterT1, () -> {
                    node(pressT1);
                    node(rollingMillT1);
                    node(assemblerT1);
                    node(refineryT1);
                });
            });

            node(launchPlatform);

            node(industryHub, () -> {
                node(storageVault);
            });

            node(payloadAssembler, () -> {
                node(payloadRail, () -> {
                    node(payloadRailRouter);
                    node(payloadLoader, () -> {
                        node(payloadUnloader);
                    });
                    node(payloadCrane);
                });
                node(payloadManufacturingGrid);
            });

            node(archiveDecoder, () -> {
                node(dataChannel, () -> {
                    node(dataRouter);
                });

                node(automation);
                node(outwardExpansion);
            });

            node(CDSectorPresets.one, () -> {
                node(CDSectorPresets.two, Seq.with(new LaunchSector(landingPodT0)), () -> {});
            });

            nodeProduce(rawAluminum, () -> {
                nodeProduce(Liquids.water, () -> {
                    nodeProduce(CDLiquids.petroleum, () -> {
                        nodeProduce(Liquids.oil, () -> {
                            nodeProduce(tar, () -> {});
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

                nodeProduce(sulfur, () -> {
                    nodeProduce(aluminum, () -> {
                        nodeProduce(aluminumPlate, () -> {
                            nodeProduce(aluminumCogwheel, () -> {});
                            nodeProduce(card1, () -> {
                                nodeProduce(card2, () -> {
                                    nodeProduce(card3, () -> {
                                        nodeProduce(card4, () -> {});
                                    });
                                });
                            });
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
                                });
                            });

                            nodeProduce(silver, () -> {
                                nodeProduce(silverPlate, () -> {
                                    nodeProduce(silverCogwheel, () -> {});

                                    nodeProduce(liquidCell, () -> {});
                                });
                                nodeProduce(silverRod, () -> {});

                                nodeProduce(Liquids.slag, () -> {});

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

        CDPlanets.asphodel.techTree = root;

        root.each(node -> {
            if (node.content instanceof Block block) {
                blocks.add(block);
            }
        });
    }

}
