package carpediem.world.blocks.distribution;

import arc.graphics.g2d.*;
import arc.util.*;
import carpediem.world.draw.DrawBeltUnder.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

public class Merger extends Block {
    public float speed = 5f;
    public DrawBlock drawer;

    public Merger(String name) {
        super(name);

        group = BlockGroup.transportation;
        update = true;
        solid = false;
        hasItems = true;
        unloadable = false;
        itemCapacity = 1;
        noUpdateDisabled = true;
        rotate = true;
        underBullets = true;
        priority = TargetPriority.transport;
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(Stat.itemsMoved, 60f / speed * itemCapacity, StatUnit.itemsSecond);
    }

    @Override
    public void load() {
        super.load();
        drawer.load(this);
    }

    @Override
    public TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    public class MergerBuild extends Building implements BeltUnderBlending {
        public float progress;
        public int[] blendInputs = new int[4], blendOutputs = new int[4];

        @Override
        public void updateTile() {
            progress += edelta() / speed * 2f;

            if (items.any()) {
                Item next = items.first();
                if (progress >= (1f - 1f / speed) && moveForward(next)) {
                    items.remove(next, 1);
                    progress %= (1f - 1f / speed);
                }
            } else {
                progress = 0f;
            }
        }

        @Override
        public int[] blendInputs() {
            return blendInputs;
        }

        @Override
        public int[] blendOutputs() {
            return blendOutputs;
        }

        @Override
        public void buildBlending(Building build) {
            buildBlending(this, -1, rotation);
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();
            buildBlending(this);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return items.total() < itemCapacity && relativeToEdge(source.tile) != rotation;
        }

        @Override
        public void draw() {
            drawer.draw(this);
        }
    }
}
