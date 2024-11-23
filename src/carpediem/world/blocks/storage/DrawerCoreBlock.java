package carpediem.world.blocks.storage;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;

public class DrawerCoreBlock extends CoreBlock {
    public DrawBlock drawer = new DrawDefault();
    public float spawnAnimationDuration = 120f;
    public float closeRadius = 30f;

    public DrawerCoreBlock(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        drawer.load(this);
    }

    @Override
    protected TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    // TODO code for drawing on launch platform when v8 releases
    // whole ass copy of the drawlanding method just to make it draw door properly this is terrible
    public void drawLanding(CoreBuild build, float x, float y) {
        float fout = Vars.renderer.getLandTime() / Vars.coreLandDuration;

        if (Vars.renderer.isLaunching()) fout = 1f - fout;

        float fin = 1f - fout;

        float scl = Scl.scl(4f) / Vars.renderer.getDisplayScale();
        float shake = 0f;
        float s = region.width * region.scl() * scl * 3.6f * Interp.pow2Out.apply(fout);
        float rotation = Interp.pow2In.apply(fout) * 135f;
        x += Mathf.range(shake);
        y += Mathf.range(shake);
        float thrustOpen = 0.25f;
        float thrusterFrame = fin >= thrustOpen ? 1f : fin / thrustOpen;
        float thrusterSize = Mathf.sample(thrusterSizes, fin);

        //when launching, thrusters stay out the entire time.
        if (Vars.renderer.isLaunching()) {
            Interp i = Interp.pow2Out;
            thrusterFrame = i.apply(Mathf.clamp(fout * 13f));
            thrusterSize = i.apply(Mathf.clamp(fout * 9f));
        }

        Draw.color(Pal.lightTrail);
        // uh
        Draw.rect("circle-shadow", x, y, s, s);

        Draw.scl(scl);

        //draw thruster flame
        float strength = (1f + (size - 3) / 2.5f) * scl * thrusterSize * (0.95f + Mathf.absin(2f, 0.1f));
        float offset = (size - 3) * 3f * scl;

        for (int i = 0; i < 4; i++) {
            Tmp.v1.trns(i * 90 + rotation, 1f);

            Tmp.v1.setLength((size * Vars.tilesize / 2f + 1f) * scl + strength * 2f + offset);
            Draw.color(build.team.color);
            Fill.circle(Tmp.v1.x + x, Tmp.v1.y + y, 6f * strength);

            Tmp.v1.setLength((size * Vars.tilesize / 2f + 1f) * scl + strength * 0.5f + offset);
            Draw.color(Color.white);
            Fill.circle(Tmp.v1.x + x, Tmp.v1.y + y, 3.5f * strength);
        }

        drawLandingThrusters(x, y, rotation, thrusterFrame);

        Draw.rect(fullIcon, x, y, rotation);
        Drawf.spinSprite(region, x, y, rotation);

        Draw.alpha(Interp.pow4In.apply(thrusterFrame));
        drawLandingThrusters(x, y, rotation, thrusterFrame);
        Draw.alpha(1f);

        if (teamRegions[build.team.id] == teamRegion) Draw.color(build.team.color);

        Drawf.spinSprite(teamRegions[build.team.id], x, y, rotation);

        Draw.color();
        Draw.scl();
        Draw.reset();
    }

    public class DrawerCoreBuild extends CoreBuild {
        public float spawnAnimationTime = 0f;
        public boolean waiting;

        @Override
        public void updateTile() {
            super.updateTile();

            if (waiting && !Units.any(x, y, closeRadius * 2f, closeRadius * 2f, u -> u.team == team && u.type == unitType && u.dst(this) < closeRadius)) {
                waiting = false;
            }

            spawnAnimationTime = Math.max(spawnAnimationTime - Time.delta, waiting ? spawnAnimationDuration * 0.5f : 0f);
        }

        public float spawnAnimationProgress() {
            return spawnAnimationTime / spawnAnimationDuration;
        }

        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public void requestSpawn(Player player) {
            super.requestSpawn(player);
            spawnAnimationTime = spawnAnimationDuration;
            waiting = true;
        }
    }
}
