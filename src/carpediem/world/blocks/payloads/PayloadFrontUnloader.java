package carpediem.world.blocks.payloads;

import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.payloads.*;

public class PayloadFrontUnloader extends PayloadFrontLoader {
    public int offloadSpeed = 4;
    public float maxPowerUnload = 80f;

    public PayloadFrontUnloader(String name) {
        super(name);
        outputsPower = true;
        consumesPower = true;
        outputsLiquid = true;
        loadPowerDynamic = false;
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    @Override
    public boolean rotatedOutput(int x, int y) {
        return false;
    }

    public class PayloadFrontUnloaderBuild extends PayloadFrontLoaderBuild {
        public float lastOutputPower = 0f;

        @Override
        public boolean acceptItem(Building source, Item item) {
            return false;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return false;
        }

        @Override
        public float getPowerProduction() {
            return lastOutputPower;
        }

        @Override
        public void updateTile() {
            lastOutputPower = 0f;

            super.updateTile();

            dumpLiquid(liquids.current());
            for (int i = 0; i < offloadSpeed; i++) {
                dumpAccumulate();
            }
        }

        @Override
        public void actOnPayload(BuildPayload payload) {
            //unload items
            if (payload.block().hasItems && canFill()) {
                if (efficiency > 0.01f && timer(timerLoad, loadTime / efficiency)) {
                    //load up items a set amount of times
                    for (int j = 0; j < itemsLoaded && canFill(); j++) {
                        for (int i = 0; i < items.length(); i++) {
                            if (payload.build.items.get(i) > 0) {
                                Item item = Vars.content.item(i);
                                payload.build.items.remove(item, 1);
                                items.add(item, 1);
                                break;
                            }
                        }
                    }
                }
            }

            //unload liquids
            //TODO tile is null may crash
            if (payload.block().hasLiquids && payload.build.liquids.currentAmount() >= 0.01f &&
                    (liquids.current() == payload.build.liquids.current() || liquids.currentAmount() <= 0.2f)) {
                var liq = payload.build.liquids.current();
                float remaining = liquidCapacity - liquids.currentAmount();
                float flow = Math.min(Math.min(liquidsLoaded * edelta(), remaining), payload.build.liquids.currentAmount());

                liquids.add(liq, flow);
                payload.build.liquids.remove(liq, flow);
            }

            if (payloadHasBattery(payload)) {
                float cap = payload.block().consPower.capacity;
                float total = payload.build.power.status * cap;
                float unloaded = Math.min(maxPowerUnload * edelta(), total);
                lastOutputPower = unloaded;
                payload.build.power.status -= unloaded / cap;
            }
        }

        public boolean canFill() {
            return items.total() < itemCapacity;
        }
    }
}
