package carpediem.world.blocks.distribution;

import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import carpediem.content.blocks.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;

// this is a fucking shitshow . this entire mod is a fucking shitshow
public class Belt extends Conveyor {
    public Block mergerReplacement;

    public Belt(String name) {
        super(name);
    }

    @Override
    public boolean blends(Tile tile, int rotation, BuildPlan[] directional, int direction, boolean checkWorld) {
        boolean planBlend = true;

        for (int i = 0; i < 4; i++) {
            int realDir = Mathf.mod(rotation - i, 4);
            boolean blendsDir = false;

            if (directional != null && directional[realDir] != null) {
                BuildPlan req = directional[realDir];
                if (blends(tile, rotation, req.x, req.y, req.rotation, req.block)) {
                    blendsDir = true;
                }
            } else if (checkWorld && blends(tile, rotation, i)) {
                blendsDir = true;
            }

            if (blendsDir) {
                if (direction != 0 && (i == 2 && direction != 2) || (i == 3 && direction != 3)) {
                    planBlend = false;
                    break;
                }
            } else if (i == direction) {
                planBlend = false;
                break;
            }
        }

        return planBlend;
    }

    @Override
    public void init() {
        Block replacement = bridgeReplacement;
        super.init();
        bridgeReplacement = replacement;
        if (mergerReplacement == null) mergerReplacement = CDDistribution.beltMerger;
    }

    @Override
    public Block getReplacement(BuildPlan req, Seq<BuildPlan> plans) {
        if (mergerReplacement == null) return this;

        if (req.tile() != null) {
            Boolf<Point2> cont = p -> {
                if (plans.contains(o -> o.x == req.x + p.x && o.y == req.y + p.y && blends(req.tile(), req.rotation, o.x, o.y, o.rotation, o.block))) {
                    return true;
                }

                Tile near = req.tile().nearby(p.x, p.y);
                return near != null && near.build != null && blends(req.tile(), req.rotation, near.x, near.y, near.build.rotation, near.build.block);
            };

            int in = 0;
            for (int i = 1; i < 4; i++) {
                if (cont.get(Geometry.d4(req.rotation + i))) {
                    in++;

                    if (in > 1) {
                        return mergerReplacement;
                    }
                }
            }
        }

        return this;
    }

    @Override
    public void handlePlacementLine(Seq<BuildPlan> plans) {
        Placement.calculateBridges(plans, (DuctBridge) bridgeReplacement, false, b -> b instanceof Conveyor || b instanceof StackConveyor);
    }

    public class BeltBuild extends ConveyorBuild {
        public int recDir;

        @Override
        public boolean acceptItem(Building source, Item item) {
            return super.acceptItem(source, item) && tile.relativeTo(Edges.getFacingEdge(source.tile(), tile)) == recDir;
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();

            if (blendbits == 0) {
                recDir = (rotation + 2) % 4;
            } else {
                if (blendscly == 1) {
                    recDir = (rotation + 1) % 4;
                } else {
                    recDir = (rotation + 3) % 4;
                }
            }
        }
    }
}
