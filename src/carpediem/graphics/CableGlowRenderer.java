package carpediem.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.struct.*;
import arc.util.pooling.*;
import arc.util.pooling.Pool.*;
import mindustry.graphics.*;

public class CableGlowRenderer {
    public FrameBuffer buffer = new FrameBuffer();
    public Seq<DrawGlowRequest> glows = new Seq<>();

    // yes this is hardcoding the glow color Whatever i dont care its not like im adding green cables
    public Color glowColor = Pal.turretHeat;

    public void draw() {
        buffer.getTexture().setFilter(TextureFilter.linear);
        buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());

        Draw.color();
        buffer.begin(Color.clear);

        glows.sort(g -> -g.alpha);
        for (DrawGlowRequest glow : glows) {
            Draw.color(Color.black, glowColor, glow.alpha);
            glow.run.run();
            Pools.free(glow);
        }

        Draw.reset();
        buffer.end();

        Blending.additive.apply();
        buffer.blit(Shaders.screenspace);
        Blending.normal.apply();

        glows.clear();
    }

    // someone is going to yell at me for this. and it will be entirely justified. but whatever
    public static class DrawGlowRequest implements Poolable {
        public float alpha;
        public Runnable run;

        public DrawGlowRequest set(float alpha, Runnable run) {
            this.alpha = alpha;
            this.run = run;
            return this;
        }

        @Override
        public void reset() {

        }
    }
}
