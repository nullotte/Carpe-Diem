package carpediem.ai.types;

import arc.struct.*;
import mindustry.*;
import mindustry.ai.types.*;
import mindustry.gen.*;
import mindustry.world.meta.*;

// cargo ai that doesnt require the unit to be tethered
public class CDCargoAI extends CargoAI {
    public static ObjectSet<Building> pendingLoaders = new ObjectSet<>();

    public Building loadTarget;

    @Override
    public void updateMovement() {
        if (!unit.hasItem()) {
            if (loadTarget != null) {
                if (!loadTarget.isValid() || loadTarget.isPayload()) {
                    loadTarget = null;
                }
            }

            if (loadTarget == null && retarget()) {
                // this actually sucks
                for (Building build : Vars.indexer.getFlagged(unit.team, BlockFlag.extinguisher)) {
                    if (!pendingLoaders.contains(build)) {
                        findAnyTarget(build);
                        if (unloadTarget != null) {
                            loadTarget = build;
                            pendingLoaders.add(loadTarget);
                            break;
                        }
                    }
                }
            }

            if (loadTarget != null && unloadTarget != null) {
                moveTo(loadTarget, moveRange, moveSmoothing);

                // check if ready to pick up
                if (unit.within(loadTarget, transferRange)) {
                    Call.takeItems(loadTarget, itemTarget, Math.min(unit.type.itemCapacity, loadTarget.items.get(itemTarget)), unit);

                    pendingLoaders.remove(loadTarget);
                    loadTarget = null;
                }
            }
        } else { // the unit has an item, deposit it somewhere.

            // there may be no current target, try to find one
            if (unloadTarget == null) {
                if (retarget()) {
                    findDropTarget(unit.item(), 0, null);

                    // if there is not even a single place to unload, dump items.
                    if (unloadTarget == null) {
                        unit.clearItem();
                    }
                }
            } else {

                // what if some prankster reconfigures or picks up the target while the unit is moving? we can't have that!
                if (unloadTarget.item != itemTarget || unloadTarget.isPayload()) {
                    unloadTarget = null;
                    return;
                }

                moveTo(unloadTarget, moveRange, moveSmoothing);

                // deposit in bursts, unloading can take a while
                if (unit.within(unloadTarget, transferRange) && timer.get(timerTarget2, dropSpacing)) {
                    int max = unloadTarget.acceptStack(unit.item(), unit.stack.amount, unit);

                    // deposit items when it's possible
                    if (max > 0) {
                        noDestTimer = 0f;
                        Call.transferItemTo(unit, unit.item(), max, unit.x, unit.y, unloadTarget);

                        //try the next target later
                        if (!unit.hasItem()) {
                            targetIndex++;
                        }
                    } else if ((noDestTimer += dropSpacing) >= emptyWaitTime) {
                        // oh no, it's out of space - wait for a while, and if nothing changes, try the next destination

                        // next targeting attempt will try the next destination point
                        targetIndex = findDropTarget(unit.item(), targetIndex, unloadTarget) + 1;

                        // nothing found at all, clear item
                        if (unloadTarget == null) {
                            unit.clearItem();
                        }
                    }
                }
            }
        }
    }
}
