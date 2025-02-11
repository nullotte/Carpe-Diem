package carpediem.graphics;

import arc.files.*;
import arc.util.*;
import carpediem.graphics.shaders.*;
import mindustry.*;

public class CDShaders {
    public static DepthShader depth;
    public static DepthAtmosphereShader depthAtmosphere;

    public static void load() {
        depth = new DepthShader();

        try {
            depthAtmosphere = new DepthAtmosphereShader();
        } catch (Throwable t) {
            // ive no fucking idea what im doing ok?
            depthAtmosphere = null;
            Log.err(t);
        }
    }

    public static Fi file(String name) {
        return Vars.tree.get("shaders/" + name);
    }
}
