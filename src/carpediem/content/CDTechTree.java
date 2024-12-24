package carpediem.content;

import arc.struct.*;
import arc.util.*;
import carpediem.game.CDObjectives.*;
import mindustry.content.*;
import mindustry.game.Objectives.*;
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
import static carpediem.content.CDArchives.*;

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

                node(cableNode, () -> {
                    node(sulfurBurner, () -> {});

                    node(cableTower);
                });

                node(smelterT1, () -> {
                    node(pressT1);
                    node(rollingMillT1);
                    node(assemblerT1);
                    node(refineryT1);
                });
            });

            node(industryHub, () -> {
                node(storageVault);
            });

            node(launchPlatformT1);

            node(payloadAssembler, () -> {
                node(payloadRail, () -> {
                    node(payloadRailRouter);
                    node(payloadCrane);
                });
                node(payloadManufacturingGrid);
            });

            node(archiveDecoder, () -> {
                node(automation, () -> {});
            });

            node(CDSectorPresets.one, () -> {
                node(CDSectorPresets.two, Seq.with(new LaunchSector(landingPodT0)), () -> {});
            });

            nodeProduce(rawAluminum, () -> {
                nodeProduce(waterIce, () -> {
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

        CDPlanets.asphodel.techTree = root;

        root.each(node -> {
            if (node.content instanceof Block block) {
                blocks.add(block);

                if (block.requirements.length == 0) {
                    node.objectives.add(new Unfinished(block.localizedName));
                }
            }
        });
    }

    // TODO remove
    public static class Unfinished implements Objective {
        public String name;

        public Unfinished(String name) {
            this.name = name;
        }

        @Override
        public boolean complete() {
            return false;
        }

        @Override
        public String display() {
            return Strings.format("[scarlet]big obvious red text that indicates that this content (@) is not ready", name);
        }
    }
}
