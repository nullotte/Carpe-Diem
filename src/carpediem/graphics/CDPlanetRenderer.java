package carpediem.graphics;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.g3d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class CDPlanetRenderer extends PlanetRenderer {
    protected static final Rand rand = new Rand();

    public int particles = 100;
    public float particleLife = 240f, particleTravelDistance = 180f,
            particleRadius = 5f, particleLength = 8f,
            particleMinAlpha = 0.4f, particleMaxAlpha = 0.7f;

    public void render(PlanetParams params, Func<Camera3D, Vec3> camUpdater) {
        Draw.flush();
        Gl.clear(Gl.depthBufferBit);
        Gl.enable(Gl.depthTest);
        Gl.depthMask(true);

        Gl.enable(Gl.cullFace);
        Gl.cullFace(Gl.back);

        int w = params.viewW <= 0 ? Core.graphics.getWidth() : params.viewW;
        int h = params.viewH <= 0 ? Core.graphics.getHeight() : params.viewH;

        bloom.blending = !params.drawSkybox;

        //lock to up vector so it doesn't get confusing
        cam.up.set(Vec3.Y);

        cam.resize(w, h);

        //cam.up.set(params.camUp); //TODO broken
        Vec3 truePosition = camUpdater.get(cam);
        cam.update();
        //write back once it changes.
        params.camUp.set(cam.up);

        projector.proj(cam.combined);
        batch.proj(cam.combined);

        Events.fire(EventType.Trigger.universeDrawBegin);

        //begin bloom
        bloom.resize(w, h);
        bloom.capture();

        if (params.drawSkybox) {
            //render skybox at 0,0,0
            Vec3 lastPos = Tmp.v31.set(cam.position);
            cam.position.setZero();
            cam.update();

            Gl.depthMask(false);

            skybox.render(cam.combined);

            Gl.depthMask(true);

            cam.position.set(lastPos);
            cam.update();
        }

        Events.fire(EventType.Trigger.universeDraw);

        Planet solarSystem = params.planet.solarSystem;
        renderPlanet(solarSystem, params);
        renderTransparent(solarSystem, params);

        drawMovementParticles(truePosition);

        //TODO: will draw under icons and look bad. maybe limit arcs based on facing dot product
        if (params.renderer != null) {
            batch.proj().mul(params.planet.getTransform(mat));
            params.renderer.renderOverProjections(params.planet);
        }

        bloom.render();

        Events.fire(EventType.Trigger.universeDrawEnd);

        Gl.enable(Gl.blend);

        if (params.renderer != null) {
            params.renderer.renderProjections(params.planet);
        }

        Gl.disable(Gl.cullFace);
        Gl.disable(Gl.depthTest);

        cam.update();
    }

    public void drawMovementParticles(Vec3 position) {
        float base = Time.globalTime / particleLife;
        rand.setSeed(67);
        for (int i = 0; i < particles; i++) {
            float fin = (rand.random(1f) + base) % 1f, fout = 1f - fin;
            float len = fin * particleTravelDistance;

            Tmp.v31.set(cam.up).setLength(particleRadius + rand.range(0.2f)).rotate(cam.direction, rand.random(360f));
            Tmp.v32.set(cam.direction).scl(-1f).setLength(particleTravelDistance / 2f);

            Tmp.c1.set(Color.white).a(Interp.pow2Out.apply(fout) * rand.random(particleMinAlpha, particleMaxAlpha));
            batch.vertex(Tmp.v33.set(cam.direction).setLength(len).add(Tmp.v32).add(Tmp.v31).add(position), Tmp.c1);
            batch.vertex(Tmp.v33.set(cam.direction).setLength(len + particleLength).add(Tmp.v32).add(Tmp.v31).add(position), Tmp.c1);
            batch.flush(Gl.lines);
        }
    }
}
