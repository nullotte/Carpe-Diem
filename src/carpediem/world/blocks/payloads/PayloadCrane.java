package carpediem.world.blocks.payloads;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.world.meta.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.meta.*;

// TODO this thing should be able to like. retract its crane inwards in order to reach blocks next to it
// ALSO ALSO ALSO NONE OF THESE PAYLOAD BLOCKS HAVE LIMITS CURRENTLY THEY CAN LITERALLY PICK UP OMURAS
// also These numbers. They anger me.
public class PayloadCrane extends Block {
    public float hookOffset = 130f, maxExtension = 220f;
    public float extensionSpeed = 0.7f;
    public float rotateSpeed = 0.5f;

    public int segments = 4;
    public float[] segmentLengths = {100f, 70f, 35f};
    public float[] segmentOffsets = {60f, 45f, 25f, 0f};

    public TextureRegion baseRegion, previewRegion, topRegion, outlineRegion, topOutlineRegion;
    public TextureRegion[] segmentRegions, segmentOutlineRegions, hookRegions, hookOutlineRegions;

    public PayloadCrane(String name) {
        super(name);
        update = true;
        configurable = true;
        outlineIcon = true;
        outlinedIcon = 1;

        config(Integer.class, (PayloadCraneBuild crane, Integer i) -> {
            Point2 pos = Point2.unpack(i);

            if (!crane.cranePoints.contains(p -> p.equals(pos.x, pos.y))) {
                // add this position as an input
                crane.cranePoints.add(new CranePoint(pos, false));
            } else {
                CranePoint point = crane.cranePoints.find(p -> p.equals(pos.x, pos.y));

                if (point.output) {
                    // remove it
                    crane.cranePoints.remove(point);
                } else {
                    // make it an output
                    point.output = true;
                }
            }
        });
    }

