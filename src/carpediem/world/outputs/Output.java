package carpediem.world.outputs;

import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

// yup
public abstract class Output {
    public void apply(Block block) {}

    public void trigger(Building build) {}
    public void update(Building build) {}
    public void dump(Building build) {}
    public void dumpTimed(Building build) {}

    public void display(Stats stats) {}

    public boolean full(Building build) {
        return false;
    }
}
