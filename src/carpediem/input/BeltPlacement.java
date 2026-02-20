package carpediem.input;

import arc.func.*;
import arc.math.geom.*;
import arc.struct.*;
import carpediem.world.blocks.liquid.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.input.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;

import static mindustry.Vars.*;

// i do not know what i am doing
public class BeltPlacement {
    private static final Seq<BuildPlan> plans1 = new Seq<>();
    private static final Seq<Point2> tmpPoints = new Seq<>(), tmpPoints2 = new Seq<>();

    public static void calculateBridges(Seq<BuildPlan> plans, Block bridge, Boolf<Block> same, boolean doOpposites) {
        if (plans.first().block.rotate && Placement.isSidePlace(plans)) return;

        // check for orthogonal placement + unlocked state
        if (!(plans.first().x == plans.peek().x || plans.first().y == plans.peek().y) || !bridge.unlockedNow()) {
            return;
        }

        Boolf<BuildPlan> rotated = plan -> plan.build() != null && same.get(plan.build().block) && (!plan.build().block.rotate || plan.rotation != plan.build().rotation);

        // idk
        Boolf<BuildPlan> placeable = plan ->
                !(rotated.get(plan)) &&
                        (plan.placeable(Vars.player.team()) ||
                                (plan.tile() != null && same.get(plan.tile().block())));

        Seq<BuildPlan> result = plans1.clear();

        outer:
        for (int i = 0; i < plans.size; ) {
            BuildPlan cur = plans.get(i);
            result.add(cur);

            // gap found
            if (i < plans.size - 1 && placeable.get(cur) && (!placeable.get(plans.get(i + 1)))) {

                // find the closest valid position within range
                for (int j = i + 2; j < plans.size; j++) {
                    BuildPlan other = plans.get(j);

                    // out of range now, set to current position and keep scanning forward for next occurrence
                    if ((bridge instanceof DirectionBridge dBridge && !dBridge.positionsValid(cur.x, cur.y, other.x, other.y)) || (bridge instanceof PipeBridge pBridge && !pBridge.positionsValid(cur.x, cur.y, other.x, other.y))) {
                        // add 'missed' conveyors
                        for (int k = i + 1; k < j; k++) {
                            result.add(plans.get(k));
                        }
                        i = j;
                        continue outer;
                    } else if (placeable.get(other)) {
                        // found a link, assign bridges
                        if (cur.block != bridge) {
                            cur.block = bridge;
                        } else {
                            cur.block = null;
                        }
                        other.block = bridge;

                        if (doOpposites) {
                            // ???????????
                            if (cur.x < other.x) {
                                cur.rotation = 0;
                            } else if (cur.x > other.x) {
                                cur.rotation = 2;
                            } else if (cur.y < other.y) {
                                cur.rotation = 1;
                            } else if (cur.y > other.y) {
                                cur.rotation = 3;
                            }

                            other.rotation = (cur.rotation + 2) % 4;
                        } else {
                            other.config = true;
                        }

                        i = j;
                        continue outer;
                    }
                }

                // if it got here, that means nothing was found. this likely means there's a bunch of stuff at the end; add it and bail out
                for (int j = i + 1; j < plans.size; j++) {
                    result.add(plans.get(j));
                }
                break;
            } else {
                i++;
            }
        }

        result.removeAll(plan -> plan.block == null);

        plans.set(result);
    }

    public static void calculateBridges(Seq<BuildPlan> plans, Block bridge, Boolf<Block> same) {
        calculateBridges(plans, bridge, same, false);
    }

    // calculatenodes but like. different somehow. i cant explain it
    public static void calculateNodes(Seq<Point2> points, Block block, int rotation, Boolf2<Point2, Point2> overlapper) {
        Seq<Point2> base = tmpPoints2, result = tmpPoints.clear();

        base.selectFrom(points, p -> p == points.first() || p == points.peek() || Build.validPlace(block, player.team(), p.x, p.y, rotation));
        boolean addedLast = false;
        boolean out = true;

        outer:
        for (int i = 0; i < base.size; ) {
            Point2 point = base.get(i);
            result.add(point);
            if (i == base.size - 1) addedLast = true;
            out = !out;

            //find the furthest node that overlaps this one
            for (int j = base.size - 1; j > i; j--) {
                var other = base.get(j);
                boolean over = overlapper.get(point, other);

                if (!out && over) {
                    //add node to list and start searching for node that overlaps the next one
                    i = j;
                    continue outer;
                }
            }

            //if it got here, that means nothing was found. try to proceed to the next node anyway
            i++;
        }

        if (!addedLast && !base.isEmpty()) result.add(base.peek());

        points.clear();
        points.addAll(result);
    }
}