    @Override
    public void init() {
        super.init();

        updateClipRadius(hookOffset + maxExtension * 1.2f);
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(CDStat.minRange, hookOffset / Vars.tilesize, StatUnit.blocks);
        stats.add(CDStat.maxRange, (hookOffset + maxExtension) / Vars.tilesize, StatUnit.blocks);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(baseRegion, plan.drawx(), plan.drawy());
        Draw.rect(hookOutlineRegions[1], plan.drawx(), plan.drawy() + hookOffset);

        Draw.rect(outlineRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
        for (int i = 0; i < segments; i++) {
            Draw.rect(segmentOutlineRegions[i], plan.drawx(), plan.drawy() + segmentOffsets[i]);
        }
        Draw.rect(topOutlineRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);

        Draw.rect(region, plan.drawx(), plan.drawy(), plan.rotation * 90f);
        for (int i = 0; i < segments; i++) {
            Draw.rect(segmentRegions[i], plan.drawx(), plan.drawy() + segmentOffsets[i]);
        }
        Draw.rect(topRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
    }

    @Override
    public void drawOverlay(float x, float y, int rotation) {
        Drawf.dashCircle(x, y, hookOffset, Pal.place); // TODO this might be confusing
        Drawf.dashCircle(x, y, hookOffset + maxExtension, Pal.placing);
    }

    @Override
    public void load() {
        super.load();
        segmentRegions = new TextureRegion[segments];
        segmentOutlineRegions = new TextureRegion[segments];
        hookRegions = new TextureRegion[2];
        hookOutlineRegions = new TextureRegion[2];

        baseRegion = Core.atlas.find(name + "-base");
        previewRegion = Core.atlas.find(name + "-preview");
        topRegion = Core.atlas.find(name + "-top");

        outlineRegion = Core.atlas.find(name + "-outline");
        topOutlineRegion = Core.atlas.find(name + "-top-outline");

        for (int i = 0; i < segments; i++) {
            segmentRegions[i] = Core.atlas.find(name + "-segment" + (i + 1));
            segmentOutlineRegions[i] = Core.atlas.find(name + "-segment" + (i + 1) + "-outline");
        }

        for (int i = 0; i < 2; i++) {
            hookRegions[i] = Core.atlas.find(name + "-hook" + i);
            hookOutlineRegions[i] = Core.atlas.find(name + "-hook" + i + "-outline");
        }
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{baseRegion, previewRegion};
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out) {
        out.add(region, topRegion);
        out.add(segmentRegions);
        out.add(hookRegions);
    }

    public class PayloadCraneBuild extends Building implements ControlBlock {
        public BlockUnitc unit;
        public float craneRotation = 90f, extension;
        public Vec2 target = new Vec2();
        public Payload payload;
        public Seq<CranePoint> cranePoints = new Seq<>();
        // player control debounce thing
        public boolean acted;

        @Override
        public void updateTile() {
            if (isControlled()) {
                // player control
                target.set(unit.aimX(), unit.aimY());

                if (unit.isShooting()) {
                    if (!acted) {
                        // only attempt if the hook is where the player is aiming
                        if (hookPos().within(target, 0.2f)) {
                            if (payload == null) {
                                tryPickupPayload();
                            } else {
                                tryDropPayload();
                            }

                            acted = true;
                        }
                    }
                } else {
                    acted = false;
                }
            } else {
                CranePoint input = cranePoints.find(p -> {
                    Building build = Vars.world.build(p.pack());
                    return payload == null && !p.output && build != null && build.getPayload() != null;
                });
                CranePoint output = cranePoints.find(p -> {
                    Building build = Vars.world.build(p.pack());
                    return payload != null && p.output && build != null && build.acceptPayload(build, payload);
                });

                if (input != null) {
                    targetPosition(Vars.world.build(input.pack()), true);
                } else if (output != null) {
                    targetPosition(Vars.world.build(output.pack()), false);
                } else {
                    target.sub(this).setLength(1f).add(this);
                }
            }

            // update crane stuff . i dont like how linear it is.... but Oh well
            craneRotation = Angles.moveToward(craneRotation, angleTo(target), rotateSpeed * edelta());
            extension = Mathf.approach(extension, Mathf.clamp(dst(target) - hookOffset, 0f, maxExtension), extensionSpeed * edelta());

            // payload stuff
            if (payload != null) {
                payload.set(hookPos().x, hookPos().y, craneRotation);
                payload.update(null, this);
            }
        }

        @Override
        public void created() {
            // so it doesnt just try to target 0, 0 immediately
            target.set(this).add(0f, 1f);
        }

        public void targetPosition(Position position, boolean pickup) {
            target.set(position);

            if (hookPos().within(target, 0.2f)) {
                if (pickup) {
                    tryPickupPayload();
                } else {
                    tryDropPayload();
                }
            }
        }

        // TODO this should be redone maybe. cant do units with it
        public void tryPickupPayload() {
            if (payload == null) {
                Building build = Vars.world.buildWorld(hookPos().x, hookPos().y);

                if (build != null && Vars.state.teams.canInteract(team, build.team)) {
                    Payload current = build.getPayload();
                    if (current != null) {
                        payload = build.takePayload();
                        Fx.unitPickup.at(build);
                    } else if (build.block.buildVisibility != BuildVisibility.hidden && build.canPickup() && payload == null) {
                        build.pickedUp();
                        build.tile.remove();
                        build.afterPickedUp();
                        payload = new BuildPayload(build);
                        Fx.unitPickup.at(build);
                    }
                }
            }
        }

        public void tryDropPayload() {
            if (payload != null) {
                Tile on = Vars.world.tileWorld(hookPos().x, hookPos().y);
                if (on != null) {
                    if (on.build != null && on.build.acceptPayload(on.build, payload)) {
                        on.build.handlePayload(on.build, payload);
                        payload = null;
                        Fx.unitDrop.at(on.build);
                    } else if (payload instanceof BuildPayload buildPayload) {
                        if (Build.validPlace(buildPayload.block(), buildPayload.build.team, on.x, on.y, buildPayload.build.rotation, false)) {
                            buildPayload.place(on, buildPayload.build.rotation);
                            payload = null;
                            Fx.unitDrop.at(buildPayload.build);
                        }
                    }
                }
            }
        }

        public Vec2 hookPos() {
            return Tmp.v1.trns(craneRotation, hookOffset + extension).add(this);
        }

        @Override
        public boolean onConfigureBuildTapped(Building other) {
            if (this == other) {
                return true;
            } else {
                configure(other.pos());
                return false;
            }
        }

        @Override
        public boolean shouldHideConfigure(Player player) {
            return unit().controller() == player;
        }

        @Override
        public Object config() {
            return cranePoints;
        }

        @Override
        public Unit unit() {
            if (unit == null) {
                unit = (BlockUnitc) UnitTypes.block.create(team);
                unit.tile(this);
            }
            return (Unit) unit;
        }

        @Override
        public boolean shouldAutoTarget() {
            return false;
        }

        @Override
        public boolean canPickup() {
            return false;
        }

        @Override
        public void draw() {
            // my disappointment is immeasurable
            float r = craneRotation - 90f;

            Draw.rect(baseRegion, x, y);

            Draw.z(Layer.blockOver + 0.1f);
            if (payload != null) payload.draw();

            Draw.z(Layer.blockOver + 0.2f);
            Draw.rect(region, x, y, r);
            Draw.rect(hookRegions[Mathf.num(payload == null)], hookPos().x, hookPos().y);

            //wow
            float totalLength = 0;
            for (int i = 0; i < segments; i++) {
                Tmp.v1.trns(craneRotation, Mathf.maxZero(extension - totalLength) + segmentOffsets[i]).add(this);
                if (i < segments - 1) totalLength += segmentLengths[i];

                Draw.z(Layer.blockOver + 0.2f);
                Draw.rect(segmentRegions[i], Tmp.v1.x, Tmp.v1.y, r);
                Draw.z(Draw.z() - 0.001f);
                Draw.rect(segmentOutlineRegions[i], Tmp.v1.x, Tmp.v1.y, r);
            }
            Draw.z(Layer.blockOver + 0.2f);
            Draw.rect(topRegion, x, y, r);

            // outline
            Draw.z(Draw.z() - 0.001f);
            Draw.rect(outlineRegion, x, y, r);
            Draw.rect(topOutlineRegion, x, y, r);
            Draw.rect(hookOutlineRegions[Mathf.num(payload == null)], hookPos().x, hookPos().y);
        }

        @Override
        public void drawConfigure() {
            Drawf.circles(x, y, tile.block().size * Vars.tilesize / 2f + 1f + Mathf.absin(Time.time, 5f, 1f));

            cranePoints.each(p -> {
                Building build = Vars.world.build(p.x, p.y);

                if (build != null) {
                    Drawf.square(build.x, build.y, build.block.size * Vars.tilesize / 2f + 1f, p.output ? Pal.noplace : Pal.place);
                }
            });
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(craneRotation);
            write.f(extension);

            write.f(target.x);
            write.f(target.y);

            Payload.write(payload, write);

            write.i(cranePoints.size);
            for (int i = 0; i < cranePoints.size; i++) {
                CranePoint point = cranePoints.get(i);
                write.i(point.pack());
                write.bool(point.output);
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            craneRotation = read.f();
            extension = read.f();

            target.set(read.f(), read.f());

            payload = Payload.read(read);

            int pointsSize = read.i();
            for (int i = 0; i < pointsSize; i++) {
                int pos = read.i();
                boolean output = read.bool();
                cranePoints.add(new CranePoint(Point2.unpack(pos), output));
            }
        }
    }

    // janky janky janky janky j
    public static class CranePoint extends Point2 {
        public boolean output;

        public CranePoint(Point2 point, boolean output) {
            this.x = point.x;
            this.y = point.y;
            this.output = output;
        }
    }
}
