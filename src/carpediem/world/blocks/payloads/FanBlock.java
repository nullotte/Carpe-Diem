package carpediem.world.blocks.payloads;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.pooling.*;
import arc.util.pooling.Pool.*;
import carpediem.world.blocks.payloads.ProcessableBlock.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.draw.*;

// ??? where do i put this
public class FanBlock extends Block {
    protected static final Rand rand = new Rand();

    public float range = 40f * 8f;
    public float pushStrength = 75f;

    public int particles = 30;
    public float particleLife = 100f, particleOffset = 12f, particleLength = 4f;
    public float particleAlpha = 0.5f, particleFadeMargin = 0.1f;
    public float particleLayer = Layer.power;
    public Color particleColor = Color.white;

    public Seq<FanProcessingType> processingTypes = new Seq<>();

    public DrawBlock drawer;

    public FanBlock(String name) {
        super(name);
        update = true;
        solid = true;
        rotate = true;
    }

    @Override
    public void load() {
        super.load();

        drawer.load(this);
    }

    @Override
    public void init() {
        super.init();
        clipSize = range;
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    protected TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    public class FanBuild extends Building {
        public float warmup;
        public float totalProgress;
        public Seq<FanFlowData> flowData = new Seq<>();

        @Override
        public void updateTile() {
            for (FanFlowData data : flowData) {
                Pools.free(data);
            }
            flowData.clear();

            if (efficiency > 0f) {
                warmup = Mathf.approachDelta(warmup, 1f, 0.02f);

                // payload processing logic stuff thing
                int dx = Geometry.d4x(rotation), dy = Geometry.d4y(rotation);
                int tileRange = (int) (range / Vars.tilesize);
                FanProcessingType currentType = null;

                for (int i = -tileRange; i <= tileRange; i++) {
                    Tile other = tile.nearby(dx * i, dy * i);
                    if (other != null && other.build != null && other.build != this) {
                        Building build = other.build;

                        // check if the block is a processing requirement
                        if (build.efficiency > 0f) {
                            boolean found = false;
                            for (FanProcessingType type : processingTypes) {
                                if (type == currentType) continue;

                                if (build.block == type.requirement) {
                                    currentType = type;
                                    flowData.add(Pools.obtain(FanFlowData.class, FanFlowData::new).set(type, i * Vars.tilesize));
                                    found = true;
                                    break;
                                }
                            }
                            if (found) continue;
                        }

                        // process the block's payload
                        if (currentType != null) {
                            Payload payload = build.getPayload();
                            if (payload instanceof BuildPayload blockPayload) {
                                if (blockPayload.build instanceof ProcessableBuild processable) {
                                    processable.progress += Time.delta / currentType.baseTime;
                                    if (processable.progress >= 1f) {
                                        Block resultBlock = ((ProcessableBlock) processable.block).resultBlock;
                                        blockPayload.build = resultBlock.newBuilding().create(resultBlock, blockPayload.build.team);
                                    }
                                }
                            }
                        }
                    }
                }

                // push units
                Tmp.v1.trns(rotation * 90f, pushStrength);
                float width = rotation % 2 == 0 ? range * 2f : size * Vars.tilesize;
                float height = rotation % 2 == 0 ? size * Vars.tilesize : range * 2f;
                Units.nearby(x - width / 2f, y - height / 2f, width, height, u -> {
                    u.impulseNet(Tmp.v1);
                });
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, 0.02f);
            }

            totalProgress += warmup * Time.delta;
        }

        @Override
        public float warmup() {
            return warmup;
        }

        @Override
        public float totalProgress() {
            return totalProgress;
        }

        @Override
        public boolean shouldAmbientSound() {
            return efficiency > 0f;
        }

        @Override
        public void draw() {
            drawer.draw(this);

            if (warmup > 0f) {
                float r = rotation * 90f;
                float a = particleAlpha * warmup;
                float base = Time.time / particleLife;
                rand.setSeed(id);
                Draw.z(particleLayer);
                for (int i = 0; i < particles; i++) {
                    float fin = (rand.random(2f) + base) % 1f;
                    float frange = fin * 2f - 1f;
                    float len = range * frange;
                    float offset = rand.range(particleOffset);

                    Draw.color(particleColor);
                    for (FanFlowData data : flowData) {
                        if (len > data.start) {
                            Draw.color(data.type.color);
                        }
                    }

                    Draw.alpha(a * (1f - Mathf.curve(1f - Mathf.slope(fin), 1f - particleFadeMargin)));
                    Lines.lineAngleCenter(
                            x + Angles.trnsx(r, len, offset),
                            y + Angles.trnsy(r, len, offset),
                            r,
                            particleLength
                    );
                }
            }
        }
    }

    public static class FanProcessingType {
        public Block requirement;
        public float baseTime;
        public Color color;

        public FanProcessingType(Block requirement, float baseTime, Color color) {
            this.requirement = requirement;
            this.baseTime = baseTime;
            this.color = color;
        }
    }

    public static class FanFlowData implements Poolable {
        public FanProcessingType type;
        public float start;

        public FanFlowData set(FanProcessingType type, float start) {
            this.type = type;
            this.start = start;
            return this;
        }

        @Override
        public void reset() {
            type = null;
            start = 0f;
        }
    }
}
