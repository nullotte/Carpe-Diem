package carpediem.world.blocks.storage;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

// TODO is this balanced?
public class StorageRelay extends Block {
    public DrawBlock drawer;

    public StorageRelay(String name) {
        super(name);
        hasItems = true;
        solid = true;
        update = true;
        group = BlockGroup.transportation;
        flags = EnumSet.of(BlockFlag.storage);
    }

    @Override
    public boolean outputsItems() {
        return false;
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

    public class StorageRelayBuild extends Building {
        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return efficiency > 0f && core() != null && core().acceptItem(source, item);
        }

        @Override
        public void handleItem(Building source, Item item) {
            if (core() != null) {
                if (core().items.get(item) >= core().storageCapacity) {
                    StorageBlock.incinerateEffect(this, source);
                }
                core().noEffect = true;
                core().handleItem(source, item);
            }
        }
    }
}
