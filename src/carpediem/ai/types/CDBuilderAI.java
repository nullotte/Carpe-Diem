package carpediem.ai.types;

import arc.util.*;
import mindustry.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.ConstructBlock.*;

// BuilderAI except it builds even if the unit it's following isn't able to
// it literally just replaces every usage of unit.isBuilding() with unit.isBuilding() and unit.updateBuilding
// This Sucks. :(
public class CDBuilderAI extends BuilderAI {
    // SIGHHHHHHHH
    boolean found;
    float retreatTimer;

    @Override
    public void updateMovement() {
        if (target != null && shouldShoot()) {
            unit.lookAt(target);
        } else if (!unit.type.flying) {
            unit.lookAt(unit.prefRotation());
        }

        unit.updateBuilding = true;

        if (assistFollowing != null && !assistFollowing.isValid()) assistFollowing = null;
        if (following != null && !following.isValid()) following = null;

        if (assistFollowing != null && assistFollowing.isBuilding() && assistFollowing.updateBuilding) {
            following = assistFollowing;
        }

        boolean moving = false;
        boolean hold = hasStance(UnitStance.holdPosition);

        if (following != null) {
            retreatTimer = 0f;
            //try to follow and mimic someone

            //validate follower
            if (!following.isValid() || !following.isBuilding() || !following.updateBuilding) {
                following = null;
                unit.plans.clear();
                return;
            }

            //set to follower's first build plan, whatever that is
            unit.plans.clear();
            unit.plans.addFirst(following.buildPlan());
            lastPlan = null;
        } else if ((unit.buildPlan() == null || alwaysFlee) && !hold) {
            //not following anyone or building
            if (timer.get(timerTarget4, 40)) {
                enemy = target(unit.x, unit.y, fleeRange, true, true);
            }

            //fly away from enemy when not doing anything, but only after a delay
            if ((retreatTimer += Time.delta) >= retreatDelay || alwaysFlee) {
                if (enemy != null) {
                    unit.clearBuilding();
                    var core = unit.closestCore();
                    if (core != null && !unit.within(core, retreatDst)) {
                        moveTo(core, retreatDst);
                        moving = true;
                    }
                }
            }
        }

        if (unit.buildPlan() != null) {
            if (!alwaysFlee) retreatTimer = 0f;
            //approach plan if building
            BuildPlan req = unit.buildPlan();

            //clear break plan if another player is breaking something
            if (!req.breaking && timer.get(timerTarget2, 40f)) {
                for (Player player : Groups.player) {
                    if (player.isBuilder() && player.unit().isBuilding() && player.unit().updateBuilding && player.unit().buildPlan().samePos(req) && player.unit().buildPlan().breaking) {
                        unit.plans.removeFirst();
                        //remove from list of plans
                        unit.team.data().plans.remove(p -> p.x == req.x && p.y == req.y);
                        return;
                    }
                }
            }

            boolean valid =
                    !(lastPlan != null && lastPlan.removed) &&
                            ((req.tile() != null && req.tile().build instanceof ConstructBuild cons && cons.current == req.block) ||
                                    (req.breaking ?
                                            Build.validBreak(unit.team(), req.x, req.y) :
                                            Build.validPlace(req.block, unit.team(), req.x, req.y, req.rotation)));

            if (valid) {
                if (!hold) {
                    float range = Math.min(unit.type.buildRange - unit.type.hitSize * 2f, buildRadius);
                    //move toward the plan
                    moveTo(req.tile(), range, 20f);
                    moving = !unit.within(req.tile(), range);
                } else if (!unit.within(req, unit.type.buildRange - Vars.tilesize) && !Vars.state.rules.infiniteResources) {
                    //discard the plan, it's too far away to reach while holding position. try the next one
                    unit.plans.removeFirst();
                    lastPlan = null;
                }
            } else {
                //discard invalid plan
                unit.plans.removeFirst();
                lastPlan = null;
            }
        } else {

            /*
            if (assistFollowing != null && !hold) {
                moveTo(assistFollowing, assistFollowing.type.hitSize + unit.type.hitSize / 2f + 60f);
                moving = !unit.within(assistFollowing, assistFollowing.type.hitSize + unit.type.hitSize / 2f + 65f);
            }
            */

            //follow someone and help them build
            if (timer.get(timerTarget2, 20f)) {
                found = false;

                Units.nearby(unit.team, unit.x, unit.y, buildRadius, u -> {
                    if (found) return;

                    if (u.canBuild() && u != unit && u.isBuilding() && u.updateBuilding) {
                        BuildPlan plan = u.buildPlan();

                        Building build = Vars.world.build(plan.x, plan.y);
                        if (build instanceof ConstructBuild cons) {
                            float dist = Math.min(cons.dst(unit) - unit.type.buildRange, 0);

                            //make sure you can reach the plan in time
                            if (dist / unit.speed() < cons.buildCost * 0.9f) {
                                following = u;
                                found = true;
                            }
                        }
                    }
                });

                if (onlyAssist) {
                    float minDst = Float.MAX_VALUE;
                    Player closest = null;
                    for (var player : Groups.player) {
                        if (!player.dead() && player.isBuilder() && player.team() == unit.team) {
                            float dst = player.dst2(unit);
                            if (dst < minDst) {
                                closest = player;
                                minDst = dst;
                            }
                        }
                    }

                    assistFollowing = closest == null ? null : closest.unit();
                }
            }

            //find new plan
            if (!onlyAssist && !unit.team.data().plans.isEmpty() && following == null && timer.get(timerTarget3, rebuildPeriod)) {
                var blocks = unit.team.data().plans;


                if (hold) {
                    //essentially build turret behavior (find first plan in range)
                    for (int i = 0; i < blocks.size; i++) {
                        var block = blocks.get(i);
                        if (Vars.state.rules.infiniteResources || unit.within(block.x * Vars.tilesize, block.y * Vars.tilesize, unit.type.buildRange)) {
                            var btype = block.block;

                            if (Build.validPlace(btype, unit.team(), block.x, block.y, block.rotation)) {
                                unit.addBuild(new BuildPlan(block.x, block.y, block.rotation, block.block, block.config));
                                //shift build plan to tail so next unit builds something else
                                blocks.addLast(blocks.removeIndex(i));
                                lastPlan = block;
                                break;
                            }
                        }
                    }
                } else {
                    BlockPlan block = blocks.first();

                    //check if it's already been placed
                    if (Vars.world.tile(block.x, block.y) != null && Vars.world.tile(block.x, block.y).block() == block.block) {
                        blocks.removeFirst();
                    } else if (Build.validPlace(block.block, unit.team(), block.x, block.y, block.rotation)
                            && (!alwaysFlee || !nearEnemy(block.x, block.y))) { //check if it's valid

                        lastPlan = block;
                        //add build plan
                        unit.addBuild(new BuildPlan(block.x, block.y, block.rotation, block.block, block.config));
                        //shift build plan to tail so next unit builds something else
                        blocks.addLast(blocks.removeFirst());
                    } else {
                        //shift head of queue to tail, try something else next time
                        blocks.addLast(blocks.removeFirst());
                    }
                }
            }
        }

        if (!unit.type.flying) {
            unit.updateBoosting(unit.type.boostWhenBuilding || moving || unit.floorOn().isDuct || unit.floorOn().damageTaken > 0f || unit.floorOn().isDeep());
        }
    }
}
