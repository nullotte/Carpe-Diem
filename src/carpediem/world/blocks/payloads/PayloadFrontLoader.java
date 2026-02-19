package carpediem.world.blocks.payloads;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.sandbox.*;

public class PayloadFrontLoader extends Block {
    public final int timerLoad = timers++;

    public float loadTime = 2f;
    public int itemsLoaded = 8;
    public float liquidsLoaded = 40f;
    public float maxPowerConsumption = 40f;
    public boolean loadPowerDynamic = true;

    protected float basePowerUse = 0f;

    public TextureRegion previewRegion, topRegion, bridgeRegion, handRegion,
            topOutlineRegion, bridgeOutlineRegion, handOutlineRegion;

    public PayloadFrontLoader(String name) {
        super(name);
        update = true;
        solid = true;
        rotate = true;

        hasItems = true;
        hasLiquids = true;
        hasPower = true;
        itemCapacity = 100;
        liquidCapacity = 100f;

        drawArrow = false;
        outlineIcon = true;
        outlinedIcon = 1;
    }

    @Override
    public void init() {
        if (loadPowerDynamic) {
            basePowerUse = consPower != null ? consPower.usage : 0f;
            consumePowerDynamic(basePowerUse, (PayloadFrontLoaderBuild loader) -> loader.shouldConsume() ? (loader.getFrontPayload() instanceof BuildPayload payload && loader.payloadHasBattery(payload) ? maxPowerConsumption + basePowerUse : basePowerUse) : 0f);
        }

        super.init();
    }

    @Override
    public void load() {
        super.load();
        previewRegion = Core.atlas.find(name + "-preview"); // it's actually a copy of topRegion, it's just that this gets outlined
        topRegion = Core.atlas.find(name + "-top");
        bridgeRegion = Core.atlas.find(name + "-bridge");
        handRegion = Core.atlas.find(name + "-hand");
        topOutlineRegion = Core.atlas.find(name + "-top-outline");
        bridgeOutlineRegion = Core.atlas.find(name + "-bridge-outline");
        handOutlineRegion = Core.atlas.find(name + "-hand-outline");
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out) {
        out.add(topRegion, bridgeRegion, handRegion);
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{region, previewRegion};
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        float r = (plan.rotation * 90f) - 90f;
        float topScl = Mathf.sign(plan.rotation * 90f > 45f && plan.rotation * 90f < 225f);
        int dx = Geometry.d4x(plan.rotation), dy = Geometry.d4y(plan.rotation);

        Draw.rect(region, plan.drawx(), plan.drawy());

        Draw.xscl = topScl;
        Draw.rect(bridgeOutlineRegion, plan.drawx(), plan.drawy(), r);
        Draw.rect(topOutlineRegion, plan.drawx(), plan.drawy(), r);
        Draw.xscl = 1f;

        Draw.rect(handOutlineRegion, plan.drawx() + (dx * size * Vars.tilesize), plan.drawy() + (dy * size * Vars.tilesize));

        Draw.xscl = topScl;
        Draw.rect(bridgeRegion, plan.drawx(), plan.drawy(), r);
        Draw.rect(topRegion, plan.drawx(), plan.drawy(), r);
        Draw.xscl = 1f;

        Draw.rect(handRegion, plan.drawx() + (dx * size * Vars.tilesize), plan.drawy() + (dy * size * Vars.tilesize));
    }

    @Override
    public void drawOverlay(float x, float y, int rotation) {
        int dx = Geometry.d4x(rotation), dy = Geometry.d4y(rotation);
        Drawf.dashLine(Pal.place,
                x + ((dx * size * Vars.tilesize) / 2f),
                y + ((dy * size * Vars.tilesize) / 2f),
                x + dx * ((size * Vars.tilesize) - 2f),
                y + dy * ((size * Vars.tilesize) - 2f)
        );
        Drawf.square(x + (dx * size * Vars.tilesize), y + (dy * size * Vars.tilesize), 2f, 45f, Pal.place);
    }

    public class PayloadFrontLoaderBuild extends Building {
        @Override
        public void updateTile() {
            if (getFrontPayload() instanceof BuildPayload buildPayload) {
                actOnPayload(buildPayload);
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return items.total() < itemCapacity;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return liquids.current() == liquid || liquids.currentAmount() < 0.2f;
        }

        public void actOnPayload(BuildPayload payload) {
            // load up items
            if (payload.block().hasItems && items.any()) {
                if (efficiency > 0.01f && timer(timerLoad, loadTime / efficiency)) {
                    // load up items a set amount of times
                    for (int j = 0; j < itemsLoaded && items.any(); j++) {
                        for (int i = 0; i < items.length(); i++) {
                            if (items.get(i) > 0) {
                                Item item = Vars.content.item(i);
                                if (payload.build.acceptItem(payload.build, item)) {
                                    payload.build.handleItem(payload.build, item);
                                    items.remove(item, 1);
                                    break;
                                } else if (payload.block().separateItemCapacity || payload.block().consumesItem(item)) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // load up liquids
            if (payload.block().hasLiquids && liquids.currentAmount() >= 0.001f) {
                Liquid liq = liquids.current();
                float total = liquids.currentAmount();
                float flow = Math.min(Math.min(liquidsLoaded * edelta(), payload.block().liquidCapacity - payload.build.liquids.get(liq)), total);
                // TODO potential crash here
                if (payload.build.acceptLiquid(payload.build, liq)) {
                    if (!(payload.block() instanceof LiquidVoid)) {
                        payload.build.liquids.add(liq, flow);
                    }
                    liquids.remove(liq, flow);
                }
            }

            // load up power
            if (payloadHasBattery(payload)) {
                // base power input that in raw units
                float powerInput = power.status * (basePowerUse + maxPowerConsumption);
                // how much is actually usable
                float availableInput = Math.max(powerInput - basePowerUse, 0f);

                // charge the battery
                float cap = payload.block().consPower.capacity;
                payload.build.power.status += availableInput / cap * edelta();

                // export if full
                if (payload.build.power.status >= 1f) {
                    payload.build.power.status = Mathf.clamp(payload.build.power.status);
                }
            }
        }

        public boolean payloadHasBattery(BuildPayload payload) {
            return payload != null && payload.block().consPower != null && payload.block().consPower.buffered;
        }

        public Payload getFrontPayload() {
            int dx = Geometry.d4x(rotation), dy = Geometry.d4y(rotation);
            Tile other = tile.nearby(dx * size, dy * size);
            if (other != null && other.build != null) {
                return other.build.getPayload();
            }
            return null;
        }

        @Override
        public void draw() {
            float r = drawrot() - 90f;
            float topScl = Mathf.sign(drawrot() > 45f && drawrot() < 225f);
            int dx = Geometry.d4x(rotation), dy = Geometry.d4y(rotation);

            Draw.rect(region, x, y);

            Draw.z(Layer.blockOver + 0.2f);
            Draw.xscl = topScl;
            Draw.rect(bridgeRegion, x, y, r);
            Draw.rect(topRegion, x, y, r);
            Draw.xscl = 1f;

            Draw.rect(handRegion, x + (dx * size * Vars.tilesize), y + (dy * size * Vars.tilesize));

            // outline
            Draw.z(Draw.z() - 0.001f);
            Draw.xscl = topScl;
            Draw.rect(bridgeOutlineRegion, x, y, r);
            Draw.rect(topOutlineRegion, x, y, r);
            Draw.xscl = 1f;

            Draw.rect(handOutlineRegion, x + (dx * size * Vars.tilesize), y + (dy * size * Vars.tilesize));
        }
    }
}