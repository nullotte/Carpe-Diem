package carpediem.content;

import arc.struct.*;
import mindustry.content.*;
import mindustry.world.*;

import static mindustry.content.TechTree.*;
import static carpediem.content.blocks.CDCrafting.*;
import static carpediem.content.blocks.CDDistribution.*;
import static carpediem.content.blocks.CDPower.*;
import static carpediem.content.blocks.CDStorage.*;
import static carpediem.content.CDItems.*;

public class CDTechTree {
    public static Seq<Block> blocks = new Seq<>();

    public static void load() {
        TechNode root = nodeRoot("planet", landingPodT0, () -> {
            node(belt, () -> {
                node(beltMerger, () -> {
                    node(beltSplitter);
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
                        node(mixerT1);
                        node(assemblerT1);
                    });
                });
            });

            node(industryHub);

            nodeProduce(rawAluminum, () -> {
                nodeProduce(waterIce, () -> {
                    nodeProduce(Liquids.water, () -> {
                        nodeProduce(biomass, () -> {
                            nodeProduce(charcoal, () -> {
                                nodeProduce(Items.pyratite, () -> {});
                                nodeProduce(Items.silicon, () -> {
                                    nodeProduce(siliconSheet, () -> {});
                                });
                            });
                        });

                        nodeProduce(CDLiquids.petroleum, () -> {
                            nodeProduce(Liquids.oil, () -> {
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
                                nodeProduce(silverRod, () -> {
                                    nodeProduce(silverWire, () -> {});
                                });

                                nodeProduce(platinum, () -> {
                                    nodeProduce(platinumPlate, () -> {});
                                    nodeProduce(platinumRod, () -> {
                                        nodeProduce(platinumWire, () -> {});
                                    });
                                });

                                nodeProduce(carbonAlloy, () -> {
                                    nodeProduce(alloyPlate, () -> {
                                        nodeProduce(alloyCogwheel, () -> {});
                                    });
                                    nodeProduce(alloyRod, () -> {});
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
