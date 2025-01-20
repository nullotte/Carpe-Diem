package carpediem.world.blocks.liquid;

import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.modules.*;

public class MergingLiquidBlock extends LiquidBlock {
    public static final Queue<MergingLiquidBuild> buildQueue = new Queue<>();
    // terrible
    public static final LiquidModule tmpLiquids = new LiquidModule();

    public MergingLiquidBlock(String name) {
        super(name);
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{bottomRegion, region};
    }

    public class MergingLiquidBuild extends LiquidBuild {
        public Seq<MergingLiquidBuild> chained = new Seq<>();
        public float totalCapacity;
        // idk if this is even an issue anymore
        public boolean removing;

        @Override
        public void updateTile() {
            if (liquids.currentAmount() > 0.01f) {
                moveLiquids();
            }
        }

        public void moveLiquids() {
            // dont bother if its only connected to other pipes
            if (proximity.contains(b -> !(b instanceof MergingLiquidBuild other) || other.chained != chained)) {
                dumpLiquid(liquids.current());
            }
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return (!(source instanceof MergingLiquidBuild other) || other.chained != chained) && (liquids.current() == liquid || liquids.currentAmount() < 0.2f);
        }

        @Override
        public void handleLiquid(Building source, Liquid liquid, float amount) {
            float frac = amount / totalCapacity;
            for (MergingLiquidBuild build : chained) {
                build.liquids.add(liquid, frac * build.block.liquidCapacity);
            }
        }

        // this has truly been a carpe diem
        @Override
        public void dumpLiquid(Liquid liquid, float scaling, int outputDir) {
            int dump = this.cdump;

            if (liquids.get(liquid) <= 0.0001f) return;

            if (!Vars.net.client() && Vars.state.isCampaign() && team == Vars.state.rules.defaultTeam) liquid.unlock();

            for (int i = 0; i < proximity.size; i++) {
                incrementDump(proximity.size);

                Building other = proximity.get((i + dump) % proximity.size);
                if (outputDir != -1 && (outputDir + rotation) % 4 != relativeTo(other)) continue;

                other = other.getLiquidDestination(self(), liquid);

                if (other != null && other.block.hasLiquids && canDumpLiquid(other, liquid) && other.liquids != null) {
                    float ofract = other.liquids.get(liquid) / other.block.liquidCapacity;
                    float fract = 0f;

                    for (MergingLiquidBuild build : chained) {
                        fract += build.liquids.get(liquid) / build.block.liquidCapacity;
                    }

                    if (ofract < fract) {
                        transferLiquid(other, (fract - ofract) * totalCapacity / scaling, liquid);
                    }
                }
            }
        }

        @Override
        public void transferLiquid(Building next, float amount, Liquid liquid) {
            float flow = Math.min(next.block.liquidCapacity - next.liquids.get(liquid), amount);
            if (next.acceptLiquid(this, liquid)) {
                next.handleLiquid(this, liquid, flow);

                float frac = flow / totalCapacity;
                for (MergingLiquidBuild build : chained) {
                    build.liquids.remove(liquid, frac * build.block.liquidCapacity);
                }
            }
        }

        @Override
        public void onProximityAdded() {
            super.onProximityAdded();

            updateChained();
        }

        @Override
        public void onProximityRemoved() {
            super.onProximityRemoved();

            removing = true;
            for (Building b : proximity) {
                if (b instanceof MergingLiquidBuild other) {
                    other.updateChained();
                }
            }
            removing = false;
        }

        public void updateChained() {
            Seq<MergingLiquidBuild> prev = chained;

            float capacity = 0f;
            chained = new Seq<>();
            buildQueue.clear();
            buildQueue.add(this);

            while (!buildQueue.isEmpty()) {
                MergingLiquidBuild next = buildQueue.removeLast();
                chained.add(next);
                prev.remove(next);
                capacity += next.block.liquidCapacity;

                for (Building b : next.chainTargets()) {
                    if (b instanceof MergingLiquidBuild other && other.chainTargets().contains(next) && !(other.removing) && other.chained != chained) {
                        other.chained = chained;
                        buildQueue.addFirst(other);
                    }
                }
            }

            for (MergingLiquidBuild build : chained) {
                build.totalCapacity = capacity;
            }

            for (MergingLiquidBuild build : prev) {
                build.updateChained();
            }

            // rebalance liquids
            tmpLiquids.clear();

            for (MergingLiquidBuild build : chained) {
                build.liquids.each(tmpLiquids::add);
            }

            tmpLiquids.each((liquid, amount) -> {
                float frac = amount / totalCapacity;
                for (MergingLiquidBuild build : chained) {
                    build.liquids.set(liquid, frac * build.block.liquidCapacity);
                }
            });
        }

        public Seq<Building> chainTargets() {
            return proximity;
        }

        @Override
        public void draw() {
            Draw.rect(bottomRegion, x, y);

            if (liquids.currentAmount() > 0.001f) {
                drawTiledFrames(size, x, y, 0f, liquids.current(), liquids.currentAmount() / liquidCapacity);
            }

            Draw.rect(region, x, y);
        }
    }
}
