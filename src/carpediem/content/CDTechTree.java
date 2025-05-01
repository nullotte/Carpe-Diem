package carpediem.content;

import arc.struct.*;
import carpediem.game.CDObjectives.*;
import mindustry.content.*;
import mindustry.game.Objectives.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static carpediem.content.CDArchives.*;
import static carpediem.content.CDItems.*;
import static carpediem.content.blocks.CDCampaign.*;
import static carpediem.content.blocks.CDCrafting.*;
import static carpediem.content.blocks.CDDistribution.*;
import static carpediem.content.blocks.CDLiquidBlocks.*;
import static carpediem.content.blocks.CDPayloadComponents.*;
import static carpediem.content.blocks.CDPayloadBlocks.*;
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
                    node(beltOverflowGate);
                    node(beltBridge, () -> {
                        node(providerContainer, () -> {
                            node(receiverContainer);
                        });
                    });
                    node(beltUnloader);
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
                    node(steamBoiler);
                });

                node(smelterT1, () -> {
                    node(pressT1);
                    node(rollingMillT1);
                    node(assemblerT1);
                    node(refineryT1, () -> {
                        node(pressurizationChamber);
                        node(incinerator);
                    });
                });

                node(pump, () -> {
                    node(pipe, () -> {
                        node(valve);
                        node(pipeBridge);
                        node(fluidTank);
                    });
                });
            });

            node(launchPlatform, () -> {
                node(landingPodAssembler);
                node(landingPodT1);
            });

            node(industryHub, () -> {
                node(storageVault);
                node(shippingContainer);
            });

            node(payloadAssembler, () -> {
                node(payloadRail, () -> {
                    node(payloadRailRouter);
                    node(payloadLoader, () -> {
                        node(payloadUnloader);
                    });
                    node(payloadCrane);
                });
                node(payloadDisassembler);
                node(payloadManufacturingGrid);
            });

            node(archiveScanner, () -> {
                node(archiveDecoder);
                node(dataChannel, () -> {
                    node(dataRouter);
                });
            });

            node(CDSectorPresets.theReserve, () -> {
                node(CDSectorPresets.forwardOutpost, Seq.with(new LaunchSector(landingPodT0)), () -> {
                    node(fluidProcessing);
                    node(powerProduction);
                    node(payloadLogistics);
                    node(industrialStorage);
                    node(manufacturingComponents);
                    node(navigationSystems);
                });
                node(automation);
                node(outwardExpansion);
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

        // should be able to build this right after obtaining raw aluminum. the player is told to mine sulfur immediately afterwards
        smelterT0.techNode.objectives.remove(objective -> objective instanceof Research research && research.content == sulfur);

        root.each(node -> {
            if (node.content instanceof Block block) {
                blocks.add(block);
                // items should only be used by the archive decoder
                node.setupRequirements(ItemStack.empty);
            }
        });

        blocks.addAll(packagedLandingPodT0);

        for (Block block : blocks) {
            if (block.fogRadius < 0) {
                block.fogRadius = block.size * 10;
                block.flags = block.flags.with(BlockFlag.hasFogRadius);
            }
        }
    }

}
