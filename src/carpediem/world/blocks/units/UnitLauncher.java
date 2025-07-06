package carpediem.world.blocks.units;

import arc.*;
import arc.audio.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import carpediem.audio.*;
import carpediem.graphics.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.EntityCollisions.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.meta.*;

// they just be putting classes anywhere atp
// TODO unit wont land if it's on a block or another unit
// also units just disappear if you remove the launcher while they're mid-launch
public class UnitLauncher extends Block {
    public float range = 240f * 8f;
    public float windupTime = 60f, cooldownTime = 30f;
    public float windupLength = 4f, launchedLength = 10f, launchedSizeMul = 0.4f;
    public float padShadowRadius = 56f;
    public Interp windupInterp = Interp.pow2Out, cooldownInterp = windupInterp; // shut up ok
    public Sound windupSound = CDSounds.launcherWindup, cooldownSound = CDSounds.launcherBoing;

    public TextureRegion padRegion;

    public UnitLauncher(String name) {
        super(name);
        update = true;
        configurable = true;
        clearOnDoubleTap = true;

        config(Vec2.class, (UnitLauncherBuild build, Vec2 pos) -> {
            if (pos.within(build, range)) {
                build.target.set(pos);
            }
        });
        configClear((UnitLauncherBuild build) -> build.target.set(build));
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.range, range / Vars.tilesize, StatUnit.blocks);
    }

    @Override
    public void init() {
        super.init();
        updateClipRadius(range);
    }

    @Override
    public void load() {
        super.load();
        padRegion = Core.atlas.find(name + "-pad");
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{region, padRegion};
    }

    public class UnitLauncherBuild extends Building {
        public Vec2 target = new Vec2();
        public Seq<LaunchedUnit> units = new Seq<>();
        public float windup, cooldown;
        public boolean playedWindupSound;

        @Override
        public void placed() {
            super.placed();
            target.set(this);
        }

        @Override
        public void updateTile() {
            for (int i = 0; i < units.size; i++) {
                if (units.get(i).update()) {
                    units.remove(i);
                    i--;
                }
            }

            if (efficiency > 0f && !target.epsilonEquals(x, y)) {
                if (cooldown <= 0f) {
                    if (Units.nearbyCheck(x - (size * Vars.tilesize / 2f), y - (size * Vars.tilesize / 2f), size * Vars.tilesize, size * Vars.tilesize, u -> !u.isFlying() && u.team == team)) {
                        windup += getProgressIncrease(windupTime);
                        if (!playedWindupSound) {
                            windupSound.at(this);
                            playedWindupSound = true;
                        }
                    } else {
                        windup = Mathf.maxZero(windup - getProgressIncrease(windupTime) * 2f);
                        playedWindupSound = false;
                    }

                    if (windup >= 1f) {
                        Units.nearby(team, x - (size * Vars.tilesize / 2f), y - (size * Vars.tilesize / 2f), size * Vars.tilesize, size * Vars.tilesize, u -> {
                            if (!u.isFlying()) {
                                launchUnit(u);
                            }
                        });

                        cooldownSound.at(this);
                        playedWindupSound = false;
                        windup = 0f;
                        cooldown = cooldownTime;
                    }
                } else {
                    cooldown = Mathf.maxZero(cooldown - edelta());
                }
            }
        }

        @Override
        public void draw() {
            super.draw();
            Tmp.v1.set(target).sub(this);
            if (windup > 0f) {
                Tmp.v1.setLength(windupLength * windupInterp.apply(windup)).inv();
            } else {
                Tmp.v1.setLength(launchedLength * cooldownInterp.apply(cooldown / cooldownTime));
            }

            Draw.scl(1f + launchedSizeMul * (cooldown / cooldownTime));
            Drawf.shadow(x + Tmp.v1.x, y + Tmp.v1.y, padShadowRadius, Math.max(windup, cooldown / cooldownTime));
            Draw.z(Layer.blockOver);
            Draw.rect(padRegion, x + Tmp.v1.x, y + Tmp.v1.y);
            Draw.scl(1f);

            Draw.z(Layer.flyingUnit + 1f);
            units.each(LaunchedUnit::draw);
        }

        public void launchUnit(Unit unit) {
            Vec2 offsetTarget = new Vec2(target).add(unit).sub(this);

            if (unit.isPlayer()) {
                Player p = unit.getPlayer();
                if (p == null) return;
                units.add(new LaunchedUnit(new UnitPayload(unit), new Vec2().set(unit), offsetTarget, p, this));
                unit.remove();
            } else {
                unit.remove();
                units.add(new LaunchedUnit(new UnitPayload(unit), new Vec2().set(unit), offsetTarget));
            }

            unit.vel.setZero();

            if (Vars.net.client()) {
                Vars.netClient.clearRemovedEntity(unit.id);
            }
        }

        @Override
        public boolean onConfigureTapped(float x, float y) {
            configure(Tmp.v1.set(x, y));
            CDFx.launcherSelect.at(Tmp.v1.x, Tmp.v1.y, size * Vars.tilesize / 2f + 1f, new Vec2(this.x, this.y));
            return false;
        }

        @Override
        public void drawConfigure() {
            Drawf.circles(x, y, range);

            Drawf.limitLine(this, Core.input.mouseWorld(), size * Vars.tilesize / 2f + 1f, 3f);
            Drawf.square(Core.input.mouseWorldX(), Core.input.mouseWorldY(), 3f);
            Drawf.circles(x, y, size * Vars.tilesize / 2f + 1f);
        }

        @Override
        public void drawSelect() {
            if (target.epsilonEquals(x, y)) return;
            Drawf.limitLine(this, target, size * Vars.tilesize / 2f + 1f, 3f, Pal.place);
            Drawf.square(target.x, target.y, 3f, Pal.place);
            Drawf.square(x, y, size * Vars.tilesize / 2f + 1f, Pal.place);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(windup);
            write.f(cooldown);

            write.f(target.x);
            write.f(target.y);

            write.i(units.size);
            for (int i = 0; i < units.size; i++) {
                LaunchedUnit unit = units.get(i);

                Payload.write(unit.unit, write);
                write.f(unit.start.x);
                write.f(unit.start.y);
                write.f(unit.end.x);
                write.f(unit.end.y);
                write.f(unit.progress);

                write.bool(unit.player != null);
                if (unit.player != null) {
                    write.bool(unit.player == Vars.player);
                }
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            windup = read.f();
            cooldown = read.f();

            target.x = read.f();
            target.y = read.f();

            int size = read.i();
            for (int i = 0; i < size; i++) {
                UnitPayload unit = Payload.read(read);
                Vec2 start = new Vec2(read.f(), read.f());
                Vec2 end = new Vec2(read.f(), read.f());
                float progress = read.f();
                Player player = null;

                if (read.bool() && read.bool()) {
                    player = Vars.player;
                }

                units.add(new LaunchedUnit(unit, start, end, progress, player, this));
            }
        }
    }

    public static boolean playerDumpUnitPayload(UnitPayload unit, Player player) {
        if (unit.unit.type == null) return false;

        if (!Units.canCreate(unit.unit.team, unit.unit.type)) {
            unit.overlayTime = 1f;
            unit.overlayRegion = null;
            return false;
        }

        //check if unit can be dumped here
        SolidPred solid = unit.unit.solidity();
        if (solid != null) {
            Tmp.v1.trns(unit.unit.rotation, 1f);

            int tx = World.toTile(unit.unit.x + Tmp.v1.x), ty = World.toTile(unit.unit.y + Tmp.v1.y);

            //cannot dump on solid blocks
            if (solid.solid(tx, ty)) return false;
        }

        //cannot dump when there's a lot of overlap going on
        if (!unit.unit.type.flying && Units.count(unit.unit.x, unit.unit.y, unit.unit.physicSize() * 1.05f, o -> o.isGrounded() && (o.type.allowLegStep == unit.unit.type.allowLegStep)) > 0) {
            return false;
        }

        //no client dumping
        if (Vars.net.client()) return true;

        //prevents stacking
        unit.unit.vel.add(Mathf.range(0.5f), Mathf.range(0.5f));
        unit.unit.add();
        unit.unit.unloaded();
        Events.fire(new UnitUnloadEvent(unit.unit));

        // player
        player.unit(unit.unit);
        if (Vars.net.active()) Call.unitControl(player, unit.unit);

        return true;
    }

    // hi betamindy
    // hi sk7725
    public static class LaunchedUnit {
        public UnitPayload unit;
        public Vec2 start, end;
        public float progress; // not 0-1 !!!!!!
        public Player player;
        public BlockUnitc blockUnit;

        public LaunchedUnit(UnitPayload unit, Vec2 start, Vec2 end) {
            this(unit, start, end, null, null);
        }

        public LaunchedUnit(UnitPayload unit, Vec2 start, Vec2 end, Player player, UnitLauncherBuild build) {
            this(unit, start, end, 0f, player, build);
        }

        public LaunchedUnit(UnitPayload unit, Vec2 start, Vec2 end, float progress, Player player, UnitLauncherBuild build) {
            this.unit = unit;
            this.start = start;
            this.end = end;
            this.progress = progress;
            this.player = player;

            if (player != null) {
                blockUnit = (BlockUnitc) UnitTypes.block.create(unit.unit.team);
                player.unit((Unit) blockUnit);
                blockUnit.tile(build);
            }
        }

        public boolean update() {
            if (blockUnit != null && !blockUnit.isPlayer()) {
                blockUnit = null;
                player = null;

                if (unit.unit.spawnedByCore) {
                    Fx.unitDespawn.at(unit.x(), unit.y(), 0f, unit.unit);
                    return true;
                }
            }

            if (progress <= start.dst(end)) {
                progress += 6f * Time.delta;
            } else {
                unit.set(end.x, end.y, start.angleTo(end));

                if ((player != null && playerDumpUnitPayload(unit, player)) || unit.dump()) {
                    Effect.floorDust(end.x, end.y, unit.size() / 8f);
                    return true;
                }
            }

            float fin = progress / start.dst(end);
            Tmp.v1.set(start).lerp(end, fin);
            unit.set(Tmp.v1.x, Tmp.v1.y, start.angleTo(end));
            if (blockUnit != null) {
                blockUnit.set(Tmp.v1);
            }
            if (!Vars.headless && Vars.mobile && player == Vars.player) {
                Core.camera.position.set(Tmp.v1);
            }

            return false;
        }

        public void draw() {
            float fin = progress / start.dst(end);
            float cfin = Interp.pow2Out.apply(Mathf.clamp(Mathf.slope(fin)));

            float z = cfin > 0.5f ? unit.unit.type.flyingLayer : unit.unit.type.groundLayer;

            // jesus christ
            Draw.z(Math.min(Layer.darkness, z - 1f));
            unit.unit.elevation = cfin;
            unit.unit.type.drawShadow(unit.unit);
            Draw.z(Math.min(z - 0.01f, Layer.bullet - 1f));
            if (unit.unit.type.drawSoftShadow) unit.unit.type.drawSoftShadow(unit.unit);

            Tmp.v1.set(start).lerp(end, fin);
            Draw.scl(1f + cfin * 0.6f * (Math.min(start.dst(end), 320f) / 320f));
            Draw.rect(unit.icon(), Tmp.v1.x, Tmp.v1.y, start.angleTo(end) - 90f);
            Draw.reset();
        }
    }
}
