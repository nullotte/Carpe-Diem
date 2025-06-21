package carpediem.graphics;

import arc.util.pooling.*;
import carpediem.*;
import carpediem.graphics.CableGlowRenderer.*;

// truly quite incredible
public class DrawCD {
    public static void addCableGlowDraw(float alpha, Runnable run) {
        CarpeDiem.cableGlowRenderer.glows.add(Pools.obtain(DrawGlowRequest.class, DrawGlowRequest::new).set(alpha, run));
    }
}
